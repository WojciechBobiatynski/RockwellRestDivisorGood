"use strict";

var scopeBrowseController;

angular.module("gryf.trainingInstitutions").controller("searchform.TrainingInsController",
    ["$scope", "BrowseTrainingInsService", 'GryfPopups', function($scope, BrowseTrainingInsService, GryfPopups) {
        scopeBrowseController = $scope;
        $scope.searchObjModel = BrowseTrainingInsService.getSearchDTO();
        $scope.searchResultOptions = BrowseTrainingInsService.getSearchResultOptions();
        gryfSessionStorage.setUrlToSessionStorage();
        GryfPopups.showPopup();

        $scope.loadMore = function() {
            BrowseTrainingInsService.loadMore();
        };

        $scope.find = function() {
            $scope.searchResultOptions.badQuery = false;
            BrowseTrainingInsService.find();
        };

        $scope.clear = function() {
            $scope.searchObjModel = BrowseTrainingInsService.getNewSearchDTO();
            $scope.searchResultOptions = BrowseTrainingInsService.resetSearchResultOptions();
        };

        $scope.getSorted = function(sortColumnName) {
            $scope.searchResultOptions.badQuery = false;
            BrowseTrainingInsService.findSortedBy(sortColumnName);
        };

        $scope.getSortingTypeClass = function(columnName) {
            var sortingType = $scope.searchObjModel.entity.sortTypes[0];
            if (columnName == $scope.searchObjModel.entity.sortColumns[0]) {
                return sortingType;
            }
        }
    }]);

var scopeModifyController;
angular.module("gryf.trainingInstitutions").controller("detailsform.TrainingInsController",
    ["$scope", "ModifyTrainingInsService", 'GryfModals', 'GryfPopups',
     function($scope, ModifyTrainingInsService, GryfModals, GryfPopups) {
         scopeModifyController = $scope;
         $scope.model = ModifyTrainingInsService.getNewModel();
         $scope.violations = ModifyTrainingInsService.getNewViolations();
         gryfSessionStorage.setUrlToSessionStorage();
         GryfPopups.showPopup();


         var NEW_TI_URL = contextPath + "/publicBenefits/trainingInstitutions/#modify";


         $scope.newTrainingInstitution = function() {
             var messageText = {
                 message: "Wywołując tę akcję stracisz niezapisane dane "
             };

             GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM, messageText).result.then(function(result) {
                 if (!result) {
                     return;
                 }
                 window.location = NEW_TI_URL;
             });

         };

         $scope.getPrevUrl = function() {
             return gryfSessionStorage.getUrlFromSessionStorage();
         };

         $scope.loadTI = function(id) {
             ModifyTrainingInsService.load(id);
         };
         $scope.loadTI();

         $scope.loadContactTypes = function() {
             ModifyTrainingInsService.loadContactTypes();
         };
         $scope.loadContactTypes();

         $scope.saveAndAdd = function() {
             $scope.save(NEW_TI_URL);
         };

         $scope.save = function(redirectUrl) {

             GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM).result.then(function(result) {
                 if (!result) {
                     return;
                 }
                 executeSave();
             });
             var executeSave = function() {
                 $scope.violations = ModifyTrainingInsService.getNewViolations();
                 ModifyTrainingInsService.save().then(function() {
                     if (!redirectUrl) {
                         redirectUrl = $scope.getPrevUrl();
                     }
                     window.location = redirectUrl;
                 });
             };
         };

         $scope.addContact = function() {
             ModifyTrainingInsService.addItemToList();
         };

         $scope.addContact = function(contact) {
             ModifyTrainingInsService.addContact(contact);
         };

         $scope.openInvoiceZipCodesLov = function() {
             ModifyTrainingInsService.openZipCodesLov().result.then(function(chosedItem) {
                 $scope.model.entity.zipCodeInvoice = chosedItem;
             });
         };

         $scope.openCorrZipCodesLov = function() {
             ModifyTrainingInsService.openZipCodesLov().result.then(function(chosedItem) {
                 $scope.model.entity.zipCodeCorr = chosedItem;
             });
         };
     }]);