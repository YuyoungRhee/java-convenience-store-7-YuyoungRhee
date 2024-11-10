package store.domain.promotionCondition;

import static org.assertj.core.api.Assertions.assertThat;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PeriodConditionTest {

    @DisplayName("현재 날짜가 기간을 만족하면 true를 반환한다.")
    @Test
    void isSatisfiedBy_true() {
        // given
        LocalDate currentDate = DateTimes.now().toLocalDate();
        LocalDate startDate = currentDate.minusDays(1);
        LocalDate endDate = currentDate.plusDays(1);
        PromotionCondition promotionCondition = new PeriodCondition(startDate, endDate);

        // when
        boolean result = promotionCondition.isSatisfiedBy();

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("현재 날짜가 시작 날짜 이전일 때 false를 반환한다.")
    @Test
    void isSatisfiedBy_falseBeforeStartDate() {
        // given
        LocalDate currentDate = DateTimes.now().toLocalDate();
        LocalDate startDateAfterCurrentDate = currentDate.plusDays(1);
        LocalDate endDateAfterCurrentDate = currentDate.plusDays(2);
        PromotionCondition promotionCondition = new PeriodCondition(startDateAfterCurrentDate, endDateAfterCurrentDate);

        // when
        boolean result = promotionCondition.isSatisfiedBy();

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("현재 날짜가 종료 날짜 이후일 때 false를 반환한다.")
    @Test
    void isSatisfiedBy_falseAfterEndDate() {
        // given
        LocalDate currentDate = DateTimes.now().toLocalDate();
        LocalDate startDateBeforeCurrentDate = currentDate.minusDays(2);
        LocalDate endDateBeforeCurrentDate = currentDate.minusDays(1);
        PromotionCondition promotionCondition = new PeriodCondition(startDateBeforeCurrentDate, endDateBeforeCurrentDate);

        // when
        boolean result = promotionCondition.isSatisfiedBy();

        // then
        assertThat(result).isFalse();
    }

}