"use strict";

angular.module("gryf.trainingInstances").controller("TrainingInstancesSearchController",
    ["$scope", "GryfModals", "BrowseTrainingInsService", "BrowseTrainingService", "TrainingInstanceSearchService",
        function ($scope, GryfModals, BrowseTrainingInsService, BrowseTrainingService, TrainingInstanceSearchService) {

    $scope.searchDTO = TrainingInstanceSearchService.getNewSearchDTO();
    $scope.searchResultOptions = TrainingInstanceSearchService.getSearchResultOptions();
    $scope.trainingModel = TrainingInstanceSearchService.getTrainingModel();
    $scope.statusesDictionary = null;

    $scope.find = function() {
        $scope.searchResultOptions.badQuery = false;
        TrainingInstanceSearchService.find();
    };

    $scope.getSortedBy = function(sortColumnName) {
        $scope.searchResultOptions.badQuery = false;
        TrainingInstanceSearchService.findSortedBy(sortColumnName);
    };

    $scope.getSortingTypeClass = function(columnName) {
        return TrainingInstanceSearchService.getSortingTypeClass($scope.searchDTO, columnName);
    };

    $scope.clear = function() {
        $scope.searchDTO = TrainingInstanceSearchService.getNewSearchDTO();
        $scope.searchResultOptions = TrainingInstanceSearchService.getNewSearchResultOptions();
    };

    $scope.datepicker = {
        isTrainingStartDateFromOpened: false,
        isTrainingStartDateToOpened: false,
        isTrainingEndDateFromOpened: false,
        isTrainingEndDateToOpened: false
    };

    $scope.openDatepicker = function(value) {
        $scope.datepicker[value] = true;
    };

    TrainingInstanceSearchService.getTiStatuses().success(function(data) {
        $scope.statusesDictionary = data;
    });

    $scope.loadMore = function() {
        TrainingInstanceSearchService.loadMore();
    };

    $scope.openInstitutionLov = function() {
        GryfModals.openLovModal(GryfModals.MODALS_URL.LOV_TI, BrowseTrainingInsService, 'lg').result.then(function(chosenTI) {
            $scope.searchDTO.trainingInstitutionName = chosenTI.name;
            $scope.searchDTO.trainingInstitutionVatRegNum = chosenTI.vatRegNum;
        });
    };

    $scope.openTrainingLov = function() {
        GryfModals.openLovModal(GryfModals.MODALS_URL.LOV_TRAININGS, BrowseTrainingService, 'lg').result.then(function(chosenTI) {
            $scope.searchDTO.trainingId = chosenTI.trainingId;
            $scope.searchDTO.trainingName = chosenTI.name;
        });
    };

    $scope.clear();

}]);

angular.module("gryf.trainingInstances").controller("TrainingInstanceModifyController", ["$scope", "$routeParams",
    "GryfModals", "GryfModulesUrlProvider", "BrowseTrainingInsService", "BrowseTrainingService", "TrainingInstanceSearchService", "TrainingInstanceModifyService",
function ($scope, $routeParams, GryfModals, GryfModulesUrlProvider, BrowseTrainingInsService, BrowseTrainingService, TrainingInstanceSearchService, TrainingInstanceModifyService) {

    $scope.trainingInstanceModel = TrainingInstanceSearchService.getTrainingInstanceModel();
    $scope.pinCode = null;
    $scope.violations = TrainingInstanceModifyService.getViolations();

    $scope.MODULES = GryfModulesUrlProvider.MODULES;
    $scope.getUrlFor = GryfModulesUrlProvider.getUrlFor;

    if($routeParams.id) {
        TrainingInstanceSearchService.findDetailsById($routeParams.id);
    }

    TrainingInstanceSearchService.getTiStatuses().success(function(data) {
        $scope.statusesDictionary = data;
    });

    $scope.isInStatus = function(statusId) {
        return $scope.trainingInstanceModel.entity.trainingInstanceStatusId === statusId;
    };

    $scope.cancelTrainingReservation = function() {
        TrainingInstanceModifyService.cancelTrainingReservation($scope.trainingInstanceModel.entity.trainingInstanceId)
            .then(function() {
                TrainingInstanceSearchService.findDetailsById($routeParams.id);
            });
    };

    $scope.confirmReservationPIN = function() {
        TrainingInstanceModifyService.confirmPin($scope.trainingInstanceModel.entity.trainingInstanceId, $scope.pinCode)
            .then(function() {
                TrainingInstanceSearchService.findDetailsById($routeParams.id);
            });
    };

}]);