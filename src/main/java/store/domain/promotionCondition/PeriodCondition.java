package store.domain.promotionCondition;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;

public class PeriodCondition implements PromotionCondition{
    private final LocalDate startDate;
    private final LocalDate endDate;


    public PeriodCondition(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public boolean isSatisfiedBy() {
        LocalDate currentDate = DateTimes.now().toLocalDate();
        return currentDate.isAfter(startDate) && currentDate.isBefore(endDate);
    }
}
