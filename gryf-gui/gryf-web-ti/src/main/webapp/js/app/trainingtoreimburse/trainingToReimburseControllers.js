angular.module("gryf.ti").controller("TrainingToReimburseController", ["$scope", "TrainingInstanceSearchService","DictionaryService",
    function($scope, TrainingInstanceSearchService, DictionaryService) {
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

        $scope.trainingModel.trainingStatuses = DictionaryService.loadDictionary(DictionaryService.DICTIONARY.TRAINING_INSTANCE_STATUSES, setStatus);

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