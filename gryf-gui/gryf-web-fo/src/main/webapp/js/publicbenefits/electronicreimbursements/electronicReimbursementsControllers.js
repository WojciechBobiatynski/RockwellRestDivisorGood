"use strict";
//TODO: wydzielić kontroler, po którym podziedziczy reszta z metodami wpsólnymi dla wszystkich kontrolerów
angular.module('gryf.electronicreimbursements').controller("searchform.electronicReimbursementsController",
    ['$scope', 'electronicReimbursementSearchService', function ($scope, electronicReimbursementSearchService) {
        $scope.elctRmbsCriteria = electronicReimbursementSearchService.getNewCriteria();
        $scope.searchResultOptions = electronicReimbursementSearchService.getSearchResultOptions();
        $scope.elctRmbsModel = electronicReimbursementSearchService.getElctRmbsModel();
        $scope.dictionaries = electronicReimbursementSearchService.getDictionaries();

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
            $scope.elctRmbsModel = electronicReimbursementSearchService.getNewElctRmbsModel();
        };


        $scope.getSortedBy = function(sortColumnName) {
            $scope.searchResultOptions.badQuery = false;
            electronicReimbursementSearchService.findSortedBy(sortColumnName);
        };

        $scope.getSortingTypeClass = function(columnName) {
            return electronicReimbursementSearchService.getSortingTypeClass($scope.elctRmbsCriteria, columnName);
        };

        $scope.loadMore = function() {
            electronicReimbursementSearchService.loadMore();
        };

        $scope.openDatepicker = function (fieldName) {
            $scope.datepicker[fieldName] = true;
        };

        $scope.getEreimbursementDetailsLink = function (item) {
            var detailsLink = new String();
            if(item.rmbsTypeCode === 'TI_INST'){
                detailsLink = '#/announce/'
            } else {
                detailsLink = '#/unrsv/'
            }
            detailsLink += item.rmbsNumber;
            return detailsLink;
        };

    }]);

angular.module('gryf.electronicreimbursements').controller("announce.electronicReimbursementsController",
    ['$scope', "$routeParams", "GryfModulesUrlProvider", "BrowseTrainingInsService",
        "TrainingInstanceSearchService", "AnnounceEReimbursementService", "GryfModals",
    function ($scope, $routeParams, GryfModulesUrlProvider, BrowseTrainingInsService,
          TrainingInstanceSearchService, AnnounceEReimbursementService, GryfModals) {

        //TODO: przenieść później wartości do bazy
        $scope.rejectionReasons = [
            {
                id: 1,
                name: "Przekroczono termin rozliczenia"
            },
            {
                id: 2,
                name: "Niespełnione wymogi formalne"
            },
            {
                id: 3,
                name: "Inne"
            }
        ];

        $scope.MODULES = GryfModulesUrlProvider.MODULES;
        $scope.correctionObject = AnnounceEReimbursementService.getCorrectionObject();
        $scope.eReimbObject = AnnounceEReimbursementService.getNewModel();
        $scope.violations = AnnounceEReimbursementService.getNewViolations();

        $scope.goBack = function() {
            var callback = function() {
                window.location = GryfModulesUrlProvider.getBackUrl(GryfModulesUrlProvider.MODULES.ElectronicReimbursement);
            };
            $scope.showAcceptModal("Wywołując tę akcję stracisz niezapisane dane", callback);
        };

        $scope.showAcceptModal = function(messageText, callback) {
            var message = {message: messageText};
            GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM, message).result.then(function(result) {
                if (!result) {
                    return;
                }
                callback();
            });
        };

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

        $scope.rejectButtonVisible = function(){
            return $scope.eReimbObject.entity != null && $scope.eReimbObject.entity.statusCode === 'T_RMS';
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

        $scope.sendToCorrect = function() {
            $scope.showAcceptModal("Rozliczenie zostanie wysłane do korekty.", AnnounceEReimbursementService.sendToCorrect);
        };

        $scope.createDocuments = function() {
            $scope.showAcceptModal("Zostaną wygenerowane dokumenty rozliczenia.", AnnounceEReimbursementService.createDocuments);
        };

        $scope.cancel = function() {
            $scope.showAcceptModal("Rozliczenie zostanie anulowane.", AnnounceEReimbursementService.cancel);
        };

        $scope.printReports = function() {
            $scope.showAcceptModal("Wydrukowane zostaną raporty.", AnnounceEReimbursementService.printReports);
        };

        $scope.confirm = function() {
            var callback = function() {
                AnnounceEReimbursementService.confirm().success(function(response) {
                    $scope.generateMailFromTemplatesOnInitIfNull();
                });
            };
            $scope.showAcceptModal("Rozliczenie zostanie zatwierdzone.", callback);
        };

        $scope.reject = function() {
            $scope.showAcceptModal("Rozliczenie zostanie odrzucone", AnnounceEReimbursementService.reject);
        };

        $scope.generateMailFromTemplatesOnInitIfNull = function () {
            if($scope.eReimbObject.entity.emails == null || $scope.eReimbObject.entity.emails === undefined || $scope.eReimbObject.entity.emails.length < $scope.getEmailsFromTemplateNum()){
                AnnounceEReimbursementService.createEmailsFromTemplate();
            }
        };

        $scope.getEmailsFromTemplateNum = function(){
            var mailsFromTemplate = 1;
            if($scope.eReimbObject.entity.sxoIndAmountDueTotal > 0){
                mailsFromTemplate++
            }
            return mailsFromTemplate;
        };

        $scope.generatedReportsAndEmailsSectionVisible = function () {
            return $scope.eReimbObject.entity != null && ($scope.eReimbObject.entity.statusCode === 'T_VRF' || $scope.eReimbObject.entity.statusCode === 'REIMB');
        };

        $scope.deleteMailAtt = function(parent, child) {
            var index = parent.attachments.indexOf(child);
            parent.attachments.splice(index, 1);
        };

        $scope.addNewAttToMail = function(mail) {
            mail.attachments.push({});
        };

        $scope.sendMail = function (mail) {
            AnnounceEReimbursementService.sendMail(mail);
        };

        $scope.getSaveAttUrl = function () {
            return AnnounceEReimbursementService.getSaveAttUrl();
        };

    }]);

angular.module('gryf.electronicreimbursements').controller("unrsv.electronicReimbursementsController",
    ['$scope', "$routeParams", "GryfModulesUrlProvider", "UnreservedPoolService",
        function ($scope, $routeParams, GryfModulesUrlProvider, UnreservedPoolService) {
            $scope.MODULES = GryfModulesUrlProvider.MODULES;
            $scope.unReimbObject = UnreservedPoolService.getNewModel();
            if($routeParams.id) {
                UnreservedPoolService.findById($routeParams.id);
            }

            //TODO: ujednolicić, tak jak cały ekran dla rozliczeń niewykorzystanej puli bonów
            $scope.reimburseButtonVisible = function(){
                return $scope.unReimbObject.entity != null && ($scope.unReimbObject.entity.statusCode === 'NEW' || $scope.unReimbObject.entity.statusCode === 'T_CRR');
            };

            $scope.correctButtonVisible = function(){
                return $scope.unReimbObject.entity != null && $scope.unReimbObject.entity.statusCode === 'T_RMS';
            };

            $scope.generateDocumentsButtonVisible = function(){
                return $scope.unReimbObject.entity != null && $scope.unReimbObject.entity.statusCode === 'T_RMS';
            };

            $scope.printReportsButtonVisible = function(){
                return $scope.unReimbObject.entity != null && $scope.unReimbObject.entity.statusCode === 'G_DOC';
            };

            $scope.confirmButtonVisible = function(){
                return $scope.unReimbObject.entity != null && $scope.unReimbObject.entity.statusCode === 'T_VRF';
            };

            $scope.generatedReportsAndEmailsSectionVisible = function () {
                return $scope.unReimbObject.entity != null && ($scope.unReimbObject.entity.statusCode === 'T_VRF' || $scope.unReimbObject.entity.statusCode === 'REIMB');
            };

            $scope.getReportFile = function(attachment) {
                return attachment.id != null ? contextPath + "/rest/publicBenefits/electronic/reimbursements/downloadReportFile?id=" + attachment.id : '';
            };

            $scope.generateMailFromTemplatesOnInitIfNull = function () {
                if($scope.unReimbObject.entity.emails == null || $scope.unReimbObject.entity.emails === undefined || $scope.unReimbObject.entity.emails.length < 1){
                    UnreservedPoolService.createEmailsFromTemplate();
                }
            };

            $scope.addNewAttToMail = function(mail) {
                mail.attachments.push({});
            };

            $scope.deleteMailAtt = function(parent, child) {
                var index = parent.attachments.indexOf(child);
                parent.attachments.splice(index, 1);
            };

            $scope.sendMail = function (mail) {
                UnreservedPoolService.sendMail(mail);
            };

            $scope.createDocuments = UnreservedPoolService.createDocuments;
            $scope.printReports = UnreservedPoolService.printReports;
            $scope.expire = UnreservedPoolService.expire;

    }]);