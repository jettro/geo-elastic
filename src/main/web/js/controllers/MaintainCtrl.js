function MaintainCtrl($scope, $http, $rootScope) {

    $scope.restorePercolators = function () {
        $http.get('/percolator/add').success(function (data) {
            $scope.province = data;
            createNotification(data);
        });
    };

    function createNotification(message) {
        $rootScope.$broadcast('msg:notification', 'success', message);
    }
}
MaintainCtrl.$inject = ['$scope', '$http', '$rootScope'];

