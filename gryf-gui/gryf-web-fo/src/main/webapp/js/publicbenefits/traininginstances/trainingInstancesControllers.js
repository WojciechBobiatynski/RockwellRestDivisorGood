"use strict";

angular.module("gryf.trainingInstances").controller("TrainingInstancesSearchController",
    ["$scope", "TrainingInstanceSearchService", function ($scope, TrainingInstanceSearchService) {

    $scope.searchDTO = TrainingInstanceSearchService.getNewCriteria();
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
        $scope.searchDTO = TrainingInstanceSearchService.getNewCriteria();
        $scope.searchResultOptions = TrainingInstanceSearchService.getNewSearchResultOptions();
    };

    $scope.openIsLov = function() {
        TrainingInstanceSearchService.openTrainingInstitutionLov().result.then(function(chosenTI) {
            $scope.searchDTO.entity.institutionId = chosenTI.id;
            $scope.searchDTO.entity.institutionName = chosenTI.name;
        });
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

    $scope.clear();
}]);