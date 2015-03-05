function NavbarCtrl($scope) {
    var items = [];

    this.addItem = function (item) {
        items.push(item);
    };

    this.select = $scope.select = function (item) {
        angular.forEach(items, function (item) {
            item.selected = false;
        });
        item.selected = true;
    };

    this.selectByUrl = function (url) {
        angular.forEach(items, function (item) {
            if (item.link == url.split("/")[1]) {
                $scope.select(item);
            }
        });
    };

    this.init = function () {
    };
}
NavbarCtrl.$inject = ['$scope'];
