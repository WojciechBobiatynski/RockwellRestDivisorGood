angular.module("gryf.ti").controller("TrainingReservationController",
    ["$scope", "$state", "GryfModals", "UserService", "TrainingReservationService", "DictionaryService", "TrainingSearchService",
    function($scope, $state, GryfModals, UserService, TrainingReservationService, DictionaryService, TrainingSearchService) {

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
    };

    DictionaryService.loadDictionary(DictionaryService.DICTIONARY.TRAINING_CATEGORIES).then(function(data) {
        $scope.dictionaries.trainingCategories = data;
    });

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
        $scope.searchDTO.entity.limit = 10;
        TrainingSearchService.findToReserve();
    };

    $scope.getSortedBy = function(sortColumnName) {
        TrainingSearchService.findToReserveSortedBy(sortColumnName);
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
        TrainingSearchService.loadMoreToReserve();
    };

    $scope.reserveTraining = function(item) {
        if(!$scope.userTrainingReservationData.data.contracts[0]) {
            GryfModals.openModal(GryfModals.MODALS_URL.ERROR_INFO,
                {message: "Nie można zarezerwować usługi, ponieważ ta osoba nie posiada żadnej umowy."});
            return;
        }

        $state.go("reservationModal", {
            "trainingId": item.trainingId,
            "grantProgramId": $scope.userTrainingReservationData.data.contracts[0].grantProgram.id
        });
    };

    $scope.clear();
}]);