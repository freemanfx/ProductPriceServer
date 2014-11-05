package ro.freemanfx.productpriceserver.domain.fuel;

public final class Gasoline extends Fuel {
    public static final Gasoline STANDARD_95 = new Gasoline("GASOLINE_STANDARD_95");
    public static final Gasoline EXTRA_99 = new Gasoline("GASOLINE_EXTRA_99");

    private Gasoline(String key) {
        super(key);
    }
}
