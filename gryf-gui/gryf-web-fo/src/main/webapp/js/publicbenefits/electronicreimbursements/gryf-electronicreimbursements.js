"use strict";

angular.module("gryf.electronicreimbursements", ["gryf.config", "ngFileUpload", "ui.utils.masks", "gryf.trainingInstitutions", "gryf.trainingInstances", "ngFileUpload"]);

angular.module("gryf.electronicreimbursements").config(["$routeProvider", function($routeProvider) {
    $routeProvider
        .when("/announce/:id?", {
            templateUrl: contextPath + "/templates/publicbenefits/electronicreimbursements/announceform.html",
            controller: "announce.electronicReimbursementsController"
        })
        .when("/search/", {
            templateUrl: contextPath + "/templates/publicbenefits/electronicreimbursements/searchform.html",
            controller: "searchform.electronicReimbursementsController"
        })
        .otherwise({
            redirectTo: "/"
        });
}]);