"use strict";

angular.module("gryf.ti").config(["$stateProvider", function($stateProvider) {
    $stateProvider.state("trainingToReimburse", {
        url: "/trainingToReimburse",
        templateUrl: contextPath + "/templates/trainingtoreimburse/trainingToReimburse.html",
        controller: "TrainingToReimburseController"
    }).state("reimburse", {
        url: "/reimburse/:trainingInstanceId",
        templateUrl: contextPath + "/templates/trainingtoreimburse/reimburse.html",
        resolve: {
            prevStateName: function ($state) {
                return $state.current.name;
            }
        },
        controller: "ReimbursementModifyController"
    }).state("reimburseDetails", {
        url: "/reimburseDetails/:reimbursementId",
        templateUrl: contextPath + "/templates/trainingtoreimburse/reimburse.html",
        controller: "ReimburseDetailsController",
        params : {isDisabled: true},
        resolve: {
            prevStateName: function ($state) {
                return $state.current.name;
            }
        }
    })

    $stateProvider.state("trainingToReimburse.trainingInstanceDetails", {
        parent: "trainingToReimburse",
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