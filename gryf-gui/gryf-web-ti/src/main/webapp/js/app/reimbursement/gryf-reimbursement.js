"use strict";

angular.module("gryf.ti").config(["$stateProvider", function($stateProvider) {
    $stateProvider.state("reimbursements", {
        url: "/reimbursements",
        templateUrl: contextPath + "/templates/reimbursement/reimbursements.html",
        controller: "ReimbursementsController"
    }).state("correct", {
        url: "/correct/:reimbursementId",
        templateUrl: contextPath + "/templates/trainingtoreimburse/reimburse.html",
        controller: "CorrectionController"
    });
}]);