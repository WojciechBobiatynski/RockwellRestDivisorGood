angular.module("gryf.ti").controller("TrainingToReimburseController", ["$scope", "TrainingInstanceSearchService","DictionaryService", "ReimbursementsServiceModify","$state",
    function($scope, TrainingInstanceSearchService, DictionaryService, ReimbursementsServiceModify, $state) {
        $scope.trainingCriteria = TrainingInstanceSearchService.getNewCriteria();
        $scope.searchResultOptions = TrainingInstanceSearchService.getSearchResultOptions();
        $scope.trainingModel = TrainingInstanceSearchService.getTrainingModel();

        $scope.datepicker = {
            isStartDateFromOpened: false,
            isStartDateToOpened: false,
            isEndDateFromOpened: false,
            isEndDateToOpened: false
        };

        $scope.find = function () {
            TrainingInstanceSearchService.find();
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

}]);