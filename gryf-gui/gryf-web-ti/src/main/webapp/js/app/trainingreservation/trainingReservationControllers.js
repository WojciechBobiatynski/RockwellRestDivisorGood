angular.module("gryf.ti").controller("TrainingReservationController",
    ["$scope", "$state", "GryfModals", "UserService", "TrainingReservationService", "DictionaryService", "TrainingSearchService",
    function($scope, $state, GryfModals, UserService, TrainingReservationService, DictionaryService, TrainingSearchService) {

    $scope.individualUser = UserService.getIndividualUser();
    $scope.userTrainingReservationData = TrainingReservationService.getUserTrainingReservationData();
    console.log('userTrainingReservationData ', $scope.userTrainingReservationData )
    $scope.violations = TrainingReservationService.getNewViolations();
    $scope.searchDTO = TrainingSearchService.getSearchDTO();
    console.log('searchDTO ', $scope.searchDTO )
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
        if(!$scope.userTrainingReservationData.data.contracts[0]) {
            GryfModals.openModal(GryfModals.MODALS_URL.ERROR_INFO,
                {message: "Nie można zarezerwować usługi, ponieważ ta osoba nie posiada żadnej umowy."});
            return;
        }

        $scope.searchResultOptions = TrainingSearchService.getNewSearchResultOptions();
        $scope.searchDTO.entity.limit = 10;
        TrainingSearchService.findToReserve($scope.userTrainingReservationData.data.contracts[0].grantProgram.id);
    };

    $scope.getSortedBy = function(sortColumnName) {
        if(!$scope.userTrainingReservationData.data.contracts[0]) {
            GryfModals.openModal(GryfModals.MODALS_URL.ERROR_INFO,
                {message: "Nie można zarezerwować usługi, ponieważ ta osoba nie posiada żadnej umowy."});
            return;
        }

        TrainingSearchService.findToReserveSortedBy($scope.userTrainingReservationData.data.contracts[0].grantProgram.id, sortColumnName);
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
        if(!$scope.userTrainingReservationData.data.contracts[0]) {
            GryfModals.openModal(GryfModals.MODALS_URL.ERROR_INFO,
                {message: "Nie można zarezerwować usługi, ponieważ ta osoba nie posiada żadnej umowy."});
            return;
        }

        TrainingSearchService.loadMoreToReserve($scope.userTrainingReservationData.data.contracts[0].grantProgram.id);
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