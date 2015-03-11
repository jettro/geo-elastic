package nl.gridshore.geoelastic.postalcode;

import net.sf.jsefa.csv.annotation.CsvDataType;
import net.sf.jsefa.csv.annotation.CsvField;

/**
 * Value object used to import the csv file with all postal codes.
 * <p/>
 * "id";"postcode";"postcode_id";"pnum";"pchar";"minnumber";"maxnumber";"numbertype";"street";
 * "city";"city_id";"municipality";"municipality_id";"province";"province_code";"lat";"lon";"rd_x";"rd_y";"location_detail";"changed_date"
 */
@CsvDataType
public class CSVPostalcode {

    @CsvField(pos = 1)
    public String id;

    @CsvField(pos = 2)
    public String postalCode;

    @CsvField(pos = 3)
    public String postalCodeId;

    @CsvField(pos = 4)
    public Integer postalCodeNumber;

    @CsvField(pos = 5)
    public String postalCodeChars;

    @CsvField(pos = 6)
    public Integer minNumber;

    @CsvField(pos = 7)
    public Integer maxNumber;

    @CsvField(pos = 8)
    public String numberType;

    @CsvField(pos = 9)
    public String street;

    @CsvField(pos = 10)
    public String city;

    @CsvField(pos = 11)
    public String cityId;

    @CsvField(pos = 12)
    public String municipality;

    @CsvField(pos = 13)
    public String municipalityId;

    @CsvField(pos = 14)
    public String province;

    @CsvField(pos = 15)
    public String provinceCode;

    @CsvField(pos = 16, converterType = DoubleConverterType.class)
    public Double latitude;

    @CsvField(pos = 17, converterType = DoubleConverterType.class)
    public Double longitude;

    @CsvField(pos = 18)
    public String rdX;

    @CsvField(pos = 19)
    public String rdY;

    @CsvField(pos = 20)
    public String locationDetail;

    @CsvField(pos = 21)
    public String updatedDate;

    @Override
    public String toString() {
        return "Postalcode{" +
                "city='" + city + '\'' +
                ", id='" + id + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", postalCodeId='" + postalCodeId + '\'' +
                ", postalCodeNumber=" + postalCodeNumber +
                ", postalCodeChars='" + postalCodeChars + '\'' +
                ", minNumber=" + minNumber +
                ", maxNumber=" + maxNumber +
                ", numberType='" + numberType + '\'' +
                ", street='" + street + '\'' +
                ", cityId='" + cityId + '\'' +
                ", municipality='" + municipality + '\'' +
                ", municipalityId='" + municipalityId + '\'' +
                ", province='" + province + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", rdX='" + rdX + '\'' +
                ", rdY='" + rdY + '\'' +
                ", locationDetail='" + locationDetail + '\'' +
                ", updatedDate='" + updatedDate + '\'' +
                '}';
    }
}
