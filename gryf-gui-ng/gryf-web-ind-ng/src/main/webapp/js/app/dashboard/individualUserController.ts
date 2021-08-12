"use strict";

declare var angular;
angular.module("gryf.ind").controller("IndividualUserController", ["$scope", "IndividualUserService", function ($scope, IndividualUserService) {
    $scope.model = IndividualUserService.getModel();
}]);
