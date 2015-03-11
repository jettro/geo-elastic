package nl.gridshore.geoelastic.postalcode;

import net.sf.jsefa.common.converter.SimpleTypeConverter;

/**
 * Converter used by jsefa to map input to double
 */
public class DoubleConverterType implements SimpleTypeConverter {

    private static final DoubleConverterType INSTANCE = new DoubleConverterType();

    private DoubleConverterType() {
    }

    public static DoubleConverterType create() {
        return INSTANCE;
    }

    @Override
    public Object fromString(String s) {
        return new Double(s);
    }

    @Override
    public String toString(Object d) {
        return d.toString();
    }

}