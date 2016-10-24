"use strict";

var scopeBrowseController;
angular.module('gryf.grantApplications').controller('searchform.GrantApplicationsController',
    ['$scope', 'BrowseGrantApplicationsService', 'Dictionaries', 'GryfPopups',
     function($scope, BrowseGrantApplicationsService, Dictionaries, GryfPopups) {
         scopeBrowseController = $scope;

         $scope.searchDTO = BrowseGrantApplicationsService.getSearchDTO();
         $scope.searchResultOptions = BrowseGrantApplicationsService.getSearchResultOptions();
         Dictionaries.getStatuses().then(function(response) {
             $scope.statuses = response.data;
         });
         gryfSessionStorage.setUrlToSessionStorage();
         GryfPopups.showPopup();

         $scope.datepicker = {
             considerationFromOpened: false,
             considerationToOpened: false
         };

         $scope.openDatepicker = function(value) {
             $scope.datepicker[value] = true;
         };

         $scope.findApplications = function() {
             BrowseGrantApplicationsService.find();
         };

         $scope.loadMore = function() {
             BrowseGrantApplicationsService.loadMore();
         };

         $scope.getSortedBy = function(columnName) {
             $scope.searchResultOptions.badQuery = false;
             BrowseGrantApplicationsService.getSortedBy(columnName);
         };

         $scope.getSortingTypeClass = function(columnName) {
             return BrowseGrantApplicationsService.getSortingTypeClass($scope.searchDTO.entity, columnName);
         };

         $scope.clear = function() {
             $scope.searchDTO = BrowseGrantApplicationsService.getNewSearchDTO();
             $scope.searchResultOptions = BrowseGrantApplicationsService.getNewSearchResultOptions();
         };
     }]);

var scopeModifyController;
angular.module('gryf.grantApplications').controller('detailsform.GrantApplicationsController',
    ['$scope', 'ModifyGrantApplicationService', 'GrantProgramService', 'Dictionaries', "GryfModals", 'GryfPopups','GryfModulesUrlProvider',
     function($scope, ModifyGrantApplicationService, GrantProgramService, Dictionaries, GryfModals, GryfPopups,GryfModulesUrlProvider) {
         scopeModifyController = $scope;
         $scope.grantProgram = GrantProgramService.getGrantProgram();
         $scope.entityObject = ModifyGrantApplicationService.getEntityObject();
         $scope.dictionaries = ModifyGrantApplicationService.getDictionaries();
         $scope.violations = ModifyGrantApplicationService.getViolations();
         gryfSessionStorage.setUrlToSessionStorage();
         GryfPopups.showPopup();

         $scope.getOrderLinkFor = function(orderId) {
               return GryfModulesUrlProvider.getUrlFor(GryfModulesUrlProvider.MODULES.Order , orderId);
         };

         $scope.datepicker = {
             opened: false
         };

         $scope.openDatepicker = function() {
             $scope.datepicker.opened = true;
         };


         $scope.getPrevUrl = function() {
             return gryfSessionStorage.getUrlFromSessionStorage();
         };

         $scope.newApplication = function() {
             var messageText = {
                 message: "Wywołując tę akcję stracisz niezapisane dane "
             };

             GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM, messageText).result.then(function(result) {
                 if (!result) {
                     return;
                 }
                 $scope.violations = ModifyGrantApplicationService.getNewViolations();
                 window.location = contextPath + '/publicBenefits/grantApplications/#modify';
             });
         };

         GrantProgramService.getGrantPrograms();

         $scope.save = function() {
             $scope.violations = ModifyGrantApplicationService.getNewViolations();
             ModifyGrantApplicationService.save();
         };

         $scope.apply = function() {
             $scope.violations = ModifyGrantApplicationService.getNewViolations();
             ModifyGrantApplicationService.apply();
         };

         $scope.execute = function() {
             $scope.violations = ModifyGrantApplicationService.getNewViolations();
             ModifyGrantApplicationService.execute();
         };

         $scope.reject = function() {
             $scope.violations = ModifyGrantApplicationService.getNewViolations();
             ModifyGrantApplicationService.reject();
         };

         $scope.fetchVersions = function() {
             if ($scope.entityObject.grantProgram) {
                 GrantProgramService.getVersions($scope.entityObject.grantProgram.id);
             }
         };

         $scope.loadApplication = function() {
             var promise = ModifyGrantApplicationService.loadApplication();
             if (promise) {
                 promise.then(function(response) {
                     $scope.changeVersion().then(function() {
                         ModifyGrantApplicationService.populateVersionSpecificModel(response.data);
                     });
                 });
             }
         };

         $scope.loadApplication();

         $scope.openEnterpriseLov = function() {
             ModifyGrantApplicationService.openEnterpriseLov().result.then(function(chosenEnterprise) {
                 $scope.entityObject.enterprise = chosenEnterprise;
                 $scope.$broadcast('propagateEnterpriseData', chosenEnterprise);
             });
         };

         $scope.addAttachment = function() {
             ModifyGrantApplicationService.addItemToList($scope.entityObject.attachments);
         };

         $scope.removeAttachment = function(attachement) {
             ModifyGrantApplicationService.removeItemFromList(attachement, $scope.entityObject.attachments);
         };

         $scope.getDownloadAttachmentLink = function(attachement) {
             return contextPath + "/publicBenefits/grantApplications/downloadAttachment?id=" + attachement.id;
         };

         $scope.$watch('entityObject.grantProgram', function() {
             $scope.fetchVersions();
         });

         $scope.changeVersion = function() {
             var promise = ModifyGrantApplicationService.loadVersionSpecificContent();
             if (promise) {
                 promise.then(function() {
                     var templateFileName = $scope.entityObject.applicationVersion.templateName + ".html";
                     var templateFoldersPath = contextPath + "/templates/publicbenefits/grantapplications/versions/";

                     $scope.templateUrl = templateFoldersPath + templateFileName;
                     ModifyGrantApplicationService.addMandatoryAttachments();
                 });
                 return promise;
             }
         };

         $scope.validateSize = function(files, attachmentParent) {
             if (files.length) {
                 ModifyGrantApplicationService.validateAttachmentsSize(files, attachmentParent);
             }
         };

         $scope.getAttachmentMaxSize = function() {
             return (attachmentMaxSize / 1024 / 1024);
         };

         $scope.isNotApplied = function() {
             return $scope.entityObject.status.id !== Dictionaries.STATUSES.APPLIED.id;
         };

         $scope.isNotNew = function() {
             if (!$scope.entityObject.status.id) {
                 return false;
             }
             return $scope.entityObject.status.id !== Dictionaries.STATUSES.NEW.id;
         };

         $scope.isRejectedOrExecutedOrApplied = function() {
             var currentStatus = $scope.entityObject.status.id;
             return currentStatus === Dictionaries.STATUSES.REJECTED.id
                 || currentStatus === Dictionaries.STATUSES.EXECUTED.id
                 || currentStatus === Dictionaries.STATUSES.APPLIED.id;
         }
     }]);