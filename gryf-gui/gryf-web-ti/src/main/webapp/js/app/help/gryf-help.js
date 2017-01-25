"use strict";

angular.module("gryf.ti").config(["$stateProvider", "$urlRouterProvider", function($stateProvider, $urlRouterProvider) {
    $stateProvider.state("help", {
        url: "/help",
        templateUrl: contextPath + "/templates/help/help.html",
        controller: "DashboardController"
    });
}]);