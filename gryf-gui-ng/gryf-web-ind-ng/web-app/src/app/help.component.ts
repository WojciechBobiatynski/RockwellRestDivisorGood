"use strict";

declare var angular;
declare var contextPath;

angular.module("gryf.ind").config(["$stateProvider", function($stateProvider) {

    $stateProvider.state("help", {
        url: "/help",
        templateUrl: contextPath + "/templates/help/help.html"
    });
}]);
