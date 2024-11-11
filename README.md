# java-convenience-store-precourse

## 📝 편의점 프로그램
1. 구매할 상품과 수량을 입력하여 편의점에 있는 물건을 구매한다.
   - 프로모션 상품을 구매할 경우 혜택을 받을 수 있다.
2. 멤버십 할인 적용 여부를 결정하고 결제할 수 있다.
3. 구매한 내역에 대해 영수증을 받을 수 있다.

## 📍 주요 클래스 구조 및 역할

| 클래스             | 역할                                |
|--------------------|-----------------------------------|
| `OrderService`     | 주문을 처리하며 재고와 프로모션을 검증하고 주문 관리를 담당 |
| `Seller`           | 결제 처리를 담당하며 멤버십 할인과 같은 할인 정책을 적용  |
| `Order`            | 개별 주문을 나타내며, 주문된 제품과 수량을 관리       |
| `OrderValidator`   | 재고 유효성을 검증하여 주문 가능 여부를 판단         |
| `Inventory`        | 전체 제품의 재고 상태를 관리하며 주문 시 필요한 정보를 제공 |
| `Product`          | 제품의 이름, 가격, 수량, 프로모션 정보를 포함하여 개별 제품을 나타냄 |
| `Promotion`        | 특정 조건 하에 적용되는 프로모션을 정의하고, 구매 시 추가 상품을 제공하는 등 혜택을 관리 |
| `PromotionCondition` | 프로모션의 시작일과 종료일과 같은 조건을 설정하고 적용 가능 여부를 판단 |

## 📍 기능 목록

### 1. 상품 정보와 프로모션 정보 로드
- 상품 목록과 프로모션 정보를 파일을 통해 불러온다.
    - `src/main/resources/products.md`과 `src/main/resources/promotions.md` 파일
---

### 2. 구매자 주문 입력 받기
- 고객이 주문할 상품과 수량을 입력받는다.
    - 형식: `[콜라-10],[사이다-3]`
- 잘못된 값을 입력하는 경우 "[ERROR]"로 시작하는 오류 메시지  출력 후 재입력을 받는다.
    - `예외처리`: 재고에 없는 상품인 경우
    - `예외처리`: 1개 미만 주문하는 경우
    - `예외처리`: 형식에 맞지 않는 경우
    - `예외처리`: 빈 값인 경우

---

### 3. 프로모션 적용 가능 여부 확인
- 프로모션 대상 상품의 경우, 고객이 지정된 수량 이상 구매 시 자동으로 프로모션이 적용되도록 한다.
    - 프로모션 종류: 1+1, 2+1
- 고객이 프로모션 조건을 충족하지 않은 경우, 부족한 수량을 안내하여 추가 구매를 유도한다.
    - 안내 예시: `현재 콜라는 1개를 무료로 받을 수 있습니다. 추가하시겠습니까? (Y/N)`

---

### 4. 재고 확인 및 처리
- 프로모션 재고를 우선 차감하고, 프로모션 재고가 부족할 경우 일반 재고로 부족한 수량을 차감한다.
- 프로모션 재고가 부족하여 프로모션 혜택을 적용할 수 없는 경우, 정가로 결제할지 여부를 고객에게 묻는다.
    - 안내 예시: `현재 콜라 4개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)`

---

### 5. 멤버십 할인 여부 확인
- 고객에게 멤버십 할인을 받을지 여부를 묻고, `Y` 선택 시 멤버십 할인을 적용한다.
- 프로모션 혜택이 적용되지 않은 금액에 대해 30% 할인을 적용한다.
- 멤버십 할인 금액은 최대 8,000원까지 적용된다.

---

### 6. 최종 결제 금액 계산
- 총 구매액을 계산한다.
    - 상품 가격과 수량을 곱한 값의 총합을 계산하여 기본 구매 금액을 산출한다.
    - 각 상품별 프로모션 할인을 적용한 금액을 반영하여 총구매액을 계산한다.
- 멤버십 할인을 적용한 최종 결제 금액을 산출한다.

---

### 7. 영수증 출력
- 최종 결제 금액과 할인 내역을 포함하여 영수증을 출력한다.
    - 구매 내역(상품명, 수량, 금액)
    - 증정 내역(상품명, 수량)
    - 금액 정보 (총구매액, 행사할인, 멤버십할인, 내실돈)

- 영수증 출력 후 추가 구매 여부를 묻는다.
    - `Y`: 재고 상태를 업데이트한 후 추가 구매로 돌아감
    - `N`: 구매를 종료하고 프로그램 종료
