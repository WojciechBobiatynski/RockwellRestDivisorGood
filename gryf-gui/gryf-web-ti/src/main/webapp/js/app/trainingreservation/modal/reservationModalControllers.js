angular.module("gryf.ti").controller("ReservationModalController", ["$scope", "$stateParams", "TrainingSearchService", "TrainingReservationService",
    function($scope, $stateParams, TrainingSearchService, TrainingReservationService) {
    $scope.close = $scope.$close;
    $scope.training = {data: null};
    $scope.toReservedNum = null;
    $scope.userTrainingReservationData = TrainingReservationService.getUserTrainingReservationData();
    $scope.violations = TrainingReservationService.getNewViolations();

    TrainingSearchService.findDetailsById($stateParams.trainingId).success(function(data) {
        $scope.training.data = data;
    });

    $scope.reserveTraining = function() {
        resetViolations();

        var trainingReservationDto = {};
        trainingReservationDto.trainingId = $scope.training.data.trainingId;
        trainingReservationDto.individualId = $scope.userTrainingReservationData.data.id;
        trainingReservationDto.contractId = $scope.userTrainingReservationData.data.contracts[0].id;
        trainingReservationDto.toReservedNum = $scope.toReservedNum;

        TrainingReservationService.reserveTraining(trainingReservationDto).then(function() {
            $scope.close(true);
        });
    };

    function resetViolations() {
        $scope.violations = TrainingReservationService.getNewViolations();
    };
}]);