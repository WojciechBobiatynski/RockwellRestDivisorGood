angular.module("gryf.ti").controller("TrainingToReimburseController", ["$scope", "TrainingToReimburseService","DictionaryService",
    function($scope, TrainingToReimburseService, DictionaryService) {
        $scope.trainingCriteria = TrainingToReimburseService.getNewCriteria();
        $scope.searchResultOptions = TrainingToReimburseService.getSearchResultOptions();
        $scope.trainingModel = TrainingToReimburseService.getTrainingModel();

        $scope.datepicker = {
            isStartDateFromOpened: false,
            isStartDateToOpened: false,
            isEndDateFromOpened: false,
            isEndDateToOpened: false
        };

        $scope.find = function () {
            TrainingToReimburseService.find();
        };

        $scope.openDatepicker = function (fieldName) {
            $scope.datepicker[fieldName] = true;
        };

        $scope.trainingModel.trainingStatuses = DictionaryService.loadDictionary(DictionaryService.DICTIONARY.TRAINING_TO_REIMBURSE_STATUSES, setStatus);

        function setStatus(){
            angular.forEach($scope.trainingModel.trainingStatuses, function(status){
                if(status.id === 'DONE'){
                    $scope.trainingCriteria.trainingStatusId = status.id;
                }
            });
        };

        $scope.isStatusDisabled = function(){
            return $scope.trainingCriteria.trainingStatusId != null;
        };

}]);