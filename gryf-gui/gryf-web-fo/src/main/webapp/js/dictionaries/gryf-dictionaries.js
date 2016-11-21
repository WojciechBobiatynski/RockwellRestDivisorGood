'use strict';

angular.module('gryf.dictionaries', ['gryf.config']);

angular.module('gryf.dictionaries').config(['$routeProvider', function($routeProvider) {
    $routeProvider
        .when('/', {
            templateUrl: contextPath + '/templates/dictionaries/zipCodes.html',
            controller: 'zipCodesController'
        })
        .when('/zipCode/:id?', {
            templateUrl: contextPath + '/templates/dictionaries/zipCode.html',
            controller: 'zipCodeController'
        })
        .otherwise({
            redirectTo: '/'
        });
}]);