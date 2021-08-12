"use strict";

declare var angular;
angular.module("gryf.ind").config(["$stateProvider", function($stateProvider) {
    let contextPath;
    $stateProvider.state("help", {
        url: "/help",
        templateUrl: contextPath + "/templates/help/help.html"
    });
}]);