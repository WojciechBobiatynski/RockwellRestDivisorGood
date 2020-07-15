angular.module("gryf.ti").controller("ReservationModalController", ["$scope", "$stateParams", "GryfModals",
    "TrainingSearchService", "TrainingReservationService", "UserService", "DictionaryService",
    function($scope, $stateParams, GryfModals, TrainingSearchService, TrainingReservationService, UserService, DictionaryService) {
    $scope.close = $scope.$close;
    $scope.patternNumberOnly = /^\d+$/;
    $scope.training = {data: null};
    $scope.toReservedNum = null;
    $scope.registerDate = {data: null};
    $scope.registerDateOpened = false;
    $scope.userTrainingReservationData = TrainingReservationService.getUserTrainingReservationData();
    $scope.violations = TrainingReservationService.getNewViolations();
    $scope.individualUser = UserService.getIndividualUser();
    $scope.loggedUserInfo = UserService.getLoggedUserInfo();

    TrainingSearchService.findPrecalculatedDetailsById($stateParams.trainingId, $stateParams.grantProgramId).success(function(data) {
        //@ todo add grant program name to data
        $scope.training.data = data;
        console.log(data);
        console.log($stateParams)
        DictionaryService.getRecordById(DictionaryService.DICTIONARY.TRAINING_CATEGORIES, data.category).then(function(record) {
            $scope.training.data.category = record.name;
        });
    });

    $scope.openRegisterDateDatepicker = function () {
        $scope.registerDateOpened = true;
    };

    $scope.reserveTraining = function() {
        resetViolations();

        var trainingReservationDto = {};
        trainingReservationDto.trainingId = $scope.training.data.trainingId;
        trainingReservationDto.individualId = $scope.userTrainingReservationData.data.id;
        trainingReservationDto.verificationCode = $scope.individualUser.data.verificationCode;
        trainingReservationDto.grantProgramId = $scope.training.data.grantProgramId;
        trainingReservationDto.grantProgramName = $scope.training.data.grantProgramName;
        trainingReservationDto.startDate    = $scope.training.data.startDate;
        trainingReservationDto.endDate    = $scope.training.data.endDate;

        trainingReservationDto.registerDate = $scope.registerDate.data;
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