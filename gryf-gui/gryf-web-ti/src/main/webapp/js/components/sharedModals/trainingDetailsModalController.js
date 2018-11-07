angular.module("gryf.ti").controller("TrainingDetailsModalController", ["$scope", "$stateParams", "TrainingSearchService", "DictionaryService", "TrainingReservationService",
function($scope, $stateParams, TrainingSearchService, DictionaryService, TrainingReservationService) {
    $scope.close = $scope.$close;
    $scope.training = {data: null};
    $scope.userTrainingReservationData = TrainingReservationService.getUserTrainingReservationData();
    $scope.searchDTO = TrainingSearchService.getSearchDTO();

    TrainingSearchService.findDetailsById($stateParams.trainingId)
        .success(function(data) {
            // @todo add grant program name to data
            DictionaryService.getRecordById(DictionaryService.DICTIONARY.TRAINING_CATEGORIES, data.category).then(function(record) {
                $scope.training.data.category = record.name;
            });
            $scope.training.data = data;
            $scope.training.data.grantProgramName = $scope.searchDTO.searchResultList[0].grantProgramName ;
            $scope.training.data.grantProgramId = $scope.searchDTO.searchResultList[0].grantProgramId ;
            }
        );
}]);