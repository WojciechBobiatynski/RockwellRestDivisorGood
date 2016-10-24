var scopeDeliveriesController;
angular.module('gryf.reimbursements').controller("searchform.DeliveriesController",
    ['$scope', 'BrowseDeliveriesService', 'GryfPopups', function($scope, BrowseDeliveriesService, GryfPopups) {
        scopeDeliveriesController = $scope;
        $scope.searchDTO = BrowseDeliveriesService.getSearchDTO();
        $scope.searchResultOptions = BrowseDeliveriesService.getSearchResultOptions();
        gryfSessionStorage.setUrlToSessionStorage();
        GryfPopups.showPopup();

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

        $scope.find = function() {
            BrowseDeliveriesService.find();
        };

        BrowseDeliveriesService.getDeliveryStatuses().then(function(response) {
            $scope.deliveryStatuses = response.data;
        });

        $scope.loadMore = function() {
            BrowseDeliveriesService.loadMore();
        };

        $scope.clear = function() {
            $scope.searchDTO = BrowseDeliveriesService.getNewSearchDTO();
            $scope.searchResultOptions = BrowseDeliveriesService.getNewSearchResultOptions();
        };

        $scope.getSortedBy = function(columnName) {
            $scope.searchResultOptions.badQuery = false;
            BrowseDeliveriesService.getSortedBy(columnName);
        };

        $scope.getSortingTypeClass = function(columnName) {
            return BrowseDeliveriesService.getSortingTypeClass($scope.searchDTO.entity, columnName);
        };

        $scope.$on('keydown:119', function(onEvent, keypressEvent) {
            $scope.find();
        });

        $scope.$on('triggerFind', function() {
            $scope.find();
        });
    }]);

var scopeRegisterDeliveriesController;
angular.module('gryf.reimbursements').controller("register.DeliveriesController",
    ['$scope', 'RegisterDeliveriesService', 'GryfPopups', 'GryfModals', 'AltShortcutHandler',
     '$location', 'BrowseTrainingInsService',
     function($scope, RegisterDeliveriesService, GryfPopups, GryfModals, AltShortcutHandler,
              $location, BrowseTrainingInsService) {
         scopeRegisterDeliveriesController = $scope;
         $scope.entityObject = RegisterDeliveriesService.getNewEntityObject();
         $scope.violations = RegisterDeliveriesService.getNewViolations();
         gryfSessionStorage.setUrlToSessionStorage();
         GryfPopups.showPopup();

         $scope.datepicker = {
             plannedReceiptDateOpened: false,
             requestDateOpened: false,
             deliveryDateOpened: false,
             announcementDateOpened: false
         };

         RegisterDeliveriesService.getReimbursementPatterns().then(function(response) {
             $scope.reimbursementPatterns = response.data;
         });

         $scope.loadDelivery = function() {
             RegisterDeliveriesService.loadDelivery();
         };

         $scope.loadDelivery();

         $scope.getPrevUrl = function() {
             return gryfSessionStorage.getUrlFromSessionStorage();
         };

         $scope.newDelivery = function() {
             var NEW_DELIVERY_URL = contextPath + "/publicBenefits/reimbursements/#registerDelivery";

             var messageText = {
                 message: "Wywołując tę akcję stracisz niezapisane dane "
             };

             GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM, messageText).result.then(function(result) {
                 if (!result) {
                     return;
                 }
                 window.location = NEW_DELIVERY_URL;
             });
         };


         $scope.openDatepicker = function(value) {
             $scope.datepicker[value] = true;
         };

         $scope.openIsLov = function() {
             RegisterDeliveriesService.openTrainingInstitutionLov().result.then(function(chosenTI) {
                 $scope.entityObject.trainingInstitution = chosenTI;
             });
         };

         $scope.save = function() {
             $scope.violations = RegisterDeliveriesService.getNewViolations();
             RegisterDeliveriesService.save();
         };

         $scope.settlementDelivery = function() {
             $scope.violations = RegisterDeliveriesService.getNewViolations();
             RegisterDeliveriesService.settlementDelivery();
         };

         $scope.cancelDelivery = function() {
             $scope.violations = RegisterDeliveriesService.getNewViolations();
             RegisterDeliveriesService.cancelDelivery();
         };

         AltShortcutHandler.attachAltShortcut(49, function() {
             $scope.save();
         });

         AltShortcutHandler.attachAltShortcut(50, function() {
             if ($scope.entityObject.status.id === 'SCANNED') {
                 $scope.settlementDelivery();
             }
         });

         AltShortcutHandler.attachAltShortcut(51, function() {
             if ($scope.entityObject.status.id === 'ORDERED' ||
                 $scope.entityObject.status.id === 'DELIVERED') {
                 $scope.cancelDelivery();
             }
         });

         var locSearch = $location.search();
         var trainingInstitutionQueryId = locSearch['trainingInstitution'];
         if (trainingInstitutionQueryId){
             var ti = BrowseTrainingInsService.findById(trainingInstitutionQueryId);
             ti.then(function(response) {
                 $scope.entityObject.trainingInstitution = response.data[0];
             })
         }
     }]);