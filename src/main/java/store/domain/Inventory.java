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

    public void checkStock(String productName, int requestedQuantity) {
        int totalStock = getAvailableStock(productName);
        if (totalStock < requestedQuantity) {
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }
    }

    public int getAvailableStock(String productName) {
        List<Product> products = stock.get(productName);

        if (products == null) {
            throw new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다: 다시 입력해주세요.");
        }

        return products.stream()
                .mapToInt(Product::getQuantity)
                .sum();
    }

    public void reduceProductQuantity(List<Product> products, int requestQuantity) {
        int remainingQuantity = requestQuantity;

        for (Product product : products) {
            int usedQuantity = Math.min(remainingQuantity, product.getQuantity());
            product.reduceQuantity(usedQuantity);
            remainingQuantity -= usedQuantity;
            if (remainingQuantity <= 0) break;
        }
    }

    //getter
    public List<Product> getProducts(String productName) {
        return stock.getOrDefault(productName, Collections.emptyList());
    }

    public Map<String, List<Product>> getStock() {
        return stock;
    }
}
