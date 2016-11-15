"use strict";

angular.module('gryf.electronicreimbursements').controller("searchform.electronicReimbursementsController",
    ['$scope', 'electronicReimbursementsService', function ($scope, electronicReimbursementsService) {
        $scope.elctRmbsCriteria = electronicReimbursementsService.getNewCriteria();
        $scope.searchResultOptions = electronicReimbursementsService.getSearchResultOptions();
        $scope.elctRmbsModel = electronicReimbursementsService.getElctRmbsModel();

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


angular.module('gryf.electronicreimbursements').controller("detailsform.electronicReimbursementsController",
    ['$scope', 'electronicReimbursementsService', function ($scope, electronicReimbursementsService) {

    }]);