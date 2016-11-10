angular.module("gryf.ti").controller("DashboardController", ["$scope", "DashboardService", "PersonDataCacheService", function($scope, DashboardService, PersonDataCacheService) {
    $scope.personData = PersonDataCacheService.getPesonData();
}]);