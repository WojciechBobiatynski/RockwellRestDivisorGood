import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'gryf-individual-user-controller',
  templateUrl: './individual-user-controller.component.html',
  styleUrls: ['./individual-user-controller.component.scss']
})

export class IndividualUserControllerComponent implements OnInit {
  constructor() { }
  ngOnInit(): void {
  }
}

angular.module("gryf.ind").controller("IndividualUserController", ["$scope", "IndividualUserService", function ($scope, IndividualUserService) {
  $scope.model = IndividualUserService.getModel();
}]);




