package ro.freemanfx.productpriceserver.domain;

import com.google.appengine.api.datastore.Entity;
import ro.freemanfx.productpriceserver.KeyTypes;

public class ProductPrice {
    public static final String PRODUCT = "PRODUCT";
    public static final String PLACE = "PLACE";
    public static final String PRICE = "PRICE";
    private Product product;
    private Place place;
    private Double price;

    public ProductPrice() {
    }

    public ProductPrice(Product product, Place place, Double price) {
        this.product = product;
        this.place = place;
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Entity toNewEntity() {
        Entity entity = new Entity(KeyTypes.PRODUCT_PRICE);
        entity.setProperty(PRODUCT, product.getBarcode());
        entity.setProperty(PLACE, place.getKey());
        entity.setProperty(PRICE, price);
        return entity;
    }
}
