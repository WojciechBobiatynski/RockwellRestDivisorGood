angular.module("gryf.ti").controller("ReservationModalController", ["$scope", "$stateParams", "GryfModals",
    "TrainingSearchService", "TrainingReservationService", "UserService", "DictionaryService",
    function($scope, $stateParams, GryfModals, TrainingSearchService, TrainingReservationService, UserService, DictionaryService) {
    $scope.close = $scope.$close;
    $scope.patternNumberOnly = /^\d+$/;
    $scope.training = {data: null};
    $scope.toReservedNum = null;
    $scope.userTrainingReservationData = TrainingReservationService.getUserTrainingReservationData();
    $scope.violations = TrainingReservationService.getNewViolations();
    $scope.individualUser = UserService.getIndividualUser();

    TrainingSearchService.findPrecalculatedDetailsById($stateParams.trainingId, $stateParams.grantProgramId).success(function(data) {
        //@ todo add grant program name to data
        $scope.training.data = data;
        console.log(data)
        DictionaryService.getRecordById(DictionaryService.DICTIONARY.TRAINING_CATEGORIES, data.category).then(function(record) {
            $scope.training.data.category = record.name;
        });
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

        TrainingReservationService.reserveTraining(trainingReservationDto).then(function() {
            $scope.close(true);
        });
    };

    function resetViolations() {
        $scope.violations = TrainingReservationService.getNewViolations();
    }
}]);