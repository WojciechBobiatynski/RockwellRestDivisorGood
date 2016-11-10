"use strict";

angular.module("gryf.ti").config(["$stateProvider", function($stateProvider) {
    $stateProvider.state("trainingReservation", {
        url: "/trainingReservation",
        templateUrl: contextPath + "/templates/trainingReservation/trainingReservation.html",
        controller: "TrainingReservationController"
    });
}]);