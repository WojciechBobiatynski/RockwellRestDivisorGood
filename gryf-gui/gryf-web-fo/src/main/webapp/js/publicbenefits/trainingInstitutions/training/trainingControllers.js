"use strict";

var scopeBrowseController;

angular.module("gryf.training").controller("searchform.TrainingController",
    ["$scope", "BrowseTrainingService", 'GryfPopups', function($scope, BrowseTrainingService, GryfPopups) {
        $scope.searchDTO = BrowseTrainingService.getSearchDTO();
        $scope.searchResultOptions = BrowseTrainingService.getSearchResultOptions();
        gryfSessionStorage.setUrlToSessionStorage();


        $scope.find = function() {
            $scope.searchResultOptions.badQuery = false;
            BrowseTrainingService.find();
        };

        $scope.getSortedBy = function(sortColumnName) {
            $scope.searchResultOptions.badQuery = false;
            BrowseTrainingService.findSortedBy(sortColumnName);
        };

        $scope.getSortingTypeClass = function(columnName) {
            return BrowseTrainingService.getSortingTypeClass($scope.searchDTO.entity, columnName);
        };

        $scope.clear = function() {
            $scope.searchDTO = BrowseTrainingService.getNewSearchDTO();
            $scope.searchResultOptions = BrowseTrainingService.getNewSearchResultOptions();
        };

        $scope.openIsLov = function() {
            BrowseTrainingService.openTrainingInstitutionLov().result.then(function(chosenTI) {
                $scope.searchDTO.entity.institutionId = chosenTI.id;
                $scope.searchDTO.entity.institutionName = chosenTI.name;
            });
        };

        $scope.datepicker = {
            isTrainingStartDateFromOpened: false,
            isTrainingStartDateToOpened: false,
            isTrainingEndDateFromOpened: false,
            isTrainingEndDateToOpened: false
        };

        $scope.openDatepicker = function(value) {
            $scope.datepicker[value] = true;
        };

        BrowseTrainingService.getTrainingCategoriesDict().then(function(data) {
            $scope.categoryDictionary = data;
        });

        $scope.loadMore = function() {
            BrowseTrainingService.loadMore();
        };

        $scope.clear();
    }]);

angular.module("gryf.training").controller("detailsform.TrainingController",
    ["$scope", "ModifyTrainingService", 'GryfModals', 'GryfPopups', '$routeParams',
     function($scope, ModifyTrainingService, GryfModals, GryfPopups, $routeParams) {
         gryfSessionStorage.setUrlToSessionStorage();

         $scope.model = ModifyTrainingService.getNewModel();
         $scope.violations = ModifyTrainingService.getNewViolations();
         $scope.trainingCategory = ModifyTrainingService.getNewTrainingCategory();
         $scope.categoryCatalogs = ModifyTrainingService.getNewCategoryCatalogs();

         var NEW_TRAINING_URL = contextPath + "/publicBenefits/training/#/modify";

         $scope.save = function(redirectUrl) {
             GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM).result.then(function(result) {
                 if (!result) {
                     return;
                 }
                 executeSave();
             });
             var executeSave = function() {
                 $scope.violations = ModifyTrainingService.getNewViolations();
                 ModifyTrainingService.save().then(function() {
                     if (!redirectUrl) {
                         redirectUrl = $scope.getPrevUrl();
                     }
                     window.location = redirectUrl;
                 });
             };
         };

         $scope.saveAndAdd = function() {
             $scope.save(NEW_TRAINING_URL);
         };

         $scope.newTraining = function() {
             var messageText = {
                 message: "Wywołując tę akcję stracisz niezapisane dane "
             };

             GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM, messageText).result.then(function(result) {
                 if (!result) {
                     return;
                 }
                 window.location = NEW_TRAINING_URL;
             });
         };

         if($routeParams.id) {
             ModifyTrainingService.findById($routeParams.id);
         }

         $scope.getPrevUrl = function() {
             return gryfSessionStorage.getUrlFromSessionStorage();
         };

         $scope.loadTrainingCategoryCatalogs = function () {
             ModifyTrainingService.loadTrainingCategoryCatalogs();
         }

         $scope.loadTrainingCategoryCatalogs();

         $scope.categoryCatalogChanged = function () {
             var catalogId = $scope.model.entity.trainingCategoryCatalogId;
             if(catalogId) {
                 $scope.loadTrainingCategoriesByCatalogId(catalogId);
             }else {
                 $scope.trainingCategory.list = [];
             }
         }

         $scope.loadTrainingCategoriesByCatalogId = function (catalogId) {
             ModifyTrainingService.loadTrainingCategoriesByCatalogId(catalogId);
         };

         $scope.datepicker = {
             isTrainingStartDateFromOpened: false,
             isTrainingStartDateToOpened: false,
             isTrainingEndDateFromOpened: false,
             isTrainingEndDateToOpened: false
         };

         $scope.openDatepicker = function(value) {
             $scope.datepicker[value] = true;
         };

         $scope.openIsLov = function() {
             ModifyTrainingService.openTrainingInstitutionLov().result.then(function(chosenTI) {
                 $scope.model.entity.trainingInstitution = chosenTI.id;
                 $scope.model.entity.institutionName = chosenTI.name;
             });
         };

     }]);