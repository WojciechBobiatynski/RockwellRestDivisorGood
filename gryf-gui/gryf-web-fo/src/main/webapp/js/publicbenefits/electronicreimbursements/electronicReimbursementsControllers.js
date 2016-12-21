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
        $scope.correctionObject = AnnounceEReimbursementService.getCorrectionObject();
        $scope.eReimbObject = AnnounceEReimbursementService.getNewModel();
        $scope.violations = AnnounceEReimbursementService.getNewViolations();

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
        $scope.findAllCorrections = AnnounceEReimbursementService.findAllCorrections;

        $scope.getPrevUrl = gryfSessionStorage.getUrlFromSessionStorage;
        $scope.getUrlFor = GryfModulesUrlProvider.getUrlFor;

        $scope.getDownloadCorrAttachmentLink = function(attachment) {
            return attachment.id != null ? contextPath + "/rest/publicBenefits/electronic/reimbursements/downloadCorrAttachment?id=" + attachment.id : '';
        };

        $scope.getReportFile = function(attachment) {
            return attachment.id != null ? contextPath + "/rest/publicBenefits/electronic/reimbursements/downloadReportFile?id=" + attachment.id : '';
        };

        $scope.reimburseButtonVisible = function(){
            return $scope.eReimbObject.entity != null && ($scope.eReimbObject.entity.statusCode === 'NEW' || $scope.eReimbObject.entity.statusCode === 'T_CRR');
        };

        $scope.correctButtonVisible = function(){
            return $scope.eReimbObject.entity != null && $scope.eReimbObject.entity.statusCode === 'T_RMS';
        };

        $scope.generateDocumentsButtonVisible = function(){
            return $scope.eReimbObject.entity != null && $scope.eReimbObject.entity.statusCode === 'T_RMS';
        };

        $scope.printReportsButtonVisible = function(){
            return $scope.eReimbObject.entity != null && $scope.eReimbObject.entity.statusCode === 'G_DOC';
        };

        $scope.confirmButtonVisible = function(){
            return $scope.eReimbObject.entity != null && $scope.eReimbObject.entity.statusCode === 'T_VRF';
        };

        $scope.cancelButtonVisible = function(){
            return $scope.eReimbObject.entity != null && ($scope.eReimbObject.entity.statusCode === 'NEW' || $scope.eReimbObject.entity.statusCode === 'T_RMS' || $scope.eReimbObject.entity.statusCode === 'T_CRR');
        };

        $scope.settle = function(){

        };

        $scope.sendToCorrect = AnnounceEReimbursementService.sendToCorrect;

        $scope.createDocuments = AnnounceEReimbursementService.createDocuments;

        $scope.printReports = AnnounceEReimbursementService.printReports;

        $scope.confirm = AnnounceEReimbursementService.confirm;

        $scope.cancel = AnnounceEReimbursementService.cancel;

        $scope.generatedReportsAndEmailsSectionVisible = function () {
            return $scope.eReimbObject.entity != null && ($scope.eReimbObject.entity.statusCode === 'T_VRF' || $scope.eReimbObject.entity.statusCode === 'REIMB');
        }

    }]);