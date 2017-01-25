"use strict";

angular.module("gryf.ind").config(["$stateProvider", function($stateProvider) {
    $stateProvider.state("help", {
        url: "/help",
        templateUrl: contextPath + "/templates/help/help.html"
    });
}]);