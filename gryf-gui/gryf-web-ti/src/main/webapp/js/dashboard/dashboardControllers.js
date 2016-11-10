angular.module("gryf.ti").controller("DashboardController", ["$scope", "DashboardService", "PersonDataCacheService", function($scope, DashboardService, PersonDataCacheService) {
    $scope.personData = PersonDataCacheService.getPesonData();
    $scope.loggedUserInfo = {};

    DashboardService.getLoggedUserInfo().then(function(response) {
        $scope.loggedUserInfo = response.data;
    });
}]);