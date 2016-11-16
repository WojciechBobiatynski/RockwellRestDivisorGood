"use strict";

angular.module("gryf.ti").config(["$stateProvider", function($stateProvider) {
    $stateProvider.state("trainingToReimburse", {
        url: "/trainingToReimburse",
        templateUrl: contextPath + "/templates/trainingtoreimburse/trainingToReimburse.html",
        controller: "TrainingToReimburseController"
    });
}]);