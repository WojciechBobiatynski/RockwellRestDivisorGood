angular.module("gryf.ti").controller("TrainingReservationController", ["$scope", "UserService", "TrainingReservationService",
    function($scope, UserService, TrainingReservationService) {

    $scope.individualUser = UserService.getIndividualUser();
    $scope.userProductInstancePools = TrainingReservationService.getProductInstancePoolsOfUser();
    $scope.violations = TrainingReservationService.getNewViolations();

    $scope.loadProductInstancePoolsOfUser = function() {
        resetViolations();
        TrainingReservationService.loadProductInstancePoolsOfUser($scope.individualUser.data);
    }

    $scope.reserveTrainingForAnotherUser = function() {
        TrainingReservationService.resetProductInstancePoolsOfUser();
        UserService.resetIndividualUser();
    }

    function resetViolations() {
        $scope.violations = TrainingReservationService.getNewViolations();
    }
}]);