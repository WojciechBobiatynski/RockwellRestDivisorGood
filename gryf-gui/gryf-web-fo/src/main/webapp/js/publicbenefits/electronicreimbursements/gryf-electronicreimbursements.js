'use strict';

var app = angular.module('gryf.electronicreimbursements',
    ['gryf.config', 'ngFileUpload', 'ui.utils.masks']);


app.config(['$routeProvider', function($routeProvider) {
    $routeProvider
        .when('/modify/:id?', {
            templateUrl: contextPath + '/templates/publicbenefits/electronicreimbursements/detailsform.html',
            controller: 'detailsform.electronicReimbursementsController'
        })
        .when('/search/', {
            templateUrl: contextPath + '/templates/publicbenefits/electronicreimbursements/searchform.html',
            controller: 'searchform.electronicReimbursementsController'
        })
        .otherwise({
            redirectTo: '/'
        });
}]);