function DashboardCtrl($scope, $http) {
    $scope.longitude = null;
    $scope.latitude = null;
    $scope.province = null;

    $scope.findProvince = function () {
        $http.get('/percolator/check', {
            'params': {
                'lat': $scope.latitude,
                'lon': $scope.longitude
            }
        }).success(function (data) {
            $scope.province = data;
            $scope.obtainProvince(data);
        });
    };

    $scope.obtainProvince = function (province) {
        $http.get('/percolator/province', {
            'params': {
                'province': province
            }
        }).success(function (data) {
            var dataPoints = new Array(1);
            dataPoints[0] = new Array(1);
            dataPoints[0][0] = data.points;

            var geoData = {
                "type": "FeatureCollection",
                "features": [
                    {
                        "type": "Feature",
                        "id": "province",
                        "properties": {
                            "name": province
                        },
                        "geometry": {
                            "type": "MultiPolygon",
                            "coordinates": dataPoints
                        }
                    }
                ]
            };

            angular.extend($scope, {
                geojson: {
                    data: geoData,
                    style: {
                        fillColor: "green",
                        weight: 2,
                        opacity: 1,
                        color: 'white',
                        dashArray: '3',
                        fillOpacity: 0.7
                    }
                }
            });
        });

    };

    $scope.selectCity = function (lat, lon) {
        $scope.latitude = lat;
        $scope.longitude = lon;
        $scope.city.lat = lat;
        $scope.city.lng = lon;

        $scope.removeMarkers();
        $scope.markers.m1 = {"lat": lat, "lng": lon, "message": "Selected city"};
        $scope.findProvince();
    };

    angular.extend($scope, {
        city: {
            lat: 52.060669,
            lng: 4.494025,
            zoom: 8
        },
        markers: {}
    });

    $scope.addMarkers = function () {
        angular.extend($scope, {
            markers: {
                m1: {
                    lat: 52.060669,
                    lng: 4.494025,
                    message: "Selected city"
                }
            }
        });
    };

    $scope.removeMarkers = function () {
        $scope.markers = {};
    };

    $scope.addMarkers();
}
DashboardCtrl.$inject = ['$scope', '$http'];

