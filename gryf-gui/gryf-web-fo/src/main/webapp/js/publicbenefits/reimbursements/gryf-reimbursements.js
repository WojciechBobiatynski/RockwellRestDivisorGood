'use strict';

var app = angular.module('gryf.reimbursements',
    ['gryf.config', 'ngFileUpload', 'ui.utils.masks', 'gryf.trainingInstitutions',
     'angularLoad', 'gryf.enterprises']);


app.config(['$routeProvider', function($routeProvider) {
    $routeProvider
        .when('/searchDelivery/', {
            templateUrl: contextPath + '/templates/publicbenefits/reimbursements/searchformDeliveries.html',
            controller: 'searchform.DeliveriesController'
        })
        .when('/registerDelivery/:id?', {
            templateUrl: contextPath + '/templates/publicbenefits/reimbursements/registerDelivery.html',
            controller: 'register.DeliveriesController'
        })
        .when('/searchReimbursements/', {
            templateUrl: contextPath + '/templates/publicbenefits/reimbursements/searchformReimbursements.html',
            controller: 'searchform.ReimbursementsController'
        })
        .when('/announceReimbursements/:id?', {
            templateUrl: contextPath + '/templates/publicbenefits/reimbursements/announceReimbursements.html',
            controller: 'announce.ReimbursementsController'
        })
        .otherwise({
            redirectTo: '/'
        });
}]);