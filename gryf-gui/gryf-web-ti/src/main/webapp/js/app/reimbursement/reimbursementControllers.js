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

        $scope.isInState = function(foundRmbs, stateCode) {
            return foundRmbs.rmbsStatusId != null && foundRmbs.rmbsStatusId === stateCode;
        }
}]);

angular.module("gryf.ti").controller("ReimbursementModifyController", ["$scope", "$state", "ReimbursementsServiceModify", "DictionaryService","$stateParams","TrainingInstanceSearchService",
    function ($scope, $state, ReimbursementsServiceModify, DictionaryService, $stateParams, TrainingInstanceSearchService) {
        $scope.rmbsModel = ReimbursementsServiceModify.getNewRmbsModel();
        $scope.violations = ReimbursementsServiceModify.getNewViolations();

        $scope.sendButtonText = "Wyślij do rozliczenia";

        ReimbursementsServiceModify.createReimbursement($stateParams.trainingInstanceId);

        TrainingInstanceSearchService.findDetailsById($stateParams.trainingInstanceId).success(function(data) {
            console.log('abcd');
            $scope.rmbsModel.trainingInstance = data;
        });

        $scope.save = function(){
            $scope.violations = ReimbursementsServiceModify.getNewViolations();
            ReimbursementsServiceModify.saveReimburse();
        };

        $scope.send = function(){
            $scope.violations = ReimbursementsServiceModify.getNewViolations();
            ReimbursementsServiceModify.sendToReimburse();
        };

        $scope.isDisabled = function(){
            return ($scope.rmbsModel.model != null && $scope.rmbsModel.model.statusCode === 'T_RMS') || $state.params.isDisabled;
        };

        $scope.correctionVisible = function(){
            return !!$scope.rmbsModel.model
                && !!$scope.rmbsModel.model.lastCorrectionDto
                && !$scope.rmbsModel.model.lastCorrectionDto.complementDate;
        };

}]);

angular.module("gryf.ti").controller("ReimburseDetailsController", ["$scope", "ReimbursementsServiceModify", "$state" ,"$stateParams","TrainingInstanceSearchService",
    function ($scope, ReimbursementsServiceModify, $state, $stateParams, TrainingInstanceSearchService) {
        $scope.rmbsModel = ReimbursementsServiceModify.getRmbsModel();
        $scope.violations = ReimbursementsServiceModify.getNewViolations();

        ReimbursementsServiceModify.findById($stateParams.reimbursementId).success(function(data) {
            $scope.rmbsModel.model = data;
            TrainingInstanceSearchService.findDetailsById($scope.rmbsModel.model.trainingInstanceId).success(function(data) {
                $scope.rmbsModel.trainingInstance = data;
            });
        });

        $scope.correctionVisible = function(){
            return !!$scope.rmbsModel.model
                && !!$scope.rmbsModel.model.lastCorrectionDto
                && !$scope.rmbsModel.model.lastCorrectionDto.complementDate;
        };

        $scope.isDisabled = function(){
            return $state.params.isDisabled;
        };

    }]);

angular.module("gryf.ti").controller("CorrectionController", ["$scope", "ReimbursementsServiceModify", "DictionaryService","$stateParams","TrainingInstanceSearchService",
    function ($scope, ReimbursementsServiceModify, DictionaryService, $stateParams, TrainingInstanceSearchService) {
        $scope.rmbsModel = ReimbursementsServiceModify.getRmbsModel();
        $scope.violations = ReimbursementsServiceModify.getNewViolations();

        $scope.sendButtonText = "Wyślij korektę";

        ReimbursementsServiceModify.findById($stateParams.reimbursementId).success(function(data) {
            $scope.rmbsModel.model = data;
            TrainingInstanceSearchService.findDetailsById($scope.rmbsModel.model.trainingInstanceId).success(function(data) {
                $scope.rmbsModel.trainingInstance = data;
            });
        });

        $scope.save = function(){
            $scope.violations = ReimbursementsServiceModify.getNewViolations();
            ReimbursementsServiceModify.saveCorrection();
        };

        $scope.send = function(){
            $scope.violations = ReimbursementsServiceModify.getNewViolations();
            ReimbursementsServiceModify.sendCorrection();
        };

        $scope.correctionVisible = function(){
            return !!$scope.rmbsModel.model
                && !!$scope.rmbsModel.model.lastCorrectionDto
                && !$scope.rmbsModel.model.lastCorrectionDto.complementDate;
        };

    }]);