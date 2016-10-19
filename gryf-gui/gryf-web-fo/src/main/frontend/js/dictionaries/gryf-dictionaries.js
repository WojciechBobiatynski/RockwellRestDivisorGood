'use strict';

var app = angular.module('gryf.dictionaries', ['gryf.config']);

angular.module('gryf.dictionaries').config(['$routeProvider', function($routeProvider) {
    $routeProvider
        .when('/', {
            templateUrl: 'templates/dictionaries/zipCodes.html',
            controller: 'zipCodesController'
        })
        .when('/zipCode/:id?', {
            templateUrl: 'templates/dictionaries/zipCode.html',
            controller: 'zipCodeController'
        })
        .otherwise({
            redirectTo: '/'
        });
}]);