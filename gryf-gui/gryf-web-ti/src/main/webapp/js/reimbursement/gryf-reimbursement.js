"use strict";

angular.module("gryf.ti").config(["$stateProvider", function($stateProvider) {
    $stateProvider.state("reimbursements", {
        url: "/reimbursements",
        templateUrl: contextPath + "/templates/reimbursement/reimbursements.html",
        controller: "ReimbursementsController"
    });
}]);