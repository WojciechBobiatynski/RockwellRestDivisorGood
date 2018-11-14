angular.module("gryf.ti").controller("OurTrainingsController",
    ["$scope", "DictionaryService", "TrainingSearchService",
    function($scope, DictionaryService, TrainingSearchService) {

    $scope.searchDTO = TrainingSearchService.getSearchDTO();
    $scope.searchResultOptions = TrainingSearchService.getSearchResultOptions();

    $scope.dictionaries = {};
    $scope.selectedCategories = {};

    DictionaryService.loadDictionary(DictionaryService.DICTIONARY.TRAINING_CATEGORIES).then(function(data) {
        $scope.dictionaries.trainingCategories = data;
    });

    $scope.find = function() {
        $scope.searchResultOptions = TrainingSearchService.getNewSearchResultOptions();
        $scope.searchDTO.entity.limit = 10;
        TrainingSearchService.find();
    };

    $scope.getSortedBy = function(sortColumnName) {
        TrainingSearchService.findSortedBy(sortColumnName);
    };

    $scope.getSortingTypeClass = function(columnName) {
        return TrainingSearchService.getSortingTypeClass($scope.searchDTO.entity, columnName);
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

    $scope.clear = function() {
        $scope.searchResultOptions = TrainingSearchService.getNewSearchResultOptions();
        $scope.searchDTO = TrainingSearchService.getNewSearchDTO();
    };

    $scope.loadMore = function() {
        TrainingSearchService.loadMore();
    };

    $scope.clear();

    //Po czysczeniu wszystkich
    TrainingSearchService.getGrantProgramNames().then(function(response) {
       $scope.grantPrograms = response.data;
    });

}]);