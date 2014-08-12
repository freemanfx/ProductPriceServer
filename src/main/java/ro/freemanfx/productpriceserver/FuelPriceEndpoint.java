package ro.freemanfx.productpriceserver;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.datastore.*;
import ro.freemanfx.productpriceserver.domain.Place;
import ro.freemanfx.productpriceserver.domain.fuel.FuelPrice;

import java.util.LinkedList;
import java.util.List;

import static com.google.api.server.spi.config.ApiMethod.HttpMethod.GET;
import static com.google.api.server.spi.config.ApiMethod.HttpMethod.POST;

@Api(name = "fuelprice", version = "v1", description = "Prices for fuels")
public class FuelPriceEndpoint {

    public static final int FETCH_LIMIT = 100;

    @ApiMethod(name = "add", path = "fuelprice/add", httpMethod = POST)
    public void add(FuelPrice fuelPrice) {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        ds.put(fuelPrice.getPlace().toNewEntity());
        ds.put(fuelPrice.toNewEntity());
    }

    @ApiMethod(name = "findPrices", path = "fuelprice/find", httpMethod = GET)
    public List<FuelPrice> findPricesFor(@Named("fuelKey") String fuelKey) {
        List<FuelPrice> fuelPrices = new LinkedList<>();

        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query(fuelKey);
        PreparedQuery preparedQuery = ds.prepare(query);

        List<Entity> entities = preparedQuery.asList(FetchOptions.Builder.withLimit(FETCH_LIMIT));

        for (Entity entity : entities) {
            FuelPrice fuelPrice = new FuelPrice(null, findPlace((String) entity.getProperty(KeyTypes.GAS_STATION)), (Double) entity.getProperty(KeyTypes.PRICE));
            fuelPrices.add(fuelPrice);
        }

        return fuelPrices;
    }

    private Place findPlace(String keyString) {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

        Key key = KeyFactory.createKey(KeyTypes.GAS_STATION, keyString);
        Query query = new Query(KeyTypes.GAS_STATION, key);

        Entity entity = ds.prepare(query).asSingleEntity();
        if (entity != null) {
            return new Place((String) entity.getProperty(Place.NAME), (Double) entity.getProperty(Place.LATITUDE), (Double) entity.getProperty(Place.LONGITUDE));
        }
        return null;
    }
}
