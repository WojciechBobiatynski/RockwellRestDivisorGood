angular.module("gryf.ti").controller("ReimbursementsController", ["$scope", "ReimbursementsService",
    function($scope, ReimbursementsService) {
        $scope.elctRmbsCriteria = ReimbursementsService.getNewCriteria();
        $scope.searchResultOptions = ReimbursementsService.getSearchResultOptions();
        $scope.elctRmbsModel = ReimbursementsService.getElctRmbsModel();

        $scope.datepicker = {
            isRmbsDateFromOpened: false,
            isRmbsDateToOpened: false
        };

        $scope.find = function () {
            ReimbursementsService.find();
        };

        $scope.openDatepicker = function (fieldName) {
            $scope.datepicker[fieldName] = true;
        };

        ($scope.loadReimbursementsStatuses = function () {
            ReimbursementsService.loadReimbursementsStatuses();
        })();
}]);