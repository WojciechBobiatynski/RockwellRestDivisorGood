/**
 * Created by adziobek on 24.10.2016.
 */
'use strict';

angular.module('gryf.contracts', ['gryf.config', 'gryf.enterprises', 'gryf.individuals']);
angular.module('gryf.contracts').config(['$routeProvider', function($routeProvider) {
    $routeProvider
        .when('/', {
            templateUrl: contextPath + '/templates/publicbenefits/contracts/searchformContracts.html',
            controller: 'searchform.ContractsController'
        })
        .when('/modify/:id?', {
            templateUrl: contextPath + '/templates/publicbenefits/contracts/detailsformContracts.html',
            controller: 'detailsform.ContractsController'
        })
        .otherwise({
            redirectTo: '/'
        });
}]);