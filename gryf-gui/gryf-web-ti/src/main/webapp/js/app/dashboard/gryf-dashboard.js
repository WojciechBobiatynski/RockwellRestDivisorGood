"use strict";

angular.module("gryf.ti").config(["$stateProvider", "$urlRouterProvider", function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise("/dashboard");

    $stateProvider.state("dashboard", {
        url: "/dashboard",
        templateUrl: contextPath + "/templates/dashboard/dashboard.html",
        controller: "DashboardController"
    });
}]);