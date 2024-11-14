package store.domain;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import store.dto.InventoryDto;
import store.dto.OrderResultDto;
import store.dto.ProductDto;

public class Inventory {
    private final Map<String, List<Product>> stock;

    public Inventory(Map<String, List<Product>> initialStock) {
        this.stock = new LinkedHashMap<>(initialStock);
    }

    public int getAvailableStock(String productName) {
        List<Product> products = stock.get(productName);

        if (products == null) {
            throw new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다: " + productName);
        }

        return products.stream()
                .mapToInt(Product::getQuantity)
                .sum();
    }

    public OrderResultDto processOrder(String productName, int requestQuantity) {
        List<Product> products = stock.get(productName);

        int giftQuantity = calculateGiftQuantity(products, requestQuantity);
        int totalPrice = calculateTotalPrice(products, requestQuantity);
        int promotionDiscountedPrice = calculatePromotionDiscount(products, requestQuantity);
        reduceProductQuantity(products, requestQuantity);

        return new OrderResultDto(productName, requestQuantity, giftQuantity, totalPrice, promotionDiscountedPrice);
    }

    public InventoryDto toDto() {
        List<ProductDto> productDtos = stock.values().stream()
                .flatMap(List::stream)
                .map(product -> new ProductDto(
                        product.getName(),
                        product.getPrice(),
                        product.getQuantity(),
                        product.getPromotion().getPromotionName()))
                .collect(Collectors.toList());

        return new InventoryDto(productDtos);
    }

    //getter
    public List<Product> getProducts(String productName) {
        return stock.getOrDefault(productName, Collections.emptyList());
    }

    private int calculateGiftQuantity(List<Product> products, int requestQuantity) {
        int giftQuantity = 0;
        int remainingQuantity = requestQuantity;

        for (Product product : products) {
            int usedQuantity = Math.min(remainingQuantity, product.getQuantity());
            giftQuantity += product.calculateGiftQuantity(usedQuantity);
            remainingQuantity -= usedQuantity;
            if (remainingQuantity <= 0) break;
        }

        return giftQuantity;
    }

    private int calculateTotalPrice(List<Product> products, int requestQuantity) {
        int totalPrice = 0;
        int remainingQuantity = requestQuantity;

        for (Product product : products) {
            int usedQuantity = Math.min(remainingQuantity, product.getQuantity());
            totalPrice += product.calculateTotalPrice(usedQuantity);
            remainingQuantity -= usedQuantity;
            if (remainingQuantity <= 0) break;
        }

        return totalPrice;
    }

    private int calculatePromotionDiscount(List<Product> products, int requestQuantity) {
        int promotionDiscountedPrice = 0;
        int remainingQuantity = requestQuantity;

        for (Product product : products) {
            int usedQuantity = Math.min(remainingQuantity, product.getQuantity());
            promotionDiscountedPrice += product.calculateDiscountedPrice(usedQuantity);
            remainingQuantity -= usedQuantity;
            if (remainingQuantity <= 0) break;
        }

        return promotionDiscountedPrice;
    }

    private void reduceProductQuantity(List<Product> products, int requestQuantity) {
        int remainingQuantity = requestQuantity;

        for (Product product : products) {
            int usedQuantity = Math.min(remainingQuantity, product.getQuantity());
            product.reduceQuantity(usedQuantity);
            remainingQuantity -= usedQuantity;
            if (remainingQuantity <= 0) break;
        }
    }

}
