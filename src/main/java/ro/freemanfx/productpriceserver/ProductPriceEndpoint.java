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
import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static com.google.appengine.api.datastore.Query.FilterOperator.EQUAL;
import static com.google.appengine.api.datastore.Query.FilterPredicate;

@Api(name = "productprice", version = "v1", description = "Get prices for product")
public class ProductPriceEndpoint {
    public static final FetchOptions FETCH_OPTIONS = withLimit(100);

    @ApiMethod(name = "prices", path = "productprice/prices/", httpMethod = GET)
    public List<ProductPrice> getPricesForProduct(@Named("barcode") String productBarcode) {
        List<ProductPrice> productPrices = new LinkedList<>();
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

        Query query = new Query(KeyTypes.PRODUCT_PRICE)
                .setFilter(new FilterPredicate(ProductPrice.PRODUCT, EQUAL, productBarcode));

        PreparedQuery prepared = ds.prepare(query);
        for (Entity entity : prepared.asIterable(FETCH_OPTIONS)) {
            String placeKey = (String) entity.getProperty(ProductPrice.PLACE);
            String productKey = (String) entity.getProperty(ProductPrice.PRODUCT);
            Double price = (Double) entity.getProperty(ProductPrice.PRICE);

            productPrices.add(new ProductPrice(findProduct(productKey), Place.find(placeKey, KeyTypes.PLACE), price));
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

    @ApiMethod(name = "findproduct", path = "productprice/findproduct", httpMethod = GET)
    public Product findProduct(@Named("barcode") String barcode) {
        return Product.find(barcode);
    }
}
