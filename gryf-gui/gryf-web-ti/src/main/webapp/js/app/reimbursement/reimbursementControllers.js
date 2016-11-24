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

        DictionaryService.loadDictionary(DictionaryService.DICTIONARY.REIMBURSEMENT_STATUSES).then(function(data) {
            $scope.elctRmbsModel.rmbsStatuses = data;
        });

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

        $scope.save = function(){
            ReimbursementsServiceModify.save();
        };

        $scope.sendToReimburse = function(){
            ReimbursementsServiceModify.sendToReimburse();
        };

}]);