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
    ["$scope", "ModifyTrainingService", 'GryfModals', 'GryfPopups', '$routeParams', "GryfModulesUrlProvider",
     function($scope, ModifyTrainingService, GryfModals, GryfPopups, $routeParams, GryfModulesUrlProvider) {
         $scope.model = ModifyTrainingService.getNewModel();
         $scope.violations = ModifyTrainingService.getNewViolations();
         $scope.trainingCategory = ModifyTrainingService.getNewTrainingCategory();
         $scope.categoryCatalogs = ModifyTrainingService.getNewCategoryCatalogs();

         $scope.goBack = function() {
             var callback = function() {
                 window.location = GryfModulesUrlProvider.getBackUrl(GryfModulesUrlProvider.MODULES.Training);
             };
             $scope.showAcceptModal("Wywołując tę akcję stracisz niezapisane dane", callback);
         };

         $scope.newTraining = function() {
             var callback = function() {
                 window.location = GryfModulesUrlProvider.LINKS.Training;
             };
             $scope.showAcceptModal("Wywołując tę akcję stracisz niezapisane dane", callback);
         };

         $scope.saveAndAdd = function () {
             var saveCallback = function () {
                 $scope.violations = ModifyTrainingService.getNewViolations();
                 ModifyTrainingService.save().success(function () {
                     window.location = GryfModulesUrlProvider.LINKS.Training;
                 });
             };
             $scope.showAcceptModal("Ta akcja zapisuje zmiany w szkoleniu", saveCallback);
         };

         $scope.save = function () {
             var saveCallback = function () {
                 $scope.violations = ModifyTrainingService.getNewViolations();
                 ModifyTrainingService.save().success(function (id) {
                     window.location = GryfModulesUrlProvider.getUrlFor(GryfModulesUrlProvider.MODULES.Training, id);
                     ModifyTrainingService.findById($routeParams.id);
                 });
             };
             $scope.showAcceptModal("Ta akcja zapisuje zmiany w szkoleniu", saveCallback);
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