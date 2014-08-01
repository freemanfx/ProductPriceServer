package ro.freemanfx.productpriceserver;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.datastore.*;
import ro.freemanfx.productpriceserver.domain.Place;
import ro.freemanfx.productpriceserver.domain.Product;
import ro.freemanfx.productpriceserver.domain.ProductPrice;

import java.util.LinkedList;
import java.util.List;

import static com.google.api.server.spi.config.ApiMethod.HttpMethod.GET;
import static com.google.api.server.spi.config.ApiMethod.HttpMethod.POST;
import static com.google.appengine.api.datastore.Query.FilterOperator.EQUAL;
import static com.google.appengine.api.datastore.Query.FilterPredicate;
import static java.lang.Double.parseDouble;

@Api(name = "productprice", version = "v1", description = "Get prices for product")
public class ProductPriceEndpoint {

    @ApiMethod(name = "prices", path = "productprice/prices/", httpMethod = GET)
    public List<ProductPrice> getPricesForProduct(@Named("barcode") String productBarcode) {
        List<ProductPrice> productPrices = new LinkedList<>();
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

        Query query = new Query(KeyTypes.PRODUCT_PRICE)
                .setFilter(new FilterPredicate(ProductPrice.PRODUCT, EQUAL, productBarcode));

        PreparedQuery prepared = ds.prepare(query);
        for (Entity entity : prepared.asIterable()) {
            String placeKey = (String) entity.getProperty(ProductPrice.PLACE);
            String productKey = (String) entity.getProperty(ProductPrice.PRODUCT);
            Double price = (Double) entity.getProperty(ProductPrice.PRICE);

            productPrices.add(new ProductPrice(findProduct(productKey), findPlace(placeKey), price));
        }
        return productPrices;
    }

    @ApiMethod(name = "add", path = "productprice/add", httpMethod = POST)
    public void addProductPrice(ProductPrice productPrice) {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        ds.put(productPrice.getPlace().toNewEntity());
        ds.put(productPrice.getProduct().toNewEntity());
        ds.put(productPrice.toNewEntity());
    }

    private Product findProduct(String barcode) {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Key key = KeyFactory.createKey(KeyTypes.PRODUCT, barcode);
        Query query = new Query(KeyTypes.PRODUCT, key);
        Entity entity = ds.prepare(query).asSingleEntity();

        String nameProperty = (String) entity.getProperty(Product.NAME);
        String barcodeProperty = (String) entity.getProperty(Product.BARCODE);
        return new Product(nameProperty, barcodeProperty);
    }

    private Place findPlace(String placeKey) {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Key key = KeyFactory.createKey(KeyTypes.PLACE, placeKey);
        Query query = new Query(KeyTypes.PLACE, key);
        Entity entity = ds.prepare(query).asSingleEntity();

        String name = (String) entity.getProperty(Place.NAME);
        Double latitude = parseDouble((String) entity.getProperty(Place.LATITUDE));
        Double longitude = parseDouble((String) entity.getProperty(Place.LONGITUDE));
        return new Place(name, latitude, longitude);
    }
}
