"use strict";

angular.module("gryf.ind").config(["$stateProvider", "$urlRouterProvider", function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise("/dashboard");

    $stateProvider.state("dashboard", {
        url: "/dashboard",
        templateUrl: contextPath + "/templates/dashboard/dashboard.html",
        controller: "IndividualUserController"
    }),
        $stateProvider.state("sendPinModal", {
            parent: "dashboard",
            url: "/sendPin/{trainingInstanceId}",
            onEnter: ["$state", "$modal", function($state, $modal) {
                $modal.open({
                    templateUrl: contextPath + "/templates/dashboard/modal/sendPinModal.html",
                    size: "md",
                    controller: "SendPinModalController"
                }).result.finally(function() {
                    $state.go("^");

                });
            }]
        });


}]);