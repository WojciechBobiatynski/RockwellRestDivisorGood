angular.module("gryf.ti").controller("ReimbursementsController", ["$scope", "ReimbursementsService", "DictionaryService",
    function ($scope, ReimbursementsService, DictionaryService) {
        $scope.elctRmbsCriteria = ReimbursementsService.getNewCriteria();
        $scope.searchResultOptions = ReimbursementsService.getSearchResultOptions();
        $scope.elctRmbsModel = ReimbursementsService.getElctRmbsModel();

        $scope.datepicker = {
            isRmbsDateFromOpened: false,
            isRmbsDateToOpened: false
        };

        $scope.find = function () {
            ReimbursementsService.find();
        };

        $scope.openDatepicker = function (fieldName) {
            $scope.datepicker[fieldName] = true;
        };

        $scope.elctRmbsModel.rmbsStatuses = DictionaryService.loadDictionary(DictionaryService.DICTIONARY.REIMBURSEMENT_STATUSES);

}]);

var test;
angular.module("gryf.ti").controller("ReimbursementModifyController", ["$scope", "ReimbursementsServiceModify", "DictionaryService","$stateParams","TrainingInstanceSearchService",
    function ($scope, ReimbursementsServiceModify, DictionaryService, $stateParams, TrainingInstanceSearchService) {
        $scope.rmbsModel = ReimbursementsServiceModify.getRmbsModel();
        test = $scope;

        ReimbursementsServiceModify.findById($stateParams.reimbursementId).success(function(data) {
            TrainingInstanceSearchService.findDetailsById(data.trainingInstanceId).success(function(data) {
                $scope.rmbsModel.trainingInstance = data;
            });
        });

        $scope.isDataLoaded = function () {
            $scope.rmbsModel.model != null;
        }
}]);