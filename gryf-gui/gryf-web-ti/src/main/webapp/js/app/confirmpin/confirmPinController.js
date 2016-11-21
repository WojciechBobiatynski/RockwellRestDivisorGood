angular.module("gryf.ti").controller("ConfirmPinController", ["$scope", "TrainingInstanceSearchService", "DictionaryService",
    function($scope, TrainingInstanceSearchService, DictionaryService) {
        $scope.trainingCriteria = TrainingInstanceSearchService.getNewCriteria();
        $scope.searchResultOptions = TrainingInstanceSearchService.getSearchResultOptions();
        $scope.trainingModel = TrainingInstanceSearchService.getTrainingModel();
        $scope.trainingStatuses = {data: null};

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

        $scope.trainingStatuses.data = DictionaryService.loadDictionary(DictionaryService.DICTIONARY.TRAINING_INSTANCE_STATUSES, setStatus);

        function setStatus(data) {
            if(data) {
                $scope.trainingStatuses.data = data;
            }
            angular.forEach($scope.trainingStatuses.data, function(status) {
                if(status.id === 'RES') {
                    $scope.trainingCriteria.trainingStatusId = status.id;
                }
            });
        }

        $scope.isStatusDisabled = function() {
            return $scope.trainingCriteria.trainingStatusId != null;
        };

        $scope.clear = function() {
            $scope.trainingModel = TrainingInstanceSearchService.getNewTrainingModel();
        };

        $scope.clear();

}]);