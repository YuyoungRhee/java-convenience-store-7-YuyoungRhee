package store.dto;

public class OrderRequestDto {
    private final String productName;
    private final int quantity;

    public OrderRequestDto(String productName, int quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }
}
