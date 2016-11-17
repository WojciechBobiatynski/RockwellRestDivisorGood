angular.module("gryf.ti").controller("TrainingInstanceDetailsModalController", ["$scope", "$stateParams", "TrainingInstanceSearchService",
function($scope, $stateParams, TrainingInstanceSearchService) {
    $scope.close = $scope.$close;
    $scope.trainingInstance = {data: null};

    TrainingInstanceSearchService.findById($stateParams.trainingInstanceId).success(function(data) {
        $scope.trainingInstance.data = data;
        alert(data);
    });
}]);