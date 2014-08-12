package ro.freemanfx.productpriceserver.domain.fuel;

public final class Diesel extends Fuel {
    public static final Diesel STANDARD = new Diesel("DIESEL_STANDARD");
    public static final Diesel EXTRA = new Diesel("DIESEL_EXTRA");

    private Diesel(String key) {
        super(key);
    }
}
