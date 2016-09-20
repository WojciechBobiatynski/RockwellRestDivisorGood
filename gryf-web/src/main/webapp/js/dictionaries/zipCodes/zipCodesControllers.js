'use strict';

var scopeZipCodes;

angular.module("gryf.dictionaries").controller("zipCodesController",
    ["$scope", "$log", "ZipCodesModel", "GryfPopups", function($scope, $log, ZipCodesModel, GryfPopups) {

        scopeZipCodes = $scope;

        $scope.model = ZipCodesModel.getSearchDTO();
        $scope.searchResultOptions = ZipCodesModel.getSearchResultOptions();
        gryfSessionStorage.setUrlToSessionStorage();
        GryfPopups.showPopup();

        $scope.findStates = function() {
            ZipCodesModel.findStates().then(function(response) {
                $scope.model.statesArray = response.data;
            });
        };
        $scope.findStates();

        $scope.findZipCodes = function() {
            $scope.searchResultOptions.badQuery = false;
            ZipCodesModel.find();
        };

        $scope.clearZipCodes = function() {
            $scope.model = ZipCodesModel.getNewSearchDTO();
            $scope.findStates();
            $scope.searchResultOptions = ZipCodesModel.getNewSearchResultOptions();
        };

        $scope.setSort = function(sortColumn) {
            ZipCodesModel.setSort(sortColumn);
            $scope.findZipCodes();
        };

        $scope.moreZipCodes = function() {
            ZipCodesModel.loadMore();
        };

        $scope.getSortingTypeClass = function(columnName) {
            var sortingType = $scope.model.entity.sortTypes[0];
            if (columnName == $scope.model.entity.sortColumns[0]) {
                return sortingType;
            }
        }

    }]);

var scopeZipCode;

angular.module("gryf.dictionaries").controller("zipCodeController",
    ["$scope", "$log", "ZipCodeService", 'GryfModals', 'GryfPopups',
     function($scope, $log, ZipCodesService, GryfModals, GryfPopups) {
         scopeZipCode = $scope;
         $scope.model = {};
         $scope.violations = ZipCodesService.getViolations;
         gryfSessionStorage.setUrlToSessionStorage();

         GryfPopups.showPopup();

         var NEW_ZIP_CODE_URL = contextPath + "/dictionaries/zipCodes/#zipCode";

         $scope.newCode = function() {
             var messageText = {
                 message: "Wywołując tę akcję stracisz niezapisane dane "
             };

             GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM, messageText).result.then(function(result) {
                 if (!result) {
                     return;
                 }
                 window.location = NEW_ZIP_CODE_URL;
             });
         };

         $scope.getPrevUrl = function() {
             return gryfSessionStorage.getUrlFromSessionStorage();
         };


         $scope.loadZipCode = function() {
             ZipCodesService.loadZipCode().then(function(response) {
                 $scope.model.entity = response.data;
             });
         };

         ZipCodesService.findStates().then(function(response) {
             $scope.model.statesArray = response.data;
             $scope.loadZipCode();
         });

         $scope.saveAndAdd = function() {
             $scope.save(NEW_ZIP_CODE_URL);
         };

         $scope.save = function(redirectUrl) {
             var messageText = {
                 message: "Wywołując tę akcję zapiszesz nowy kod pocztowy"
             };
             GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM, messageText).result.then(function(result) {
                 if (!result) {
                     return;
                 }
                 executeSave();
             });

             function executeSave() {
                 $scope.violations = ZipCodesService.getNewViolations();
                 ZipCodesService.saveZipCode($scope.model.entity).then(function() {
                     if (!redirectUrl) {
                         redirectUrl = $scope.getPrevUrl();
                     }
                     window.location = redirectUrl;
                 });
             }
         };


     }]);