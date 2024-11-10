package store.domain.promotion;

public class NoPromotion extends Promotion{

    public NoPromotion() {
        super(0,0, null);
    }

    @Override
    public boolean isActive() {
        return false;
    }

}
