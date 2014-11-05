package ro.freemanfx.productpriceserver.domain.fuel;

import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY)
public class Fuel {
    private final String key;

    public Fuel(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
