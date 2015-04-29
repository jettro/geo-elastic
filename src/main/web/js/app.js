'use strict';

var myApp = angular.module('myApp', ['ngRoute', 'myApp.services', 'myApp.directives.navbar', 'myApp.directives.confirm', 'ui.bootstrap', 'leaflet-directive']);

myApp.config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/dashboard', {templateUrl: '/partials/dashboard.html', controller: 'DashboardCtrl'});
    $routeProvider.when('/maintain', {templateUrl: '/partials/maintain.html', controller: 'MaintainCtrl'});
    $routeProvider.otherwise({redirectTo: '/dashboard'});
}]);

myApp.factory('$exceptionHandler', ['$injector', '$log', function ($injector, $log) {
    return function (exception, cause) {
        $log.error(exception);
        var errorHandling = $injector.get('errorHandling');
        errorHandling.add(exception.message);
        throw exception;
    };
}]);

var serviceModule = angular.module('myApp.services', []);