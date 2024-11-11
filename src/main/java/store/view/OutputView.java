package store.view;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import store.dto.OrderResultDto;
import store.dto.Receipt;

public class OutputView {
    public void printWelcomeMessage() {
        System.out.println("안녕하세요. W편의점입니다.");
        System.out.println("현재 보유하고 있는 상품입니다.");
        System.out.println();
    }

    public void printProductsFromFile(String filePath) {
        Map<String, List<String>> productMap = loadProductsFromFile(filePath);
        printProductInventory(productMap);
    }

    private Map<String, List<String>> loadProductsFromFile(String filePath) {
        ClassLoader classLoader = getClass().getClassLoader();
        Map<String, List<String>> productMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(classLoader.getResource(filePath).getFile()))) {
            reader.readLine(); // 헤더 건너뛰기
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String productInfo = formatProductInfo(parts);
                productMap.computeIfAbsent(parts[0].trim(), k -> new ArrayList<>()).add(productInfo);
            }
        } catch (IOException | NullPointerException e) {
            System.out.println("[ERROR] 파일을 읽는 중 오류가 발생했습니다.");
        }

        return productMap;
    }

    private String formatProductInfo(String[] parts) {
        String name = parts[0].trim();
        int priceValue = Integer.parseInt(parts[1].trim());
        String price = NumberFormat.getNumberInstance(Locale.KOREA).format(priceValue) + "원";

        String quantity = "재고 없음";
        if (!parts[2].trim().equals("0")) {
            quantity = parts[2].trim() + "개";
        }

        String promotion = "";
        if (!parts[3].trim().equals("null")) {
            promotion = parts[3].trim();
        }

        return String.format("- %s %s %s %s", name, price, quantity, promotion);
    }

    private void printProductInventory(Map<String, List<String>> productMap) {
        addNoStockEntries(productMap);
        productMap.values().forEach(stocks -> stocks.forEach(System.out::println));
        System.out.println();
    }

    private void addNoStockEntries(Map<String, List<String>> productMap) {
        for (Map.Entry<String, List<String>> entry : productMap.entrySet()) {
            List<String> stocks = entry.getValue();
            boolean hasGeneralStock = stocks.stream()
                    .anyMatch(s -> !s.contains("재고 없음") && s.split(" ").length == 4);

            if (!hasGeneralStock) {
                String[] firstProduct = stocks.get(0).split(" ");
                String name = firstProduct[1];
                String price = firstProduct[2];
                stocks.add(String.format("- %s %s 재고 없음", name, price));
            }
        }
    }

    public void printReceipt(Receipt receipt) {
        System.out.println();
        System.out.println("===========W 편의점=============");
        System.out.println("상품명\t\t수량\t금액");

        receipt.getOrderResults().forEach(result ->
                System.out.printf("%s\t\t%d\t%d\n", result.getPruductName(), result.getPurchaseQuantity(),
                        result.getTotalPrice())
        );

        System.out.println("===========증\t정=============");
        receipt.getOrderResults().stream()
                .filter(result -> result.getGiftQuantity() > 0)
                .forEach(result ->
                        System.out.printf("%s\t\t%d\n", result.getPruductName(), result.getGiftQuantity())
                );

        System.out.println("==============================");
        System.out.printf("총구매액\t\t%d\t%,d\n",
                receipt.getOrderResults().stream()
                        .mapToInt(OrderResultDto::getPurchaseQuantity)
                        .sum(),
                receipt.getTotalPrice());
        System.out.printf("행사할인\t\t\t-%d\n", receipt.getDiscountPrice());
        System.out.printf("멤버십할인\t\t\t-%d\n", receipt.getFinalDiscountPrice());
        System.out.printf("내실돈\t\t\t%,d\n", receipt.getFinalAmount());
        System.out.println();
    }
}
