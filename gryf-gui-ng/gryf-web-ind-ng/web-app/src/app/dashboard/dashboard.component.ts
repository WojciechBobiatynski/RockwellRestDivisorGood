"use strict";

import {Component, OnInit} from "@angular/core";

declare var angular: { module: (arg0: string) => { (): any; new(): any; config: { (arg0: (string | (($stateProvider: any, $urlRouterProvider: any) => void))[]): void; new(): any; }; }; };
declare var contextPath: string;


@Component({
  selector: 'gryf-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})

export class DashboardComponent implements OnInit {
  stateProvider: any;
  urlRouterProvider: any;
  private username: any;
  private authService: any;

  ngOnInit() {
    this.username = this.authService.username;
  }
}


angular.module("gryf.ind").config(["$stateProvider", "$urlRouterProvider", function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise("/dashboard");
    $stateProvider.state("dashboard", {
        url: "/dashboard",
        templateUrl: contextPath + "/templates/dashboard/dashboard.html",
        controller: "IndividualUserController"
    }),
        $stateProvider.state("sendPinModal", {
            parent: "dashboard",
            url: "/sendPin/{trainingInstanceId}",
            onEnter: ["$state", "$modal", function($state, $modal) {
                $modal.open({
                    templateUrl: contextPath + "/templates/dashboard/modal/sendPinModal.html",
                    size: "md",
                    controller: "SendPinModalController"
                }).result.finally(function() {
                    $state.go("^");

                });
            }]
        });

}]);
