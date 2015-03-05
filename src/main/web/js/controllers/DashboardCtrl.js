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
        });
    };

    $scope.selectCity = function (lat, lon) {
        $scope.latitude = lat;
        $scope.longitude = lon;
    };
}
DashboardCtrl.$inject = ['$scope', '$http'];

