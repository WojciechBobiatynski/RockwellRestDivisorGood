angular.module("gryf.ti").controller("TrainingDetailsModalController", ["$scope", "$stateParams", "TrainingSearchService", "DictionaryService",
function($scope, $stateParams, TrainingSearchService, DictionaryService) {
    $scope.close = $scope.$close;
    $scope.training = {data: null};

    TrainingSearchService.findDetailsById($stateParams.trainingId).success(function(data) {
        DictionaryService.getRecordById(DictionaryService.DICTIONARY.TRAINING_CATEGORIES, data.category).then(function(record) {
            $scope.training.data.category = record.name;
        });
        $scope.training.data = data;
    });
}]);