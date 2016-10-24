'use strict';

angular.module('gryf.trainingInstitutions', ['gryf.config', 'gryf.dictionaries']);

angular.module('gryf.trainingInstitutions').config(['$routeProvider', function($routeProvider) {
    $routeProvider
        .when('/', {
            templateUrl: contextPath + '/templates/publicbenefits/trainingInstitutions/searchformTrainingInstitutions.html',
            controller: 'searchform.TrainingInsController'
        })
        .when('/modify/:id?', {
            templateUrl: contextPath + '/templates/publicbenefits/trainingInstitutions/detailsformTrainingInstitution.html',
            controller: 'detailsform.TrainingInsController'
        })
        .otherwise({
            redirectTo: '/'
        });
}]);