"use strict";

angular.module("gryf.ti").config(["$stateProvider", function($stateProvider) {
    $stateProvider.state("trainingToReimburse", {
        url: "/trainingToReimburse",
        templateUrl: contextPath + "/templates/trainingtoreimburse/trainingToReimburse.html",
        controller: "TrainingToReimburseController"
    }).state("reimburse", {
        url: "/reimburse/:trainingInstanceId",
        templateUrl: contextPath + "/templates/trainingtoreimburse/reimburse.html",
        controller: "ReimburseController"
    });
}]);