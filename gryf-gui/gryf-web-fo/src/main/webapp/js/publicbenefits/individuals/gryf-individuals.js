'use strict';

angular.module('gryf.individuals', ['gryf.config', 'gryf.dictionaries', 'gryf.enterprises']);

angular.module('gryf.individuals').config(['$routeProvider', function($routeProvider) {
    $routeProvider
        .when('/', {
            templateUrl: contextPath + '/templates/publicbenefits/individuals/searchformIndividuals.html',
            controller: 'searchform.IndividualController'
        })
        .when('/modify/:id?', {
            templateUrl: contextPath + '/templates/publicbenefits/individuals/detailsformIndividuals.html',
            controller: 'detailsform.IndividualController'
        })
        .otherwise({
            redirectTo: '/'
        });
}]);


