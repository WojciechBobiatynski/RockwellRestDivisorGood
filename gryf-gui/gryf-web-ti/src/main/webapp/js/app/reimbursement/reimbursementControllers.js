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
            $scope.searchResultOptions = ReimbursementsService.getNewSearchResultOptions();
            $scope.elctRmbsCriteria.limit = 10;
            ReimbursementsService.find();
        };

        $scope.openDatepicker = function (fieldName) {
            $scope.datepicker[fieldName] = true;
        };

        DictionaryService.loadDictionary(DictionaryService.DICTIONARY.REIMBURSEMENT_STATUSES).then(function(data) {
            $scope.elctRmbsModel.rmbsStatuses = data;
        });

        $scope.getSortedBy = function(sortColumnName) {
            ReimbursementsService.findSortedBy(sortColumnName);
        };

        $scope.getSortingTypeClass = function(columnName) {
            return ReimbursementsService.getSortingTypeClass($scope.elctRmbsCriteria, columnName);
        };

        $scope.loadMore = function() {
            ReimbursementsService.loadMore();
        };

        $scope.clear = function() {
            $scope.elctRmbsCriteria = ReimbursementsService.getNewCriteria();
            $scope.searchResultOptions = ReimbursementsService.getNewSearchResultOptions();
            $scope.elctRmbsModel = ReimbursementsService.getNewElctRmbsModel();
        };

        $scope.clear();
}]);

var test;
angular.module("gryf.ti").controller("ReimbursementModifyController", ["$scope", "ReimbursementsServiceModify", "DictionaryService","$stateParams","TrainingInstanceSearchService",
    function ($scope, ReimbursementsServiceModify, DictionaryService, $stateParams, TrainingInstanceSearchService) {
        $scope.rmbsModel = ReimbursementsServiceModify.getRmbsModel();
        $scope.violations = ReimbursementsServiceModify.getNewViolations();
        test = $scope;

        TrainingInstanceSearchService.findDetailsById($stateParams.trainingInstanceId).success(function(data) {
            $scope.rmbsModel.trainingInstance = data;
        });

        ReimbursementsServiceModify.createReimbursement($stateParams.trainingInstanceId).success(function(data) {
            $scope.rmbsModel.model = data;
        });

        $scope.saveReimburse = function(){
            $scope.violations = ReimbursementsServiceModify.getNewViolations();
            ReimbursementsServiceModify.saveReimburse();
        };

        $scope.sendToReimburse = function(){
            $scope.violations = ReimbursementsServiceModify.getNewViolations();
            ReimbursementsServiceModify.sendToReimburse();
        };

}]);