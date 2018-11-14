angular.module("gryf.ti").controller("CancelTrainingReservationController", ["$scope", "TrainingInstanceSearchService", "DictionaryService", "UserService",
    function($scope, TrainingInstanceSearchService, DictionaryService, UserService) {

        $scope.trainingCriteria = TrainingInstanceSearchService.getNewCriteria();
        $scope.searchResultOptions = TrainingInstanceSearchService.getSearchResultOptions();
        $scope.trainingModel = TrainingInstanceSearchService.getTrainingModel();
        $scope.trainingStatuses = {data: null};

        $scope.setDefaultCriteria = function() {
            $scope.indUserSearchInfo = UserService.getIndividualUserSearchInfo();

            $scope.trainingCriteria.participantPesel = $scope.indUserSearchInfo.data.pesel;
            $scope.trainingCriteria.participantName = $scope.indUserSearchInfo.data.firstName;
            $scope.trainingCriteria.participantSurname = $scope.indUserSearchInfo.data.lastName;
        };

        $scope.setDefaultCriteria();

        $scope.datepicker = {
            isStartDateFromOpened: false,
            isStartDateToOpened: false,
            isEndDateFromOpened: false,
            isEndDateToOpened: false
        };

        $scope.find = function () {
            $scope.searchResultOptions = TrainingInstanceSearchService.getNewSearchResultOptions();
            $scope.trainingCriteria.limit = 10;
            TrainingInstanceSearchService.find();
        };

        $scope.getSortedBy = function(sortColumnName) {
            TrainingInstanceSearchService.findSortedBy(sortColumnName);
        };

        $scope.getSortingTypeClass = function(columnName) {
            return TrainingInstanceSearchService.getSortingTypeClass($scope.trainingCriteria, columnName);
        };

        $scope.openDatepicker = function (fieldName) {
            $scope.datepicker[fieldName] = true;
        };

        DictionaryService.loadDictionary(DictionaryService.DICTIONARY.TRAINING_INSTANCE_STATUSES).then(function(data) {
            $scope.trainingStatuses.data = data;

            DictionaryService.getRecordById(DictionaryService.DICTIONARY.TRAINING_INSTANCE_STATUSES, "RES").then(function(record) {
                $scope.trainingCriteria.trainingStatusId = record.id;
            });
        });

        $scope.isStatusDisabled = function() {
            return $scope.trainingCriteria.trainingStatusId != null;
        };

        $scope.clear = function() {
            var trainingStatusId =  $scope.trainingCriteria.trainingStatusId;
            $scope.trainingCriteria = TrainingInstanceSearchService.getNewCriteria();
            $scope.trainingCriteria.trainingStatusId = trainingStatusId;
            $scope.trainingModel = TrainingInstanceSearchService.getNewTrainingModel();
            $scope.searchResultOptions = TrainingInstanceSearchService.getNewSearchResultOptions();
        };

        $scope.loadMore = function() {
            TrainingInstanceSearchService.loadMore();
        };

        $scope.clear();

        //Po czysczeniu wszystkich
        TrainingInstanceSearchService.getGrantProgramNames().then(function(response) {
            $scope.grantPrograms = response.data;
        });

}]);