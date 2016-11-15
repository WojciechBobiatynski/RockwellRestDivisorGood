'use strict';

var app = angular.module('gryf.orders',
    ['gryf.config', 'gryf.contracts', 'angularLoad', 'ngFileUpload', "ui.utils.masks"]);

app.config(['$routeProvider', function($routeProvider) {
    $routeProvider
        .when('/', {
            templateUrl: contextPath + '/templates/publicbenefits/orders/searchformOrders.html',
            controller: 'searchform.OrdersController'
        })
        .when('/modify/:id?', {
            templateUrl: contextPath + '/templates/publicbenefits/orders/detailsformOrders.html',
            controller: 'detailsform.OrdersController'
        })
        .when('/preview/:id?', {
            templateUrl: contextPath + '/templates/publicbenefits/orders/previewformOrders.html',
            controller: 'previewform.OrdersController'
        })
        .otherwise({
            redirectTo: '/'
        });
}]);