/**
 * Created by adziobek on 24.10.2016.
 */
'use strict';

angular.module('gryf.agreements', ['gryf.config']);
angular.module('gryf.agreements').config(['$routeProvider', function($routeProvider) {
    $routeProvider
        .when('/', {
            templateUrl: contextPath + '/templates/publicbenefits/agreements/searchformAgreements.html',
            controller: 'searchform.AgreementsController'
        })
        .when('/modify/:id?', {
            templateUrl: contextPath + '/templates/publicbenefits/agreements/detailsformAgreements.html',
            controller: 'detailsform.AgreementsController'
        })
        .otherwise({
            redirectTo: '/'
        });
}]);