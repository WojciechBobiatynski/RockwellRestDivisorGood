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

        $scope.correctionObject = AnnounceEReimbursementService.getCorrectionObject();
        $scope.eReimbObject = AnnounceEReimbursementService.getNewModel();
        $scope.violations = AnnounceEReimbursementService.getNewViolations();

        $scope.test = 123123;

            $scope.corrections = [
                {expiryDate: '2014-05-12', correctionDate: '2014-02-15', reason: 'Dzięki za poprzednią poprawkę, ale to jeszcze nie koniec. Zapomniałeś o tym i o tym.'},
                {expiryDate: '2018-11-04', correctionDate: '2016-12-01', reason: 'Proszę o dodanie brakujących załączników'},
                {expiryDate: '2017-10-12', correctionDate: '2017-14-04', reason: 'Zapomniała Pani o potwierdzeniu przelewu'},
                {expiryDate: '2016-02-23', correctionDate: '2016-01-12', reason: 'Cześć Pszemek, pracuję w Sodexo!'},
                {expiryDate: '2016-04-06', correctionDate: '2016-02-14', reason: 'Bo tak.'}];

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

        $scope.getNewRequiredCorrectionDate = AnnounceEReimbursementService.getNewRequiredCorrectionDate;
        $scope.sendToCorrect = AnnounceEReimbursementService.sendToCorrect;

        $scope.getPrevUrl = gryfSessionStorage.getUrlFromSessionStorage();
    }]);