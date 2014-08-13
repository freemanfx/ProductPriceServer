package ro.freemanfx.productpriceserver.domain.fuel;

import com.google.appengine.api.datastore.Entity;
import ro.freemanfx.productpriceserver.KeyTypes;
import ro.freemanfx.productpriceserver.domain.Place;

public class FuelPrice {
    private String fuel;
    private Place place;
    private Double price;

    public FuelPrice() {
    }

    public FuelPrice(String fuel, Place place, Double price) {
        this.fuel = fuel;
        this.place = place;
        this.price = price;
    }

    public String getFuel() {
        return fuel;
    }

    public Double getPrice() {
        return price;
    }

    public Place getPlace() {
        return place;
    }

    public String makeKey() {
        return fuel + place.getKey() + price.toString();
    }

    public Entity toNewEntity() {
        Entity entity = new Entity(fuel, makeKey());
        entity.setProperty(KeyTypes.GAS_STATION, place.getKey());
        entity.setProperty(KeyTypes.PRICE, price);
        return entity;
    }
}
