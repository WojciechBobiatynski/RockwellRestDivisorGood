'use strict';

angular.module('gryf.enterprises', ['gryf.config', 'gryf.dictionaries']);

angular.module('gryf.enterprises').config(['$routeProvider', function($routeProvider) {
    $routeProvider
        .when('/', {
            templateUrl: contextPath + '/templates/publicbenefits/enterprises/searchformEnterprises.html',
            controller: 'searchform.EnterpriseController'
        })
        .when('/modify/:id?', {
            templateUrl: contextPath + '/templates/publicbenefits/enterprises/detailsformEnterprises.html',
            controller: 'detailsform.EnterpriseController'
        })
        .otherwise({
            redirectTo: '/'
        });
}]);


