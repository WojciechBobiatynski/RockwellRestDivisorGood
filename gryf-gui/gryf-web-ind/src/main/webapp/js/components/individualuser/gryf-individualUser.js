"use strict";

angular.module("gryf.ind").config(["$stateProvider", "$urlRouterProvider", function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise("/");

    $stateProvider.state("individualUser", {
        url: "/",
        templateUrl: contextPath + "/templates/individualuser/individualUser.html",
        controller: "IndividualUserController"
    }),
    $stateProvider.state("sendPinModal", {
        parent: "individualUser",
        url: "/sendPin/{trainingInstanceId}",
        onEnter: ["$state", "$modal", function($state, $modal) {
            $modal.open({
                templateUrl: contextPath + "/templates/individualuser/modal/sendPinModal.html",
                size: "md",
                controller: "SendPinModalController"
            }).result.finally(function() {
                $state.go("^");
            });
        }]
    });
}])