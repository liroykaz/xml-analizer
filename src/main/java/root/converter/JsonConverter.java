package root.converter;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * @author Lebedev
 */
public class JsonConverter implements Converter {

    private static final Logger log = Logger.getLogger(JsonConverter.class);

    private static String URL = "https://www.cbr-xml-daily.ru/daily_json.js";

    @Override
    public Double rubToEuroConvert(Double rub) {
        try {
            if (rub != null && rub != 0.0) {
                JSONObject json = new JSONObject(IOUtils.toString(new URL(URL), Charset.forName("UTF-8")));
                if (json.getJSONObject("Valute") != null && json.getJSONObject("Valute").getJSONObject("EUR") != null) {
                    Double euroRate = (Double) json.getJSONObject("Valute").getJSONObject("EUR").get("Value");

                    Double exchangedSum = new BigDecimal(rub / euroRate).setScale(2, RoundingMode.UP).doubleValue();
                    log.info("Sum of items = " + exchangedSum + " (EUR) | EUR rate = " + euroRate);
                    System.out.println("Sum of items = " + exchangedSum + " (EUR) | EUR rate = " + euroRate);

                    return exchangedSum;
                }
            }
        } catch (IOException e) {
            log.error("Error in Converter " + e.getMessage());
        }

        return null;
    }
}
