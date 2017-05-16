"use strict";

angular.module("gryf.password", ["gryf.config"]);

angular.module("gryf.password").config(["$routeProvider", function($routeProvider) {
    $routeProvider
        .when("/", {
            templateUrl: contextPath + "/templates/administration/password/password.html",
            controller: "PasswordController"
        })
        .otherwise({
            redirectTo: "/"
        });
}]);