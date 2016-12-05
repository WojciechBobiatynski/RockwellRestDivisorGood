"use strict";

angular.module("gryf.trainingInstances", ["gryf.config", "gryf.trainingInstitutions", "gryf.training"]);

angular.module("gryf.trainingInstances").config(["$routeProvider", function($routeProvider) {
    $routeProvider
        .when("/", {
            templateUrl: contextPath + "/templates/publicbenefits/traininginstances/searchformTrainingInstances.html",
            controller: "TrainingInstancesSearchController"
        })
        .when("/modify/:id?", {
            templateUrl: contextPath + "/templates/publicbenefits/traininginstances/detailsformTrainingInstance.html",
            controller: "TrainingInstanceModifyController"
        })
        .otherwise({
            redirectTo: "/"
        });
}]);