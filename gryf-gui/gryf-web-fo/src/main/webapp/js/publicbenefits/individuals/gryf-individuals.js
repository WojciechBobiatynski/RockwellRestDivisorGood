'use strict';

var app = angular.module('gryf.individuals', ['gryf.config', 'gryf.dictionaries', 'gryf.enterprises']);

app.config(['$routeProvider', function($routeProvider) {
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


