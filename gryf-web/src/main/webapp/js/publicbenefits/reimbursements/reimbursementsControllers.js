"use strict";

var scopeReimbursementsController;
angular.module('gryf.reimbursements').controller("searchform.ReimbursementsController",
    ['$scope', 'BrowseReimbursementsService', 'GryfPopups', function($scope, BrowseReimbursementsService, GryfPopups) {
        scopeReimbursementsController = $scope;
        $scope.searchDTO = BrowseReimbursementsService.getSearchDTO();
        $scope.searchResultOptions = BrowseReimbursementsService.getSearchResultOptions();
        gryfSessionStorage.setUrlToSessionStorage();
        GryfPopups.showPopup();
        $scope.datepicker = {
            deliveryDateFromOpened: false,
            deliveryDateToOpened: false,
            announcementDateFromOpened: false,
            announcementDateToOpened: false,
            reimbursementDateFromOpened: false,
            reimbursementDateTo: false
        };

        $scope.find = function() {
            BrowseReimbursementsService.find();
        };

        $scope.openDatepicker = function(fieldName) {
            $scope.datepicker[fieldName] = true;
        };

        BrowseReimbursementsService.getReimbursementsStatuses().then(function(response) {
            $scope.reimbursementStatuses = response.data;
        });

        $scope.getSortedBy = function(columnName) {
            $scope.searchResultOptions.badQuery = false;
            BrowseReimbursementsService.getSortedBy(columnName);
        };

        $scope.getSortingTypeClass = function(columnName) {
            return BrowseReimbursementsService.getSortingTypeClass($scope.searchDTO.entity, columnName);
        };

        $scope.loadMore = function() {
            BrowseReimbursementsService.loadMore();
        };

        $scope.clear = function() {
            $scope.searchDTO = BrowseReimbursementsService.getNewSearchDTO();
            $scope.searchResultOptions = BrowseReimbursementsService.getNewSearchResultOptions();
        };
    }]);


var scopeAnnounceReimbursementsController;
angular.module('gryf.reimbursements').controller("announce.ReimbursementsController",
    ['$scope', 'AnnounceReimbursementsService', 'GryfModals', 'GryfPopups', '$location', 'BrowseDeliveriesService',
     function($scope, AnnounceReimbursementsService, GryfModals, GryfPopups, $location, BrowseDeliveriesService) {
         scopeAnnounceReimbursementsController = $scope;

         $scope.entityObject = AnnounceReimbursementsService.getNewEntityObject();
         $scope.violations = AnnounceReimbursementsService.getNewViolations();
         $scope.isReimbursementPatternSpecified = false;
         gryfSessionStorage.setUrlToSessionStorage();
         GryfPopups.showPopup();

         AnnounceReimbursementsService.getSex().then(function(response) {
             $scope.sex = response.data;
         });

         AnnounceReimbursementsService.getAttachmentTypes().then(function(response) {
             $scope.attachmentTypes = response.data;
         });

         $scope.loadReimbursement = function() {
             AnnounceReimbursementsService.loadReimbursement();
         };

         $scope.loadReimbursement();

         $scope.openDeliveryLov = function() {
             AnnounceReimbursementsService.openDeliveryLov().result.then(function(chosenDelivery) {
                 $scope.entityObject.reimbursementDelivery = chosenDelivery;
                 AnnounceReimbursementsService.createInitialReimbursement();
             });
         };

         $scope.getPatternSpecificUrl = function() {
             if ($scope.entityObject.reimbursementDelivery) {
                 return contextPath + "/templates/publicbenefits/reimbursements/versions/v"
                     + $scope.entityObject.reimbursementDelivery.reimbursementPattern.id + ".html";
             }
         };

         $scope.$watch('entityObject.reimbursementDelivery', function() {
             if ($scope.entityObject.reimbursementDelivery) {
                 AnnounceReimbursementsService.loadVersionSpecificContent().then(function() {
                     $scope.isReimbursementPatternSpecified = true;
                 })
             }
         });

         $scope.save = function() {
             resetViolations();
             AnnounceReimbursementsService.save();
         };

         $scope.isNewOrAnnouncedOrCorrected = function() {
             if ($scope.entityObject.status) {
                 return (!$scope.entityObject.status.id) ||
                     ($scope.entityObject.status.id === "ANNOUNCED") ||
                     ($scope.entityObject.status.id === "CORRECTED");
             }
             return true;
         };

         $scope.newReimbursement = function() {
             var NEW_REIMBURSEMENT_URL = contextPath + "/publicBenefits/reimbursements/#announceReimbursements";

             var messageText = {
                 message: "Wywołując tę akcję stracisz niezapisane dane "
             };

             GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM, messageText).result.then(function(result) {
                 if (!result) {
                     return;
                 }
                 resetViolations();
                 window.location = NEW_REIMBURSEMENT_URL;
             });
         };

         $scope.getPrevUrl = function() {
             return gryfSessionStorage.getUrlFromSessionStorage();
         };

         $scope.isInStatus = function(statusName) {
             if ($scope.entityObject.status) {
                 return $scope.entityObject.status.id === statusName;
             }
             return false;
         };

         $scope.correct = function() {
             resetViolations();
             AnnounceReimbursementsService.correct();
         };

         $scope.verify = function() {
             resetViolations();
             AnnounceReimbursementsService.verify();
         };

         $scope.settle = function() {
             resetViolations();
             AnnounceReimbursementsService.settle();
         };

         $scope.complete = function() {
             resetViolations();
             AnnounceReimbursementsService.complete();
         };

         $scope.cancel = function() {
             resetViolations();
             AnnounceReimbursementsService.cancel();
         };

         $scope.newTraining = function() {
             if (!$scope.entityObject.reimbursementTrainings) {
                 $scope.entityObject.reimbursementTrainings = [];
             }
             $scope.entityObject.reimbursementTrainings.push({});
         };


         $scope.addReimbursementAttachment = function() {
             if (!$scope.entityObject.reimbursementAttachments) {
                 $scope.entityObject.reimbursementAttachments = [];
             }
             $scope.entityObject.reimbursementAttachments.push({});
         };

         $scope.removeAttachment = function(attachement) {
             AnnounceReimbursementsService.removeItemFromList(attachement, $scope.entityObject.reimbursementAttachments);
         };

         $scope.removeAttachmentFromTrainee = function(attachment, traineeItem) {
             AnnounceReimbursementsService.removeItemFromList(attachment, traineeItem.reimbursementTraineeAttachments);
         };

         $scope.removeTrainee = function(trainee, training) {
             AnnounceReimbursementsService.removeItemFromList(trainee, training);
         };

         $scope.generateConfirmation = function() {
             AnnounceReimbursementsService.generateConfirmation();
         };

         function resetViolations() {
             $scope.violations = AnnounceReimbursementsService.getNewViolations();
         }

         var locSearch = $location.search();
         if (locSearch['deliveryId']) {
             BrowseDeliveriesService.findById(locSearch['deliveryId']).then(function(response) {
                 var statusId = response.data[0].status.id;
                 if (statusId === 'DELIVERED' || statusId === 'SCANNED') {
                     $scope.entityObject.reimbursementDelivery = response.data[0];
                 }
             })
         }
     }]);


