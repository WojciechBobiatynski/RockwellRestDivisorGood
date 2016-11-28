"use strict";

angular.module("gryf.trainingInstances", ["gryf.config"]);

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