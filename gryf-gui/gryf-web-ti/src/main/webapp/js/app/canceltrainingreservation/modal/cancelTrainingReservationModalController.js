angular.module("gryf.ti").controller("CancelTrainingReservationModalController",
    ["$scope", "$stateParams", "TrainingInstanceSearchService", "TrainingReservationService",
function($scope, $stateParams, TrainingInstanceSearchService, TrainingReservationService) {
    $scope.close = $scope.$close;
    $scope.trainingInstance = {data: null};

    TrainingInstanceSearchService.findDetailsById($stateParams.trainingInstanceId).success(function(data) {
        $scope.trainingInstance.data = data;
    });

    $scope.cancelTrainingReservation = function() {
        TrainingReservationService.cancelTrainingReservation($scope.trainingInstance.data.trainingInstanceId,
                                                            $scope.trainingInstance.data.trainingInstanceVersion).then(function() {
            TrainingInstanceSearchService.find();
            $scope.close(true);
        });;
    };
}]);