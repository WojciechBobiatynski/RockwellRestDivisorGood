"use strict";

angular.module("gryf.ti").config(["$stateProvider", function($stateProvider) {
    $stateProvider.state("trainingReservation", {
        url: "/trainingReservation",
        templateUrl: contextPath + "/templates/trainingreservation/trainingReservation.html",
        controller: "TrainingReservationController"
    }),
    $stateProvider.state('reservationModal', {
        parent: 'trainingReservation',
        url: '/reservationModal/{trainingId}',
        onEnter: ['$state', '$modal', function($state, $modal) {
            $modal.open({
                templateUrl: contextPath + "/templates/trainingreservation/modal/reservationModal.html",
                size: "md",
                controller: "ReservationModalController"
            }).result.finally(function() {
                $state.go("^");
            });
        }]
    });
}])