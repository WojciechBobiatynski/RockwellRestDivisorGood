"use strict";

angular.module("gryf.asynchjobs").controller("AsynchJobsSearchController",
    ["$scope", "GryfModals", "AsynchJobsSearchService",
        function ($scope, GryfModals, AsynchJobsSearchService) {

    $scope.searchObjModel = AsynchJobsSearchService.getSearchObjModel();
    $scope.searchResultOptions = AsynchJobsSearchService.getSearchResultOptions();
    $scope.dictionaries = AsynchJobsSearchService.getDictionaries();

    AsynchJobsSearchService.findJobStatuses();
    AsynchJobsSearchService.findJobTypes(null, false);

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
    "GryfModals", "GryfModulesUrlProvider", "AsynchJobsSearchService", "AsynchJobsModifyService", "ImportDataRowsSearchService",
function ($scope, $routeParams, GryfModals, GryfModulesUrlProvider, AsynchJobsSearchService, AsynchJobsModifyService, ImportDataRowsSearchService) {

    $scope.importJobModel = AsynchJobsModifyService.getImportJobModel();
    $scope.violations = AsynchJobsModifyService.getViolations();
    $scope.dictionaries = AsynchJobsSearchService.getDictionaries();

    $scope.isDisabled = false;

    AsynchJobsSearchService.findJobStatuses();
    AsynchJobsSearchService.findGrantPrograms();

    if($routeParams.id) {
        AsynchJobsSearchService.findJobTypes(100, false);
        AsynchJobsModifyService.findDetailsById($routeParams.id);
        $scope.isDisabled = true;
    } else {
        AsynchJobsSearchService.findJobTypes(100, true);
    }

    $scope.findImportTypes = function(grantProgramId) {
        var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING);
        AsynchJobsSearchService.findJobTypes(grantProgramId, true);
        GryfModals.closeModal(modalInstance);
    };

    $scope.createImportJob = function() {
        AsynchJobsModifyService.createImportJob();
        $scope.violations = AsynchJobsModifyService.getNewViolations();
    };

    $scope.newImportJob = function() {
        $scope.isDisabled = false;
        $scope.importJobModel = AsynchJobsModifyService.getNewImportJobModel();
        $scope.violations = AsynchJobsModifyService.getNewViolations();
        window.location = AsynchJobsModifyService.getNewImportJobUrl();
    };


    //import data rows search

    $scope.dataRowsSearchObjModel = ImportDataRowsSearchService.getSearchObjModel();
    $scope.dataRowsSearchResultOptions = ImportDataRowsSearchService.getSearchResultOptions();
    $scope.dataRowsDictionaries = ImportDataRowsSearchService.getDictionaries();

    ImportDataRowsSearchService.findRowStatuses();

    $scope.find = function() {
        $scope.dataRowsSearchResultOptions.badQuery = false;
        ImportDataRowsSearchService.find();
    };

    $scope.isDictionaryEmpty = function(dictionary) {
        if(!dictionary) {
            return false;
        }
        return Object.keys(dictionary).length == 0;
    };

    $scope.showErrorDetails = function(errors) {
        var message = "Lista błędów:\n";
        for(var i=1; i<=errors.length; i++) {
            message += i + ". " + errors[i-1] + "\n";
        }
        GryfModals.openModal(GryfModals.MODALS_URL.ERROR_INFO, {
            message: message
        });
    };

    $scope.getSortedBy = function(sortColumnName) {
        $scope.dataRowsSearchResultOptions.badQuery = false;
        ImportDataRowsSearchService.findSortedBy(sortColumnName);
    };

    $scope.getSortingTypeClass = function(columnName) {
        return ImportDataRowsSearchService.getSortingTypeClass($scope.dataRowsSearchObjModel.entity, columnName);
    };

    $scope.clear = function() {
        $scope.dataRowsSearchObjModel = ImportDataRowsSearchService.getNewSearchObjModel();
        $scope.dataRowsSearchResultOptions = ImportDataRowsSearchService.getNewSearchResultOptions();
    };

    $scope.loadMore = function() {
        ImportDataRowsSearchService.loadMore();
    };

    $scope.clear();

}]);