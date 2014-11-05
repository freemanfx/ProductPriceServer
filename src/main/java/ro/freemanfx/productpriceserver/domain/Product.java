package ro.freemanfx.productpriceserver.domain;

import com.google.appengine.api.datastore.*;
import ro.freemanfx.productpriceserver.KeyTypes;

public class Product {
    public static final String NAME = "NAME";
    public static final String BARCODE = "BARCODE";
    private String name;
    private String barcode;

    public Product() {
    }

    public Product(String name, String barcode) {
        this.name = name;
        this.barcode = barcode;
    }

    public static Product find(String barcode) {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Key key = KeyFactory.createKey(KeyTypes.PRODUCT, barcode);
        Query query = new Query(KeyTypes.PRODUCT, key);
        Entity entity = ds.prepare(query).asSingleEntity();
        if (entity == null) {
            return null;
        }
        String nameProperty = (String) entity.getProperty(Product.NAME);
        String barcodeProperty = (String) entity.getProperty(Product.BARCODE);
        return new Product(nameProperty, barcodeProperty);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Entity toNewEntity() {
        Key key = KeyFactory.createKey(KeyTypes.PRODUCT, barcode);
        Entity entity = new Entity(key);
        entity.setProperty(NAME, name);
        entity.setProperty(BARCODE, barcode);
        return entity;
    }
}
