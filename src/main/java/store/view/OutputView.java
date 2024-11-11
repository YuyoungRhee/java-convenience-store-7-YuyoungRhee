package store.view;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import store.domain.Inventory;
import store.dto.InventoryDto;
import store.dto.OrderResultDto;
import store.dto.ProductDto;
import store.dto.Receipt;

public class OutputView {
    private final NumberFormat currencyFormatter = NumberFormat.getNumberInstance(Locale.KOREA);

    public void printWelcomeMessage() {
        System.out.println("안녕하세요. W편의점입니다.");
        System.out.println("현재 보유하고 있는 상품입니다.");
        System.out.println();
    }

    public void printProducts(InventoryDto inventoryDto) {
        List<ProductDto> productDtos = inventoryDto.getProducts();

        Map<String, List<ProductDto>> groupedProducts = productDtos.stream()
                .collect(Collectors.groupingBy(ProductDto::getName, LinkedHashMap::new, Collectors.toList()));

        groupedProducts.forEach((name, products) -> {
            boolean hasNonPromotionStock = products.stream()
                    .anyMatch(product -> product.getQuantity() > 0 && product.getPromotionName().isBlank());

            products.forEach(this::printProductInfo);

            // 일반 재고가 없는 경우 "재고 없음" 출력
            if (!hasNonPromotionStock) {
                ProductDto firstProduct = products.get(0); // 해당 제품의 기본 정보를 사용
                System.out.printf("- %s %s원 재고 없음%n", firstProduct.getName(), currencyFormatter.format(firstProduct.getPrice()));
            }
        });
    }

    private void printProductInfo(ProductDto productDto) {
        String stockInfo = "재고 없음";
        if (productDto.getQuantity() != 0) {
            stockInfo = productDto.getQuantity() + "개";
        }

        String promotionName = "";
        if (!productDto.getPromotionName().isBlank()) {
            promotionName = productDto.getPromotionName();
        }

        System.out.printf("- %s %s원 %s %s%n",
                productDto.getName(),
                currencyFormatter.format(productDto.getPrice()),
                stockInfo,
                promotionName);
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
        System.out.printf("내실돈\t\t\t%,d", receipt.getFinalAmount());
    }
}
