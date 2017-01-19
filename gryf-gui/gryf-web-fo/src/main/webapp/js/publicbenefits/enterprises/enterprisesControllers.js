"use strict";

var scopeBrowseController;

angular.module("gryf.enterprises").controller("searchform.EnterpriseController",
    ["$scope", "BrowseEnterprisesService", "GryfPopups",
    function($scope, BrowseEnterprisesService, GryfPopups) {

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
    ["$scope", "ModifyEnterprisesService", 'GryfModals', 'GryfPopups', "GryfModulesUrlProvider",
     function($scope, ModifyEnterprisesService, GryfModals, GryfPopups, GryfModulesUrlProvider) {
         scopeModifyController = $scope;
         $scope.model = ModifyEnterprisesService.getNewModel();
         $scope.violations = ModifyEnterprisesService.getNewViolations();
         GryfPopups.showPopup();

         $scope.goBack = function() {
             var callback = function() {
                 window.location = GryfModulesUrlProvider.getBackUrl(GryfModulesUrlProvider.MODULES.Enterprise);
             };
             $scope.showAcceptModal("Wywołując tę akcję stracisz niezapisane dane", callback);
         };

         $scope.newEnterprise = function() {
             var callback = function() {
                 window.location = GryfModulesUrlProvider.LINKS.Enterprise;
             };
              $scope.showAcceptModal("Wywołując tę akcję stracisz niezapisane dane", callback);
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
             var saveCallback = function() {
                 ModifyEnterprisesService.save().success(function() {
                     window.location = GryfModulesUrlProvider.LINKS.Enterprise;
                     $scope.model = ModifyEnterprisesService.getNewModel();
                     $scope.violations = ModifyEnterprisesService.getNewViolations();
                 });
             };
             $scope.showAcceptModal("Ta akcja zapisuje zmiany w MŚP", saveCallback);
         };

         $scope.save = function() {
             var saveCallback = function() {
                 ModifyEnterprisesService.save().success(function(id) {
                     window.location = GryfModulesUrlProvider.getUrlFor(GryfModulesUrlProvider.MODULES.Enterprise, id);
                     ModifyEnterprisesService.loadEnterprises();
                 });
             };
             $scope.showAcceptModal("Ta akcja zapisuje zmiany w MŚP", saveCallback);
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