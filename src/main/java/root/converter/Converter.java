package root.converter;


/**
 * Special interface that converts russian ruble to euro by current rate
 */
public interface Converter {

    /**
     * Convert russian ruble to euro
     * @param rub is number of rubles
     * @return converted value
     */
    Double rubToEuroConvert(Double rub);
}
