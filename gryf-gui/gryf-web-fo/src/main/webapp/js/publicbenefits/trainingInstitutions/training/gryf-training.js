'use strict';

angular.module('gryf.training', ['gryf.config', 'gryf.trainingInstitutions']);

angular.module('gryf.training').config(['$routeProvider', function($routeProvider) {
    $routeProvider
        .when('/', {
            templateUrl: contextPath + '/templates/publicbenefits/trainingInstitutions/training/searchformTraining.html',
            controller: 'searchform.TrainingController'
        })
        .when('/modify/:id?', {
            templateUrl: contextPath + '/templates/publicbenefits/trainingInstitutions/training/detailsformTraining.html',
            controller: 'detailsform.TrainingController'
        })
        .otherwise({
            redirectTo: '/'
        });
}]);
