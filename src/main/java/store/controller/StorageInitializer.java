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
import java.util.List;
import java.util.Map;

import store.domain.Product;
import store.domain.Inventory;
import store.domain.promotion.NoPromotion;
import store.domain.promotion.Promotion;
import store.domain.promotionCondition.PeriodCondition;
import store.domain.promotionCondition.PromotionCondition;

public class StorageInitializer {
    private final String productsFilePath;
    private final String promotionsFilePath;

    public StorageInitializer(String productsFilePath, String promotionsFilePath) {
        this.productsFilePath = productsFilePath;
        this.promotionsFilePath = promotionsFilePath;
    }

    // 상품 초기화
    public Inventory initializeStorage() {
        try {
            Map<String, Promotion> promotions = initializePromotions();
            Map<String, List<Product>> productMap = new HashMap<>();

            try (BufferedReader reader = getBufferedReader(productsFilePath)) {
                reader.readLine(); // 헤더 건너뛰기
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length < 4) {
                        throw new IOException("Invalid data format in products file: " + line);
                    }

                    String name = parts[0].trim();
                    int price = Integer.parseInt(parts[1].trim());
                    int quantity = Integer.parseInt(parts[2].trim());
                    String promotionName = parts[3].trim().equals("null") ? null : parts[3].trim();

                    Promotion promotion = (promotionName == null) ? new NoPromotion() : promotions.getOrDefault(promotionName, new NoPromotion());
                    Product product = new Product(name, price, quantity, promotion);

                    productMap.computeIfAbsent(name, k -> new ArrayList<>()).add(product);
                }
            }

            return new Inventory(productMap);
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize storage from files: " + productsFilePath + ", " + promotionsFilePath, e);
        }
    }

    private Map<String, Promotion> initializePromotions() throws IOException {
        Map<String, Promotion> promotions = new HashMap<>();

        try (BufferedReader reader = getBufferedReader(promotionsFilePath)) {
            reader.readLine(); // 헤더 건너뛰기
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 5) {
                    throw new IOException("Invalid data format in promotions file: " + line);
                }

                String name = parts[0].trim();
                int purchaseQuantity = Integer.parseInt(parts[1].trim());
                int giftQuantity = Integer.parseInt(parts[2].trim());

                LocalDate startDate;
                LocalDate endDate;
                try {
                    startDate = LocalDate.parse(parts[3].trim());
                    endDate = LocalDate.parse(parts[4].trim());
                } catch (DateTimeParseException e) {
                    throw new IOException("Invalid date format in promotions file: " + line, e);
                }

                PromotionCondition periodCondition = new PeriodCondition(startDate, endDate);
                promotions.put(name, new Promotion(purchaseQuantity, giftQuantity, periodCondition));
            }
        }

        return promotions;
    }

    private BufferedReader getBufferedReader(String filePath) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL fileUrl = classLoader.getResource(filePath);
        if (fileUrl == null) {
            throw new IOException("File not found: " + filePath);
        }
        return new BufferedReader(new InputStreamReader(fileUrl.openStream(), StandardCharsets.UTF_8));
    }
}