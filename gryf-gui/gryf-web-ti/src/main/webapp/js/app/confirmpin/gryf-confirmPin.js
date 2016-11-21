"use strict";

angular.module("gryf.ti").config(["$stateProvider", function($stateProvider) {
    $stateProvider.state("confirmPin", {
        url: "/confirmPin",
        templateUrl: contextPath + "/templates/confirmPin/confirmPin.html",
        controller: "ConfirmPinController"
    }),
    $stateProvider.state("confirmPinModal", {
        parent: "confirmPin",
        url: "/confirmPin/{trainingInstanceId}",
        onEnter: ["$state", "$modal", function($state, $modal) {
            $modal.open({
                templateUrl: contextPath + "/templates/confirmPin/modal/confirmPinModal.html",
                size: "md",
                controller: "ConfirmPinModalController"
            }).result.finally(function() {
                $state.go("^");
            });
        }]
    }),
    $stateProvider.state("confirmPin.trainingInstanceDetails", {
        parent: "confirmPin",
        url: "/trainingInstanceDetails/{trainingInstanceId}",
        onEnter: ["$state", "$modal", function($state, $modal) {
            $modal.open({
                templateUrl: contextPath + "/templates/sharedModals/trainingInstanceDetailsModal.html",
                size: "md",
                controller: "TrainingInstanceDetailsModalController"
            }).result.finally(function() {
                $state.go("^");
            });
        }]
    });
}]);