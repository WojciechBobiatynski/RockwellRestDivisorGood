angular.module("gryf.ti").controller("TrainingReservationController",
    ["$scope", "UserService", "TrainingReservationService", "DictionaryService", "TrainingSearchService",
    function($scope, UserService, TrainingReservationService, DictionaryService, TrainingSearchService) {

    $scope.individualUser = UserService.getIndividualUser();
    $scope.userTrainingReservationData = TrainingReservationService.getUserTrainingReservationData();
    $scope.violations = TrainingReservationService.getNewViolations();
    $scope.searchDTO = TrainingSearchService.getSearchDTO();
    $scope.searchResultOptions = TrainingSearchService.getSearchResultOptions();

    $scope.dictionaries = {};
    $scope.selectedCategories = {};

    $scope.loadUserTrainingReservationData = function() {
        resetViolations();
        TrainingReservationService.loadUserTrainingReservationData($scope.individualUser.data);
    }

    $scope.loadDictionaries = function() {
        $scope.dictionaries.trainingCategories = DictionaryService.loadDictionary(DictionaryService.DICTIONARY.TRAINING_CATEGORIES);
    }

    $scope.reserveTrainingForAnotherUser = function() {
        TrainingReservationService.resetUserTrainingReservationData();
        UserService.resetIndividualUser();
        $scope.clear();
    }

    function resetViolations() {
        $scope.violations = TrainingReservationService.getNewViolations();
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