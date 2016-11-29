"use strict";

angular.module("gryf.trainingInstances", ["gryf.config", "gryf.trainingInstitutions", "gryf.training"]);

angular.module("gryf.trainingInstances").config(["$routeProvider", function($routeProvider) {
    $routeProvider
        .when("/", {
            templateUrl: contextPath + "/templates/publicbenefits/traininginstances/searchformTrainingInstances.html",
            controller: "TrainingInstancesSearchController"
        })
        .otherwise({
            redirectTo: "/"
        });
}]);