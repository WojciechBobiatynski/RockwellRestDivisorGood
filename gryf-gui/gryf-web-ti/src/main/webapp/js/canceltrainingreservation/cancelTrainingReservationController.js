angular.module("gryf.ti").controller("CancelTrainingReservationController", ["$scope", "CancelTrainingReservationService","DictionaryService",
    function($scope, CancelTrainingReservationService, DictionaryService) {
        $scope.trainingCriteria = CancelTrainingReservationService.getNewCriteria();
        $scope.searchResultOptions = CancelTrainingReservationService.getSearchResultOptions();
        $scope.trainingModel = CancelTrainingReservationService.getTrainingModel();

        $scope.datepicker = {
            isStartDateFromOpened: false,
            isStartDateToOpened: false,
            isEndDateFromOpened: false,
            isEndDateToOpened: false
        };

        $scope.find = function () {
            CancelTrainingReservationService.find();
        };

        $scope.openDatepicker = function (fieldName) {
            $scope.datepicker[fieldName] = true;
        };

        $scope.trainingModel.trainingStatuses = DictionaryService.loadDictionary(DictionaryService.DICTIONARY.TRAINING_TO_REIMBURSE_STATUSES, setStatus);

        function setStatus(){
            angular.forEach($scope.trainingModel.trainingStatuses, function(status){
                if(status.id === 'RES'){
                    $scope.trainingCriteria.trainingStatusId = status.id;
                }
            });
        };

        $scope.isStatusDisabled = function(){
            return $scope.trainingCriteria.trainingStatusId != null;
        };

}]);