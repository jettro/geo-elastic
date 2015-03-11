function MaintainCtrl($scope, $http, $rootScope) {

    $scope.numberPostalCodes = 0;

    $scope.restorePercolators = function () {
        $http.get('/percolator/add').success(function (data) {
            $scope.province = data;
            createNotification(data);
        });
    };

    $scope.restorePostalCodes = function () {
        $http.get('/postalcode/import').success(function (data) {
            createNotification(data);
        });
    };

    $scope.countPostalCodes = function () {
        $http.get('/postalcode/count').success(function (data) {
            $scope.numberPostalCodes = data;
        });
    };

    function createNotification(message) {
        $rootScope.$broadcast('msg:notification', 'success', message);
    }

    $scope.countPostalCodes();
}
MaintainCtrl.$inject = ['$scope', '$http', '$rootScope'];

