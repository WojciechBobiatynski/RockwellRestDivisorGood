"use strict";

angular.module('gryf.electronicreimbursements').controller("searchform.electronicReimbursementsController",
    ['$scope', 'electronicReimbursementsService', function($scope, electronicReimbursementsService) {
        $scope.elctRmbsCriteria = electronicReimbursementsService.getNewCriteria();
        $scope.foundRmbs = electronicReimbursementsService.getFoundRmbs();
    }]);


angular.module('gryf.electronicreimbursements').controller("detailsform.electronicReimbursementsController",
    ['$scope', 'electronicReimbursementsService', function($scope, electronicReimbursementsService) {

    }]);