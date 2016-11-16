angular.module("gryf.ti").controller("TrainingToReimburseController", ["$scope", "TrainingToReimburseService",
    function($scope, TrainingToReimburseService) {
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

        ($scope.loadTrainingStatuses = function () {
            TrainingToReimburseService.loadTrainingStatuses();
        })();
}]);