"use strict";

import {Component} from "@angular/core";

declare var angular;

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


@Component({
  selector: 'app-sendPinModal',
  templateUrl: './sendPinModal.component.html',
  styleUrls: ['./sendPinModal.component.scss'],
  controller: class SendPinModalController {
    sendPin() {
      IndividualUserService.sendPin($stateParams.trainingInstanceId).then(function() {
        $scope.close(true);
        IndividualUserService.load();
      });
     }
})

export class sendPinModalComponent implements OnEnter {
  stateProvider: any;
  urlRouterProvider: any;


}

