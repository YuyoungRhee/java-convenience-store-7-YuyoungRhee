package store.dto;

import java.util.List;
import java.util.stream.Collectors;
import store.domain.Inventory;

public class InventoryDto {
    private final List<ProductDto> products;

    public InventoryDto(List<ProductDto> products) {
        this.products = products;
    }

    public List<ProductDto> getProducts() {
        return products;
    }

    public static InventoryDto from(Inventory inventory) {
        List<ProductDto> productDtos = inventory.getStock().values().stream()
                .flatMap(List::stream)
                .map(product -> new ProductDto(
                        product.getName(),
                        product.getPrice(),
                        product.getQuantity(),
                        product.getPromotion().getPromotionName()))
                .collect(Collectors.toList());

        return new InventoryDto(productDtos);
    }
}