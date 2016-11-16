angular.module("gryf.ti").controller("OurTrainingsController",
    ["$scope", "DictionaryService", "TrainingSearchService",
    function($scope, DictionaryService, TrainingSearchService) {

    $scope.searchDTO = TrainingSearchService.getSearchDTO();
    $scope.searchResultOptions = TrainingSearchService.getSearchResultOptions();

    $scope.dictionaries = {};
    $scope.selectedCategories = {};

    $scope.loadDictionaries = function() {
        $scope.dictionaries.trainingCategories = DictionaryService.loadDictionary(DictionaryService.DICTIONARIES_NAMES.TRAINING_CATEGORIES);
    }

    $scope.find = function() {
        $scope.searchResultOptions = TrainingSearchService.getNewSearchResultOptions();
        $scope.searchResultOptions.badQuery = false;
        TrainingSearchService.find();
    };

    $scope.getSortedBy = function(sortColumnName) {
        $scope.searchResultOptions.badQuery = false;
        TrainingSearchService.findSortedBy(sortColumnName);
    };

    $scope.getSortingTypeClass = function(columnName) {
        return TrainingSearchService.getSortingTypeClass($scope.searchDTO.entity, columnName);
    };

    $scope.datepicker = {
        isTrainingStartDateOpened: false,
        isTrainingEndDateOpened: false
    };

    $scope.openDatepicker = function(value) {
        $scope.datepicker[value] = true;
    };

    $scope.clear = function() {
        $scope.searchResultOptions = TrainingSearchService.getNewSearchResultOptions();
        $scope.searchDTO = TrainingSearchService.getNewSearchDTO();
    };

    $scope.loadMore = function() {
        TrainingSearchService.loadMore();
    };

    $scope.loadDictionaries();
    $scope.clear();
}]);