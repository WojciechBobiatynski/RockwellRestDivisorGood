"use strict";

angular.module("gryf.asynchjobs").controller("AsynchJobsSearchController",
    ["$scope", "GryfModals", "AsynchJobsSearchService",
        function ($scope, GryfModals, AsynchJobsSearchService) {

    $scope.searchObjModel = AsynchJobsSearchService.getSearchObjModel();
    $scope.searchResultOptions = AsynchJobsSearchService.getSearchResultOptions();
    $scope.dictionaries = AsynchJobsSearchService.getDictionaries();

    AsynchJobsSearchService.findJobStatuses();
    AsynchJobsSearchService.findJobTypes();

    $scope.find = function() {
        $scope.searchResultOptions.badQuery = false;
        AsynchJobsSearchService.find();
    };

    $scope.getSortedBy = function(sortColumnName) {
        $scope.searchResultOptions.badQuery = false;
        AsynchJobsSearchService.findSortedBy(sortColumnName);
    };

    $scope.getSortingTypeClass = function(columnName) {
        return AsynchJobsSearchService.getSortingTypeClass($scope.searchObjModel.entity, columnName);
    };

    $scope.clear = function() {
        $scope.searchObjModel = AsynchJobsSearchService.getNewSearchObjModel();
        $scope.searchResultOptions = AsynchJobsSearchService.getNewSearchResultOptions();
    };

    $scope.datepicker = {
        isCreatedDateFromOpened: false,
        isCreatedDateToOpened: false
    };

    $scope.openDatepicker = function(value) {
        $scope.datepicker[value] = true;
    };

    $scope.loadMore = function() {
        AsynchJobsSearchService.loadMore();
    };

    $scope.clear();

}]);

angular.module("gryf.asynchjobs").controller("AsynchJobsModifyController", ["$scope", "$routeParams",
    "GryfModals", "GryfModulesUrlProvider", "AsynchJobsSearchService", "AsynchJobsModifyService",
function ($scope, $routeParams, GryfModals, GryfModulesUrlProvider, AsynchJobsSearchService, AsynchJobsModifyService) {

    $scope.trainingInstanceModel = AsynchJobsSearchService.getTrainingInstanceModel();
    $scope.pinCode = null;
    $scope.violations = AsynchJobsModifyService.getViolations();

    $scope.MODULES = GryfModulesUrlProvider.MODULES;
    $scope.getUrlFor = GryfModulesUrlProvider.getUrlFor;

    if($routeParams.id) {
        AsynchJobsSearchService.findDetailsById($routeParams.id);
    }

    AsynchJobsSearchService.getTiStatuses().success(function(data) {
        $scope.statusesDictionary = data;
    });

    $scope.isInStatus = function(statusId) {
        return $scope.trainingInstanceModel.entity.trainingInstanceStatusId === statusId;
    };

    $scope.cancelTrainingReservation = function() {
        AsynchJobsModifyService.cancelTrainingReservation($scope.trainingInstanceModel.entity.trainingInstanceId)
            .then(function() {
                AsynchJobsSearchService.findDetailsById($routeParams.id);
            });
    };

    $scope.confirmReservationPIN = function() {
        AsynchJobsModifyService.confirmPin($scope.trainingInstanceModel.entity.trainingInstanceId, $scope.pinCode)
            .then(function() {
                AsynchJobsSearchService.findDetailsById($routeParams.id);
            });
    };

}]);