"use strict";

angular.module("gryf.password").controller("PasswordController",
    ["$scope", "PasswordService", function ($scope, PasswordService) {
     $scope.passwordModel = PasswordService.getPasswordModel();

     $scope.changePassword = function(){
         PasswordService.changePassword();
     };

}]);