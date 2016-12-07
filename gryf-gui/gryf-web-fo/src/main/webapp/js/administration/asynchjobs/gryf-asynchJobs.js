"use strict";

angular.module("gryf.asynchjobs", ["gryf.config"]);

angular.module("gryf.asynchjobs").config(["$routeProvider", function($routeProvider) {
    $routeProvider
        .when("/", {
            templateUrl: contextPath + "/templates/administration/asynchjobs/searchformAsynchJobs.html",
            controller: "AsynchJobsSearchController"
        })
        .when("/modify/:id?", {
            templateUrl: contextPath + "/templates/administration/asynchjobs/detailsformAsynchJobs.html",
            controller: "AsynchJobsModifyController"
        })
        .otherwise({
            redirectTo: "/"
        });
}]);