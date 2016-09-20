'use strict';

var app = angular.module('gryf.grantApplications',
    ['gryf.config', 'gryf.dictionaries', 'gryf.enterprises', 'ngFileUpload', 'angularLoad']);

angular.module('gryf.grantApplications').config(['$routeProvider', function($routeProvider) {
    $routeProvider
        .when('/', {
            templateUrl: contextPath + '/templates/publicbenefits/grantapplications/searchformGrantApplication.html',
            controller: 'searchform.GrantApplicationsController'
        })
        .when('/modify/:id?', {
            templateUrl: contextPath + '/templates/publicbenefits/grantapplications/detailsformGrantApplication.html',
            controller: 'detailsform.GrantApplicationsController'
        })
        .otherwise({
            redirectTo: '/'
        });
}]);