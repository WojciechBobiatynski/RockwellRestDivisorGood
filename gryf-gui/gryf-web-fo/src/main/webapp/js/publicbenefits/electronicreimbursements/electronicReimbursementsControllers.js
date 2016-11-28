"use strict";

angular.module('gryf.electronicreimbursements').controller("searchform.electronicReimbursementsController",
    ['$scope', 'electronicReimbursementsService', function ($scope, electronicReimbursementsService) {
        $scope.elctRmbsCriteria = electronicReimbursementsService.getNewCriteria();
        $scope.searchResultOptions = electronicReimbursementsService.getSearchResultOptions();
        $scope.elctRmbsModel = electronicReimbursementsService.getElctRmbsModel();

        gryfSessionStorage.setUrlToSessionStorage();

        $scope.datepicker = {
            reimbursementDateFromOpened: false,
            reimbursementDateToOpened: false
        };

        $scope.find = function () {
            electronicReimbursementsService.find();
        };

        $scope.openDatepicker = function (fieldName) {
            $scope.datepicker[fieldName] = true;
        };

        ($scope.loadReimbursementsStatuses = function () {
            electronicReimbursementsService.loadReimbursementsStatuses();
        })();

    }]);


angular.module('gryf.electronicreimbursements').controller("announce.electronicReimbursementsController",
    ['$scope', 'electronicReimbursementsService',
    function ($scope, electronicReimbursementsService) {

        gryfSessionStorage.setUrlToSessionStorage();

        $scope.getPrevUrl = function() {
            return gryfSessionStorage.getUrlFromSessionStorage();
        };

        $scope.test = 123123213123;

    }]);