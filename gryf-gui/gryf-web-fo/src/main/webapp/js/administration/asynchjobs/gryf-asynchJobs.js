"use strict";

angular.module("gryf.asynchjobs", ["gryf.config", "ngFileUpload"]);

angular.module("gryf.asynchjobs").config(["$routeProvider", function($routeProvider) {
    $routeProvider
        .when("/", {
            templateUrl: contextPath + "/templates/administration/asynchjobs/searchformAsynchJobs.html",
            controller: "AsynchJobsSearchController"
        })
        .when("/modify/:id?", {
            templateUrl: contextPath + "/templates/administration/asynchjobs/detailsformImportJob.html",
            controller: "AsynchJobsModifyController"
        })
        .otherwise({
            redirectTo: "/"
        });
}]);