package store.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import store.domain.Product;
import store.domain.Inventory;
import store.domain.promotion.NoPromotion;
import store.domain.promotion.Promotion;
import store.domain.promotionCondition.PeriodCondition;
import store.domain.promotionCondition.PromotionCondition;

public class StorageInitializer {
    private static final String STORAGE_INITIALIZE_FAILED = "[ERROR] 재고를 초기화하는 데 실패했습니다.";
    private static final String ERROR_FILE_NOT_FOUND = "[ERROR] 파일을 찾을 수 없습니다: ";
    private static final String ERROR_INVALID_DATA_FORMAT = "[ERROR] 데이터 형식이 올바르지 않습니다: ";
    private static final String ERROR_DATE_FORMAT = "[ERROR] 날짜 형식이 올바르지 않습니다: ";

    private final String productsFilePath;
    private final String promotionsFilePath;

    public StorageInitializer(String productsFilePath, String promotionsFilePath) {
        this.productsFilePath = productsFilePath;
        this.promotionsFilePath = promotionsFilePath;
    }

    public Inventory initializeStorage() {
        try {
            Map<String, Promotion> promotions = initializePromotions();
            Map<String, List<Product>> productMap = loadProductMap(promotions);
            return new Inventory(productMap);
        } catch (IOException e) {
            throw new RuntimeException(STORAGE_INITIALIZE_FAILED);
        }
    }

    private Map<String, List<Product>> loadProductMap(Map<String, Promotion> promotions) throws IOException {
        Map<String, List<Product>> productMap = new LinkedHashMap<>();

        try (BufferedReader reader = getBufferedReader(productsFilePath)) {
            reader.readLine(); // 헤더 건너뛰기
            String line;
            while ((line = reader.readLine()) != null) {
                processProductLine(line, productMap, promotions);
            }
        }
        return productMap;
    }

    private void processProductLine(String line, Map<String, List<Product>> productMap, Map<String, Promotion> promotions) throws IOException {
        String[] parts = line.split(",");
        if (parts.length < 4) {
            throw new IOException(ERROR_INVALID_DATA_FORMAT + line);
        }

        String name = parts[0].trim();
        int price = Integer.parseInt(parts[1].trim());
        int quantity = Integer.parseInt(parts[2].trim());
        String promotionName = parts[3].trim().equals("null") ? null : parts[3].trim();

        Promotion promotion = (promotionName == null) ? new NoPromotion() : promotions.getOrDefault(promotionName, new NoPromotion());
        Product product = new Product(name, price, quantity, promotion);

        List<Product> products = productMap.computeIfAbsent(name, k -> new ArrayList<>());

        if (promotion instanceof NoPromotion) {
            products.add(product);
        } else {
            products.add(0, product);
        }
    }

    private Map<String, Promotion> initializePromotions() throws IOException {
        Map<String, Promotion> promotions = new HashMap<>();

        try (BufferedReader reader = getBufferedReader(promotionsFilePath)) {
            reader.readLine(); // 헤더 건너뛰기
            String line;
            while ((line = reader.readLine()) != null) {
                processPromotionLine(line, promotions);
            }
        }

        return promotions;
    }

    private void processPromotionLine(String line, Map<String, Promotion> promotions) throws IOException {
        String[] parts = line.split(",");
        validatePromotionData(parts, line);

        String promotionName = parts[0].trim();
        int purchaseQuantity = Integer.parseInt(parts[1].trim());
        int giftQuantity = Integer.parseInt(parts[2].trim());
        LocalDate startDate = parseDate(parts[3].trim(), line);
        LocalDate endDate = parseDate(parts[4].trim(), line);

        PromotionCondition periodCondition = new PeriodCondition(startDate, endDate);
        promotions.put(promotionName, new Promotion(promotionName, purchaseQuantity, giftQuantity, periodCondition));
    }

    private void validatePromotionData(String[] parts, String line) throws IOException {
        if (parts.length < 5) {
            throw new IOException(ERROR_INVALID_DATA_FORMAT + line);
        }
    }

    private LocalDate parseDate(String datePart, String line) throws IOException {
        try {
            return LocalDate.parse(datePart);
        } catch (DateTimeParseException e) {
            throw new IOException(ERROR_DATE_FORMAT + line);
        }
    }

    private BufferedReader getBufferedReader(String filePath) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL fileUrl = classLoader.getResource(filePath);
        if (fileUrl == null) {
            throw new IOException(ERROR_FILE_NOT_FOUND + filePath);
        }
        return new BufferedReader(new InputStreamReader(fileUrl.openStream(), StandardCharsets.UTF_8));
    }
}