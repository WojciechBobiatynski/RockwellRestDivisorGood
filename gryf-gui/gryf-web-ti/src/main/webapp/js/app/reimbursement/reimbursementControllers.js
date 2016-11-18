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

angular.module("gryf.ti").controller("ReimbursementModifyController", ["$scope", "ReimbursementsServiceModify", "DictionaryService","$stateParams",
    function ($scope, ReimbursementsServiceModify, DictionaryService, $stateParams) {
        $scope.rmbsModel = ReimbursementsServiceModify.getRmbsModel();
        ReimbursementsServiceModify.findById($stateParams.reimbursementId);

}]);