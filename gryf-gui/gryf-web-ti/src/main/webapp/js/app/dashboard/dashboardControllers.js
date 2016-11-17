angular.module("gryf.ti").controller("DashboardController", ["$scope", "DashboardService", "UserService", "TrainingReservationService",
    function($scope, DashboardService, UserService, TrainingReservationService) {

    $scope.individualUser = UserService.getIndividualUser();
    $scope.loggedUserInfo = UserService.getLoggedUserInfo();

    $scope.loadUserTrainingReservationData = TrainingReservationService.loadUserTrainingReservationData;
}]);