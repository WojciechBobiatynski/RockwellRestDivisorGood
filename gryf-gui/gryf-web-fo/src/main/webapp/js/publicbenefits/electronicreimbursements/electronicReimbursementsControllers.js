"use strict";

angular.module('gryf.electronicreimbursements').controller("searchform.electronicReimbursementsController",
    ['$scope', 'electronicReimbursementsService', function($scope, electronicReimbursementsService) {
        $scope.elctRmbsCriteria = electronicReimbursementsService.getNewCriteria();
        $scope.foundRmbs = electronicReimbursementsService.getFoundRmbs();
        $scope.searchResultOptions = electronicReimbursementsService.getSearchResultOptions();

        $scope.datepicker = {
            reimbursementDateFromOpened: false,
            reimbursementDateToOpened: false
        };

        $scope.find = function () {
            electronicReimbursementsService.find();
        };

        $scope.openDatepicker = function(fieldName) {
            $scope.datepicker[fieldName] = true;
        };

    }]);


angular.module('gryf.electronicreimbursements').controller("detailsform.electronicReimbursementsController",
    ['$scope', 'electronicReimbursementsService', function($scope, electronicReimbursementsService) {

    }]);