angular.module("gryf.ti").controller("ReservationModalController", ["$scope", "$stateParams", "GryfModals",
    "TrainingSearchService", "TrainingReservationService", "UserService",
    function($scope, $stateParams, GryfModals, TrainingSearchService, TrainingReservationService, UserService) {
    $scope.close = $scope.$close;
    $scope.patternNumberOnly = /^\d+$/;
    $scope.training = {data: null};
    $scope.toReservedNum = null;
    $scope.userTrainingReservationData = TrainingReservationService.getUserTrainingReservationData();
    $scope.violations = TrainingReservationService.getNewViolations();
    $scope.individualUser = UserService.getIndividualUser();

    TrainingSearchService.findDetailsById($stateParams.trainingId).success(function(data) {
        $scope.training.data = data;
    });

    $scope.reserveTraining = function() {
        resetViolations();

        var trainingReservationDto = {};
        trainingReservationDto.trainingId = $scope.training.data.trainingId;
        trainingReservationDto.individualId = $scope.userTrainingReservationData.data.id;
        trainingReservationDto.verificationCode = $scope.individualUser.data.verificationCode;
        if($scope.userTrainingReservationData.data.contracts[0]) {
            trainingReservationDto.contractId = $scope.userTrainingReservationData.data.contracts[0].id;
        }
        trainingReservationDto.toReservedNum = $scope.toReservedNum;
        trainingReservationDto.version = $scope.training.data.version;

        if(!trainingReservationDto.contractId) {
            GryfModals.openModal(GryfModals.MODALS_URL.ERROR_INFO,
                {message: "Nie można zarezerwować szkolenia, ponieważ ta osoba nie posiada żadnej umowy."});
            return;
        }

        TrainingReservationService.reserveTraining(trainingReservationDto).then(function() {
            $scope.close(true);
        });
    };

    function resetViolations() {
        $scope.violations = TrainingReservationService.getNewViolations();
    };
}]);