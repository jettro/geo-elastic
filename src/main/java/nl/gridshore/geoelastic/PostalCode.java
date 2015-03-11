package nl.gridshore.geoelastic;

import nl.gridshore.geoelastic.postalcode.CSVPostalcode;

/**
 * Created by jettrocoenradie on 06/03/15.
 */
public class PostalCode {
    private String city;
    private String street;
    private Integer lowestNumber;
    private Integer highestNumber;
    private String numberType;
    private String postalCode;
    private String municipality;
    private String province;
    private String provinceCode;
    private Location location;

    public static PostalCode from(CSVPostalcode csvPostalcode) {
        PostalCode instance = new PostalCode();
        instance.setCity(csvPostalcode.city);
        instance.setStreet(csvPostalcode.street);
        instance.setLowestNumber(csvPostalcode.minNumber);
        instance.setHighestNumber(csvPostalcode.maxNumber);
        instance.setNumberType(csvPostalcode.numberType);
        instance.setPostalCode(csvPostalcode.postalCode);
        instance.setMunicipality(csvPostalcode.municipality);
        instance.setProvince(csvPostalcode.province);
        instance.setProvinceCode(csvPostalcode.provinceCode);
        instance.setLocation(new Location(csvPostalcode.latitude, csvPostalcode.longitude));
        return instance;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getHighestNumber() {
        return highestNumber;
    }

    public void setHighestNumber(Integer highestNumber) {
        this.highestNumber = highestNumber;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Integer getLowestNumber() {
        return lowestNumber;
    }

    public void setLowestNumber(Integer lowestNumber) {
        this.lowestNumber = lowestNumber;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getNumberType() {
        return numberType;
    }

    public void setNumberType(String numberType) {
        this.numberType = numberType;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }


    public static class Location {
        private double lat;
        private double lon;

        public Location() {
        }

        public Location(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }
    }
}
