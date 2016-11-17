"use strict";

angular.module("gryf.ti").config(["$stateProvider", function($stateProvider) {
    $stateProvider.state("cancelReservation", {
        url: "/cancelReservation",
        templateUrl: contextPath + "/templates/canceltrainingreservation/cancelTrainingReservation.html",
        controller: "CancelTrainingReservationController"
    });
}]);