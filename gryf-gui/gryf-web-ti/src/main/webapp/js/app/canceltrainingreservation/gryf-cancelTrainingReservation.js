"use strict";

angular.module("gryf.ti").config(["$stateProvider", function($stateProvider) {
    $stateProvider.state("cancelReservation", {
        url: "/cancelReservation",
        templateUrl: contextPath + "/templates/canceltrainingreservation/cancelTrainingReservation.html",
        controller: "CancelTrainingReservationController"
    }),
    $stateProvider.state('trainingInstanceDetails', {
        parent: 'cancelReservation',
        url: '/trainingInstanceDetails/{trainingInstanceId}',
        onEnter: ['$state', '$modal', function($state, $modal) {
            $modal.open({
                templateUrl: contextPath + "/templates/canceltrainingreservation/modal/trainingInstanceDetailsModal.html",
                size: "md",
                controller: "TrainingInstanceDetailsModalController"
            }).result.finally(function() {
                $state.go("^");
            });
        }]
    }),
        $stateProvider.state('cancelTrainingReservation', {
            parent: 'cancelReservation',
            url: '/cancelTrainingReservation/{trainingInstanceId}',
            onEnter: ['$state', '$modal', function($state, $modal) {
                $modal.open({
                    templateUrl: contextPath + "/templates/canceltrainingreservation/modal/cancelTrainingInstanceDetailsModal.html",
                    size: "md",
                    controller: "CancelTrainingReservationModalController"
                }).result.finally(function() {
                    $state.go("^");
                });
            }]
        });
}]);