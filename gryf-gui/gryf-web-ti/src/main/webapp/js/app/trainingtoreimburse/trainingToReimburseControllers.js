angular.module("gryf.ti").controller("TrainingToReimburseController",
    ["$scope", "TrainingInstanceSearchService", "DictionaryService", "ReimbursementsServiceModify","$state", "UserService",
    function($scope, TrainingInstanceSearchService, DictionaryService, ReimbursementsServiceModify, $state, UserService) {
        $scope.trainingCriteria = TrainingInstanceSearchService.getNewCriteria();
        $scope.searchResultOptions = TrainingInstanceSearchService.getSearchResultOptions();
        $scope.trainingModel = TrainingInstanceSearchService.getTrainingModel();

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
            $scope.trainingModel.trainingStatuses = data;

            DictionaryService.getRecordById(DictionaryService.DICTIONARY.TRAINING_INSTANCE_STATUSES, "DONE").then(function(record) {
                $scope.trainingCriteria.trainingStatusId = record.id;
            });
        });

        $scope.isStatusDisabled = function(){
            return $scope.trainingCriteria.trainingStatusId != null;
        };

        $scope.createReimbursementAndNav = function(trainingInstanceId){
            ReimbursementsServiceModify.createReimbursement(trainingInstanceId).then(function (response) {
                $scope.find();
                $state.go('reimburse', {'reimbursementId' :response.data})
            });
        };

        $scope.loadMore = function() {
            TrainingInstanceSearchService.loadMore();
        };

        $scope.clear = function() {
            $scope.trainingModel = TrainingInstanceSearchService.getNewTrainingModel();
            $scope.searchResultOptions = TrainingInstanceSearchService.getNewSearchResultOptions();
        };

        $scope.clear();
}]);