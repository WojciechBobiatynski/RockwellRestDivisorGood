"use strict";

angular.module('gryf.electronicreimbursements').controller("searchform.electronicReimbursementsController",
    ['$scope', 'electronicReimbursementSearchService', function ($scope, electronicReimbursementSearchService) {
        $scope.elctRmbsCriteria = electronicReimbursementSearchService.getNewCriteria();
        $scope.searchResultOptions = electronicReimbursementSearchService.getSearchResultOptions();
        $scope.elctRmbsModel = electronicReimbursementSearchService.getElctRmbsModel();

        gryfSessionStorage.setUrlToSessionStorage();

        $scope.datepicker = {
            reimbursementDateFromOpened: false,
            reimbursementDateToOpened: false
        };

        $scope.find = function () {
            electronicReimbursementSearchService.find();
        };

        $scope.clear = function() {
            $scope.elctRmbsCriteria = electronicReimbursementSearchService.getNewCriteria();
            $scope.searchResultOptions = electronicReimbursementSearchService.getNewSearchResultOptions();
        };

        $scope.openDatepicker = function (fieldName) {
            $scope.datepicker[fieldName] = true;
        };

        ($scope.loadReimbursementsStatuses = function () {
            electronicReimbursementSearchService.loadReimbursementsStatuses();
        })();

    }]);


angular.module('gryf.electronicreimbursements').controller("announce.electronicReimbursementsController",
    ['$scope', "$routeParams", "GryfModulesUrlProvider", "BrowseTrainingInsService", "TrainingInstanceSearchService", "AnnounceEReimbursementService",
        function ($scope, $routeParams, GryfModulesUrlProvider, BrowseTrainingInsService, TrainingInstanceSearchService, AnnounceEReimbursementService) {

        $scope.MODULES = GryfModulesUrlProvider.MODULES;
        $scope.getUrlFor = GryfModulesUrlProvider.getUrlFor;

        $scope.eReimbObject = AnnounceEReimbursementService.getNewModel();
        $scope.violations = AnnounceEReimbursementService.getNewViolations();

        $scope.test = 123123;

        if($routeParams.id) {
            AnnounceEReimbursementService.findById($routeParams.id).then(function() {
                TrainingInstanceSearchService.findDetailsById($scope.eReimbObject.entity.trainingInstanceId).success(function(data) {
                    $scope.eReimbObject.trainingInstance = data;
                });
                BrowseTrainingInsService.findById($scope.eReimbObject.entity.trainingInstitutionId).success(function(data) {
                    $scope.eReimbObject.trainingInstitution = data[0];
                });
            });
        }

        $scope.toCorrect = AnnounceEReimbursementService.toCorrect;

        $scope.getPrevUrl = function() {
            return gryfSessionStorage.getUrlFromSessionStorage();
        };
    }]);