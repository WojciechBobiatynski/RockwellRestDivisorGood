"use strict";

angular.module("gryf.ind").controller("SendPinModalController",
    ["$scope", "$stateParams", "IndividualUserService", function ($scope, $stateParams, IndividualUserService) {

    $scope.close = $scope.$close;
    $scope.model = IndividualUserService.getModel();

    $scope.sendPin = function() {
        IndividualUserService.sendPin($stateParams.trainingInstanceId).then(function() {
            $scope.close(true);
            IndividualUserService.load();
        });
    };
}]);