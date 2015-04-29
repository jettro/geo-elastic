# geo-elastic
Project to try out things with elasticsearch and geo. There are a number of functions in this application. The following urls are available:

## Add the dutch provinces to the percolator index
Using the url _/percolator/add_ you can drop the current index and add the percolator queries for all the provinces in the netherlands. The shape files for the provinces are obtained from the following website:
http://www.imergis.nl/asp/47.asp

## Check location
Using the url _/percolator/check?lat=&lon=_ you can find province the provided location is in
http://localhost:8080/percolator/check?lat=52.060669&lon=4.494025

You can find the coordinates for a city with the following website
http://www.findlatitudeandlongitude.com/?loc=rotterdam

## Indexing new documents
Using the url _/city/add_ you get a form to enter the name of a city and its longitude/latitude. Before inserting the city we obtain the province for the city and add it to the index.

# references
http://www.mapshaper.org - Conversion of a shape file
http://www.imergis.nl/asp/47.asp - Downloaden van shape files for provinces of The Netherlands
https://github.com/opendatalab-de/geojson-jackson

https://gist.github.com/lekkerduidelijk/4387055

https://github.com/tombatossals/angular-leaflet-directive