package nl.gridshore.geoelastic;

/**
 * Value object for a city.
 */
public class City {
    private String name;
    private double longitude;
    private double latitude;

    public City() {
    }

    public City(String name, double longitude, double latitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "City{" +
                "latitude=" + latitude +
                ", name='" + name + '\'' +
                ", longitude=" + longitude +
                '}';
    }
}
