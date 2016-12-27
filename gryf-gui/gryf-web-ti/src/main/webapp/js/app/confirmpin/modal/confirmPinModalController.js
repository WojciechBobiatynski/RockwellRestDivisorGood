angular.module("gryf.ti").controller("ConfirmPinModalController",
    ["$scope", "$stateParams", "TrainingInstanceSearchService", "TrainingReservationService",
function($scope, $stateParams, TrainingInstanceSearchService, TrainingReservationService) {
    $scope.close = $scope.$close;
    $scope.trainingInstance = {data: null};
    $scope.pinCode = null;

    TrainingInstanceSearchService.findDetailsById($stateParams.trainingInstanceId).success(function(data) {
        $scope.trainingInstance.data = data;
    });

    $scope.confirmPin = function() {
        TrainingReservationService.confirmPin($scope.trainingInstance.data.trainingInstanceId, $scope.pinCode,
                                              $scope.trainingInstance.data.trainingInstanceVersion
        ).then(function() {
            TrainingInstanceSearchService.find();
            $scope.close(true);
        });
    };
}]);