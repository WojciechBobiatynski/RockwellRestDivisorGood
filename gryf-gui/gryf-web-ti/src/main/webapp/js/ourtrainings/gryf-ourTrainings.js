"use strict";

angular.module("gryf.ti").config(["$stateProvider", function($stateProvider) {
    $stateProvider.state("ourTrainings", {
        url: "/ourTrainings",
        templateUrl: contextPath + "/templates/ourTrainings/ourTrainings.html",
        controller: "OurTrainingsController"
    });
}]);