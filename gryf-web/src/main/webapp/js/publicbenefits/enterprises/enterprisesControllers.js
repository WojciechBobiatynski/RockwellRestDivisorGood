"use strict";

var scopeBrowseController;

angular.module("gryf.enterprises").controller("searchform.EnterpriseController",
    ["$scope", "BrowseEnterprisesService", "GryfPopups", function($scope, BrowseEnterprisesService, GryfPopups) {
        scopeBrowseController = $scope;
        $scope.searchObjModel = BrowseEnterprisesService.getSearchDTO();
        $scope.searchResultOptions = BrowseEnterprisesService.getSearchResultOptions();
        gryfSessionStorage.setUrlToSessionStorage();
        GryfPopups.showPopup();

        $scope.loadMore = function() {
            BrowseEnterprisesService.loadMore();
        };

        $scope.findEnterprises = function() {
            $scope.searchResultOptions.badQuery = false;
            BrowseEnterprisesService.find();
        };

        $scope.clear = function() {
            $scope.searchObjModel = BrowseEnterprisesService.getNewSearchDTO();
            $scope.searchResultOptions = BrowseEnterprisesService.resetSearchResultOptions();
        };

        $scope.getSortedEnterprises = function(sortColumnName) {
            $scope.searchResultOptions.badQuery = false;
            BrowseEnterprisesService.findSortedBy(sortColumnName);
        };

        $scope.getSortingTypeClass = function(columnName) {
            var sortingType = $scope.searchObjModel.entity.sortTypes[0];
            if (columnName == $scope.searchObjModel.entity.sortColumns[0]) {
                return sortingType;
            }
        }
    }]);

var scopeModifyController;
angular.module("gryf.enterprises").controller("detailsform.EnterpriseController",
    ["$scope", "ModifyEnterprisesService", 'GryfModals', 'GryfPopups',
     function($scope, ModifyEnterprisesService, GryfModals, GryfPopups) {
         scopeModifyController = $scope;
         $scope.model = ModifyEnterprisesService.getNewModel();
         $scope.violations = ModifyEnterprisesService.getNewViolations();
         gryfSessionStorage.setUrlToSessionStorage();
         GryfPopups.showPopup();

         var NEW_ENTERPRISE_URL =  contextPath + "/publicBenefits/enterprises/#modify";


         $scope.newEnterprise = function() {
             var messageText = {
                 message: "Wywołując tę akcję stracisz niezapisane dane "
             };

             GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM, messageText).result.then(function(result) {
                 if (!result) {
                     return;
                 }
                 window.location = NEW_ENTERPRISE_URL;
             });
         };

         $scope.getPrevUrl = function() {
             return gryfSessionStorage.getUrlFromSessionStorage();
         };


         $scope.loadEnterprise = function(id) {
             ModifyEnterprisesService.loadEnterprises(id);
         };
         $scope.loadEnterprise();


         $scope.loadContactTypes = function() {
             ModifyEnterprisesService.loadContactTypes();
         };
         $scope.loadContactTypes();

         $scope.hasNotPrivilege = function(privilege) {
             return !privileges[privilege];
         };

         $scope.saveAndAdd = function() {
             $scope.save(NEW_ENTERPRISE_URL);
         };

         $scope.save = function(redirectUrl) {
             var messageText = {
                 message: "Ta akcja zapisuje zmiany MŚP"
             };

             GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM, messageText).result.then(function(result) {
                 if (!result) {
                     return;
                 }
                 executeSave();
             });

             var executeSave = function() {
                 $scope.violations = ModifyEnterprisesService.getNewViolations();
                 ModifyEnterprisesService.save().then(function() {
                     if (!redirectUrl){
                         redirectUrl = $scope.getPrevUrl();
                     }
                     window.location = redirectUrl;
                 });
             }
         };

         $scope.addContact = function() {
             ModifyEnterprisesService.addItemToList();
         };

         $scope.removeContact = function(contact) {
             ModifyEnterprisesService.removeItemFromList(contact);
         };

         $scope.openInvoiceZipCodesLov = function() {
             ModifyEnterprisesService.openZipCodesLov().result.then(function(chosedItem) {
                 $scope.model.entity.zipCodeInvoice = chosedItem;
             });
         };

         $scope.openCorrZipCodesLov = function() {
             ModifyEnterprisesService.openZipCodesLov().result.then(function(chosedItem) {
                 $scope.model.entity.zipCodeCorr = chosedItem;
             });
         };
     }]);