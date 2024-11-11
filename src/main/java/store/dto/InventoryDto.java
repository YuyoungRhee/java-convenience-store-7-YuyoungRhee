package store.dto;

import java.util.List;

public class InventoryDto {
    private final List<ProductDto> products;

    public InventoryDto(List<ProductDto> products) {
        this.products = products;
    }

    public List<ProductDto> getProducts() {
        return products;
    }
}