package nl.gridshore.geoelastic;

import java.util.ArrayList;

/**
 * Created by jettrocoenradie on 29/04/15.
 */
public class Province {
    private String name;
    private ArrayList<ArrayList<Double>> points;

    public Province() {
    }

    public Province(String name, ArrayList<ArrayList<Double>> points) {
        this.name = name;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ArrayList<Double>> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<ArrayList<Double>> points) {
        this.points = points;
    }
}
