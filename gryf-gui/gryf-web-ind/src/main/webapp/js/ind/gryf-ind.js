/**
 * Created by adziobek on 20.10.2016.
 */
'use strict';

angular.module('gryf.ind', ['gryf.config']);

angular.module('gryf.ind').config(['$routeProvider', function($routeProvider) {
    $routeProvider
        .when('/', {
            templateUrl: contextPath + '/templates/detailsformInd.html',
            controller: 'indController'
        })
        .otherwise({
            redirectTo: '/'
        });
}]);