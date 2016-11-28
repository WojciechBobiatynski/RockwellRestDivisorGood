'use strict';

var app = angular.module('gryf.electronicreimbursements',
    ['gryf.config', 'ngFileUpload', 'ui.utils.masks']);


app.config(['$routeProvider', function($routeProvider) {
    $routeProvider
        .when('/announce/:id?', {
            templateUrl: contextPath + '/templates/publicbenefits/electronicreimbursements/announceform.html',
            controller: 'announce.electronicReimbursementsController'
        })
        .when('/search/', {
            templateUrl: contextPath + '/templates/publicbenefits/electronicreimbursements/searchform.html',
            controller: 'searchform.electronicReimbursementsController'
        })
        .otherwise({
            redirectTo: '/'
        });
}]);