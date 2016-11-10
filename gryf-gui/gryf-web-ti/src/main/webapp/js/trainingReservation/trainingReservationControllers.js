angular.module("gryf.ti").controller("TrainingReservationController", ["$scope", "PersonDataCacheService", function($scope, PersonDataCacheService) {
    $scope.personData = PersonDataCacheService.getPesonData();
}]);