package nl.gridshore.geoelastic.postalcode;

import net.sf.jsefa.Deserializer;
import net.sf.jsefa.common.lowlevel.filter.HeaderAndFooterFilter;
import net.sf.jsefa.csv.CsvIOFactory;
import net.sf.jsefa.csv.config.CsvConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Service used to import a csv file with all postal code from the netherlands
 * <p/>
 * <p/>
 * http://www.postcodedata.nl/download/
 */
@Service
public class ImportService {
    private static final Logger logger = LoggerFactory.getLogger(ImportService.class);

    @Value("${data.import.postalcodes}")
    private String fileName;

    public void importPostalCodes(PostalCodeHandler handler) {
        CsvConfiguration config = new CsvConfiguration();
        config.setLineFilter(new HeaderAndFooterFilter(1, false, true));
        config.setFieldDelimiter(';');

        Deserializer deserializer = CsvIOFactory.createFactory(config, CSVPostalcode.class).createDeserializer();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
            deserializer.open(bufferedReader);
            while (deserializer.hasNext()) {
                handler.handle(deserializer.next());
            }
            deserializer.close(true);
        } catch (IOException e) {
            logger.error("Problem importing the file {}", fileName, e);
        }

    }

}
