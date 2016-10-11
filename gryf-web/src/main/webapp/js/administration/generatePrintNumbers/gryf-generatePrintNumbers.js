'use strict';

angular.module('gryf.generatePrintNumbers', ['gryf.config']).config(['$routeProvider', function ($routeProvider) {
    $routeProvider
        .when('/', {
            templateUrl: contextPath + '/templates/administration/generatePrintNumbers/generatePrintNumbers.html',
            controller: 'GeneratePrintNumbersController'
        })
        .otherwise({
            redirectTo: '/'
        });
}]);

