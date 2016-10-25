/**
 * Created by adziobek on 20.10.2016.
 */

"use strict";

angular.module("gryf.ind").controller("indController",
    ["$scope", "IndService", function ($scope, IndService) {

        $scope.model = IndService.getNewModel();
        $scope.loadInd = function () {
            IndService.load();
        };
       $scope.loadInd();
    }]);