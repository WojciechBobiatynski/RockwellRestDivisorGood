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
            plannedReceiptDateFromOpened: false,
            plannedReceiptDateToOpened: false,
            requestDateFromOpened: false,
            requestDateToOpened: false,
            deliveryDateFromOpened: false,
            deliveryDateToOpened: false
        };

        $scope.openDatepicker = function(value) {
            $scope.datepicker[value] = true;
        };

        BrowseTrainingService.getTrainingCategoriesDict().then(function(data) {
            $scope.categoryDictionary = data;
        });


        $scope.clear();
    }]);

angular.module("gryf.training").controller("detailsform.TrainingController",
    ["$scope", "ModifyTrainingService", 'GryfModals', 'GryfPopups', '$routeParams',
     function($scope, ModifyTrainingService, GryfModals, GryfPopups, $routeParams) {
         gryfSessionStorage.setUrlToSessionStorage();

         $scope.model = ModifyTrainingService.getNewModel();
         $scope.violations = ModifyTrainingService.getNewViolations();

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

         ModifyTrainingService.getTrainingCategoriesDict().then(function(data) {
             $scope.categoryDictionary = data;
         });

         $scope.datepicker = {
             plannedReceiptDateFromOpened: false,
             plannedReceiptDateToOpened: false,
             requestDateFromOpened: false,
             requestDateToOpened: false,
             deliveryDateFromOpened: false,
             deliveryDateToOpened: false
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