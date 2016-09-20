angular.module('gryf.reimbursements').factory("BrowseReimbursementsService",
    ['$http', 'GryfModals', 'GryfHelpers', 'GryfTables', function($http, GryfModals, GryfHelpers, GryfTables) {
        ////////////////////////////////
        //CONSTANTS
        ////////////////////////////////
        var FIND_REIMBURSEMENTS_URL = contextPath + "/rest/publicBenefits/reimbursements/reimbursements";
        var FIND_REIMBURSEMENTS_STATUSES_URL = contextPath + "/rest/publicBenefits/dictionaries/REIMBURSEMENT_STATUS";

        ////////////////////////////////
        //MODEL INSTANCES
        ////////////////////////////////
        var searchDTO = new SearchDTO();
        var searchResultOptions = new SearchResultOptions();

        ////////////////////////////////
        //MODEL
        ////////////////////////////////
        function SearchDTO() {
            this.searchResultList = [];
            this.entity = {
                id: null,
                invoiceNumber: null,
                trainingInstitutionVatRegNum: null,
                trainingInstitutionName: null,
                statusId: null,
                reimbursementDeliveryId: null,
                deliveryDateFrom: null,
                deliveryDateTo: null,
                announcementDateFrom: null,
                announcementDateTo: null,
                sortTypes: [],
                sortColumns: [],
                limit: 10
            }
        }

        function SearchResultOptions() {
            this.displayLimit = 10;
            this.displayLimitIncrementer = 10;
            this.overflow = false;
            this.badQuery = false;
        }


        var find = function(findUrl) {
            var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING);

            GryfHelpers.transformDatesToString(searchDTO.entity);
            if (!findUrl) {
                findUrl = FIND_REIMBURSEMENTS_URL;
            }
            var promise = $http.get(findUrl, {params: searchDTO.entity});
            promise.then(function(response) {
                //success
                searchDTO.searchResultList = response.data;
                searchResultOptions.overflow = response.data.length > searchResultOptions.displayLimit;
            }, function() {
                //error
                searchResultOptions.badQuery = true;
            });

            promise.finally(function() {
                GryfModals.closeModal(modalInstance);
            });
            return promise;
        };

        var getSearchDTO = function() {
            return searchDTO;
        };

        var getNewSearchDTO = function() {
            searchDTO = new SearchDTO();
            return searchDTO;
        };

        var getSearchResultOptions = function() {
            return searchResultOptions;
        };

        var getNewSearchResultOptions = function() {
            searchResultOptions = new SearchResultOptions();
            return searchResultOptions;
        };

        var getReimbursementsStatuses = function() {
            return $http.get(FIND_REIMBURSEMENTS_STATUSES_URL);
        };

        var getSortedBy = function(sortColumnName) {
            GryfTables.sortByColumn(searchDTO.entity, sortColumnName);
            return find();
        };

        var getSortingTypeClass = function(entity, columnName) {
            return GryfTables.getSortingTypeClass(entity, columnName);
        };

        var loadMore = function() {
            searchDTO.entity.limit += searchResultOptions.displayLimitIncrementer;
            searchResultOptions.displayLimit += searchResultOptions.displayLimitIncrementer;
            return find();
        };

        return {
            find: find,
            getSearchDTO: getSearchDTO,
            getSearchResultOptions: getSearchResultOptions,
            getReimbursementsStatuses: getReimbursementsStatuses,
            getSortedBy: getSortedBy,
            getSortingTypeClass: getSortingTypeClass,
            loadMore: loadMore,
            getNewSearchDTO: getNewSearchDTO,
            getNewSearchResultOptions: getNewSearchResultOptions

        };
    }]);

angular.module('gryf.reimbursements').factory("AnnounceReimbursementsService",
    ['$http', 'GryfModals', 'GryfPopups', '$routeParams', 'GryfHelpers', 'GryfExceptionHandler',
     'BrowseDeliveriesService', 'angularLoad', 'BrowseEnterprisesService', 'Upload', '$location', '$route',
     function($http, GryfModals, GryfPopups, $routeParams, GryfHelpers,
              GryfExceptionHandler, BrowseDeliveriesService, angularLoad,
              BrowseEnterprisesService, Upload, $location, $route) {
         var REIMBURSEMENTS_URL = contextPath + "/rest/publicBenefits/reimbursements/v1/reimbursement/";
         var FIND_GRANT_OWNER_AID_PROD_URL = contextPath + "/rest/publicBenefits/dictionaries/GRANT_OWNER_AID_PRODUCT";
         var FIND_SEX_URL = contextPath + "/rest/publicBenefits/dictionaries/SEX";
         var FIND_ATTACHMENT_TYPES_URL = contextPath + "/rest/publicBenefits/dictionaries/ATTACHMENT_TYPES";
         var INIT_REIMBURSEMENT_URL = contextPath + "/rest/publicBenefits/reimbursements/v1/reimbursement/initial/";

         var entityObject = new EntityObject();
         var violations = {};

         function EntityObject() {
             this.id = null;
             this.announcementDate = null;
             this.correctionDate = null;
             this.correctionReason = null;
             this.correctionsNumber = null;
             this.enterprise = null;
             this.invoiceAnonGrossAmount = null;
             this.invoiceAnonVouchAmount = null;
             this.invoiceNumber = null;
             this.entToTiAmountDueTotal = null;
             this.usedOwnEntContributionAmountTotal = null;
             this.grantAmountTotal = null;
             this.grantAmountPayedToTiTotal = null;
             this.trainingCostTotal = null;
             this.reimbursementAttachments = null;
             this.reimbursementDate = null;
             this.reimbursementDelivery = null;
             this.reimbursementTrainings = [];
             this.remarks = null;
             this.requiredCorrectionDate = null;
             this.sxoEntAmountDueTotal = null;
             this.sxoTiAmountDueTotal = null;
             this.trainingInstitutionReimbursementAccountNumber = null;
             this.transferDate = null;
             this.status = null;
             this.reimbursementTraineeAttachmentRequiredList = null;
             this.version = null;
         }

         var loadReimbursement = function(idFromResponse) {
             GryfPopups.showPopup();
             if ($routeParams.id || idFromResponse) {
                 var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Wczytuję dane"});
                 var promise = $http.get(REIMBURSEMENTS_URL + ($routeParams.id ? $routeParams.id : idFromResponse));
                 promise.then(function(response) {
                     GryfHelpers.copyPropertiesIfExist(entityObject, response.data);
                 });
                 promise.finally(function() {
                     GryfModals.closeModal(modalInstance);
                 });
                 return promise;
             }
         };

         var openDeliveryLov = function() {
             return GryfModals.openLovModal(GryfModals.MODALS_URL.LOV_DELIVERY, BrowseDeliveriesService, "lg");
         };

         var getNewEntityObject = function() {
             entityObject = new EntityObject();
             return entityObject;
         };
         var getNewViolations = function() {
             violations = {};
             return violations;
         };

         var loadVersionSpecificContent = function() {
             var templateName = entityObject.reimbursementDelivery.reimbursementPattern.id;
             var VERSION_MODEL_FOLDER = contextPath + "/js/publicbenefits/reimbursements/versions/v";
             if (templateName) {
                 return angularLoad.loadScript(VERSION_MODEL_FOLDER + templateName + ".js");
             }
         };

         var openEnterpriseLov = function() {
             var TEMPLATE_URL = GryfModals.MODALS_URL.LOV_ENTERPRISES;
             return GryfModals.openLovModal(TEMPLATE_URL, BrowseEnterprisesService, "lg");
         };

         var getGrantOwnerAidProduct = function() {
             return $http.get(FIND_GRANT_OWNER_AID_PROD_URL);
         };

         var getSex = function() {
             return $http.get(FIND_SEX_URL);
         };

         var getAttachmentTypes = function() {
             return $http.get(FIND_ATTACHMENT_TYPES_URL);
         };

         var createInitialReimbursement = function() {
             var promise = $http.get(INIT_REIMBURSEMENT_URL + entityObject.reimbursementDelivery.id);
             promise.then(function(response) {
                 GryfHelpers.copyPropertiesIfExist(entityObject, response.data);
             });
         };

         function findAllFileAttachments() {
             var resultArray = [];
             if (entityObject.reimbursementAttachments) {
                 for (var z = 0; z < entityObject.reimbursementAttachments.length; z++) {
                     var attach = entityObject.reimbursementAttachments[z];
                     if (attach && attach["file"] && attach["file"]["length"]) {
                         resultArray.push(attach.file[0]);
                         attach.fileIncluded = true;
                     }
                 }
             }
             var trainings = entityObject.reimbursementTrainings;
             if (trainings) {
                 for (var i = 0; i < trainings.length; i++) {
                     var trainees = trainings[i].reimbursementTrainees;
                     if (trainees) {
                         for (var k = 0; k < trainees.length; k++) {
                             var attachments = trainees[k].reimbursementTraineeAttachments;
                             if (attachments) {
                                 for (var l = 0; l < attachments.length; l++) {
                                     var attachmentField = attachments[l];
                                     if (attachmentField && attachmentField['file'] && attachmentField['file']["length"]) {
                                         resultArray.push(attachmentField.file[0]);
                                         attachmentField.fileIncluded = true;
                                     }
                                 }
                             }
                         }
                     }
                 }
             }
             return resultArray;
         }

         var save = function() {
             var SAVE_REIMBURSEMENT_URL = contextPath + "/rest/publicBenefits/reimbursements/v"
                 + entityObject.reimbursementDelivery.reimbursementPattern.id
                 + "/reimbursement/save";
             GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM).result.then(function(result) {
                 if (!result) {
                     return;
                 }
                 var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Zapisuję dane"});
                 var files = findAllFileAttachments();
                 var mixinString = angular.toJson(entityObject);

                 Upload.upload({
                     url: SAVE_REIMBURSEMENT_URL,
                     data: mixinString,
                     file: files
                 }).success(function(id) {
                     GryfPopups.setPopup("success", "Sukces", "Rozliczenie poprawnie zapisane");
                     if (!$routeParams.id) {
                         $location.search('id', id);
                     }
                     $route.reload();
                 }).error(function(response) {
                     GryfPopups.setPopup("error", "Błąd", "Nie udało się zapisać rozliczenia.");
                     GryfPopups.showPopup();
                     var conflictCallbacksObject = {
                         refresh: function() {
                             if (!$routeParams.id) {
                                 $location.search('id', response.id);
                             }
                             $route.reload();
                         },
                         force: function() {
                             entityObject.version = response.version;
                             save();
                         }
                     };
                     GryfExceptionHandler.handleSavingError(response, violations, conflictCallbacksObject);
                 }).finally(function() {
                     GryfModals.closeModal(modalInstance);
                 });
             });
         };

         var correct = function() {
             var CORRECT_REIMBURSEMENT_URL = contextPath + "/rest/publicBenefits/reimbursements/v"
                 + entityObject.reimbursementDelivery.reimbursementPattern.id
                 + "/reimbursement/correct";
             GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM).result.then(function(result) {
                 if (!result) {
                     return;
                 }
                 var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Zapisuję dane"});
                 var files = findAllFileAttachments();
                 var mixinString = angular.toJson(entityObject);

                 Upload.upload({
                     url: CORRECT_REIMBURSEMENT_URL,
                     data: mixinString,
                     file: files
                 }).success(function(id) {
                     GryfPopups.setPopup("success", "Sukces", "Rozliczenie poprawnie oddano do korekty");
                     if (!$routeParams.id) {
                         $location.search('id', id);
                     }
                     $route.reload();
                 }).error(function(response) {
                     GryfPopups.setPopup("error", "Błąd", "Nie udało się oddać do korekty rozliczenia.");
                     GryfPopups.showPopup();
                     var conflictCallbacksObject = {
                         refresh: function() {
                             if (!$routeParams.id) {
                                 $location.search('id', response.id);
                             }
                             $route.reload();
                         },
                         force: function() {
                             entityObject.version = response.version;
                             correct();
                         }
                     };
                     GryfExceptionHandler.handleSavingError(response, violations, conflictCallbacksObject);
                 }).finally(function() {
                     GryfModals.closeModal(modalInstance);
                 });
             });
         };

         var verify = function() {
             var VERIFY_REIMBURSEMENT_URL = contextPath + "/rest/publicBenefits/reimbursements/v"
                 + entityObject.reimbursementDelivery.reimbursementPattern.id
                 + "/reimbursement/verify";
             GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM).result.then(function(result) {
                 if (!result) {
                     return;
                 }
                 var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Zapisuję dane"});
                 var files = findAllFileAttachments();
                 var mixinString = angular.toJson(entityObject);

                 Upload.upload({
                     url: VERIFY_REIMBURSEMENT_URL,
                     data: mixinString,
                     file: files
                 }).success(function(id) {
                     GryfPopups.setPopup("success", "Sukces", "Rozliczenie poprawnie oddano do weryfikacji");
                     if (!$routeParams.id) {
                         $location.search('id', id);
                     }
                     $route.reload();
                 }).error(function(response) {
                     GryfPopups.setPopup("error", "Błąd", "Nie udało się oddać do weryfikacji rozliczenia.");
                     GryfPopups.showPopup();
                     var conflictCallbacksObject = {
                         refresh: function() {
                             if (!$routeParams.id) {
                                 $location.search('id', response.id);
                             }
                             $route.reload();
                         },
                         force: function() {
                             entityObject.version = response.version;
                             verify();
                         }
                     };

                     GryfExceptionHandler.handleSavingError(response, violations, conflictCallbacksObject);
                 }).finally(function() {
                     GryfModals.closeModal(modalInstance);
                 });
             });
         };

         var settle = function() {
             var SETTLE_REIMBURSEMENT_URL = contextPath + "/rest/publicBenefits/reimbursements/v"
                 + entityObject.reimbursementDelivery.reimbursementPattern.id
                 + "/reimbursement/settle";
             GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM).result.then(function(result) {
                 if (!result) {
                     return;
                 }
                 var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Zapisuję dane"});
                 var files = findAllFileAttachments();
                 var mixinString = angular.toJson(entityObject);

                 Upload.upload({
                     url: SETTLE_REIMBURSEMENT_URL,
                     data: mixinString,
                     file: files
                 }).success(function(id) {
                     GryfPopups.setPopup("success", "Sukces", "Rozliczenie poprawnie oddano do rozliczenia");
                     if (!$routeParams.id) {
                         $location.search('id', id);
                     }
                     $route.reload();
                 }).error(function(response) {
                     GryfPopups.setPopup("error", "Błąd", "Rozliczenia nie udało się oddać do" +
                         " rozliczenia");
                     GryfPopups.showPopup();
                     var conflictCallbacksObject = {
                         refresh: function() {
                             if (!$routeParams.id) {
                                 $location.search('id', response.id);
                             }
                             $route.reload();
                         },
                         force: function() {
                             entityObject.version = response.version;
                             settle();
                         }
                     };
                     GryfExceptionHandler.handleSavingError(response, violations, conflictCallbacksObject);
                 }).finally(function() {
                     GryfModals.closeModal(modalInstance);
                 });
             });
         };

         var complete = function() {
             var COMPLETE_REIMBURSEMENT_URL = contextPath + "/rest/publicBenefits/reimbursements/v"
                 + entityObject.reimbursementDelivery.reimbursementPattern.id
                 + "/reimbursement/complete";
             GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM).result.then(function(result) {
                 if (!result) {
                     return;
                 }
                 var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Zapisuję dane"});
                 var files = findAllFileAttachments();
                 var mixinString = angular.toJson(entityObject);

                 Upload.upload({
                     url: COMPLETE_REIMBURSEMENT_URL,
                     data: mixinString,
                     file: files
                 }).success(function(id) {
                     GryfPopups.setPopup("success", "Sukces", "Rozliczenie poprawnie zakończono");
                     if (!$routeParams.id) {
                         $location.search('id', id);
                     }
                     $route.reload();
                 }).error(function(response) {
                     GryfPopups.setPopup("error", "Błąd", "Nie udało się  zakończyć rozliczenia." +
                         " rozliczenia");
                     GryfPopups.showPopup();
                     var conflictCallbacksObject = {
                         refresh: function() {
                             if (!$routeParams.id) {
                                 $location.search('id', response.id);
                             }
                             $route.reload();
                         },
                         force: function() {
                             entityObject.version = response.version;
                             complete();
                         }
                     };
                     GryfExceptionHandler.handleSavingError(response, violations, conflictCallbacksObject);
                 }).finally(function() {
                     GryfModals.closeModal(modalInstance);
                 });
             });
         };

         var cancel = function() {
             var CANCEL_REIMBURSEMENT_URL = contextPath + "/rest/publicBenefits/reimbursements/v"
                 + entityObject.reimbursementDelivery.reimbursementPattern.id
                 + "/reimbursement/cancel";
             GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM).result.then(function(result) {
                 if (!result) {
                     return;
                 }
                 var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Zapisuję dane"});
                 $http.post(CANCEL_REIMBURSEMENT_URL, entityObject).then(function(response) {
                     GryfPopups.setPopup("success", "Sukces", "Rozliczenie poprawnie anulowano");
                     loadReimbursement(response.data);
                 }, function(response) {
                     GryfPopups.setPopup("error", "Błąd", "Nie udało się anulowąć rozliczenia.");
                     GryfPopups.showPopup();
                     var conflictCallbacksObject = {
                         refresh: function() {
                             if (!$routeParams.id) {
                                 $location.search('id', response.data.id);
                             }
                             $route.reload();
                         },
                         force: function() {
                             entityObject.version = response.data.version;
                             cancel();
                         }
                     };
                     GryfExceptionHandler.handleSavingError(response.data, violations, conflictCallbacksObject);
                 }).finally(function() {
                     GryfModals.closeModal(modalInstance);
                 })
             });
         };

         var removeItemFromList = function(item, list) {
             var index = list.indexOf(item);
             list.splice(index, 1);
         };

         var populateMandatoryAttachments = function(choosedTrainees) {
             var requiredAttachments = entityObject.reimbursementTraineeAttachmentRequiredList;
             if (requiredAttachments && requiredAttachments["length"] > 0) {
                 var lastIndex = choosedTrainees.length - 1;
                 choosedTrainees[lastIndex].reimbursementTraineeAttachments = [];
                 for (var i = 0; i < requiredAttachments.length; i++) {
                     choosedTrainees[lastIndex].reimbursementTraineeAttachments.push(requiredAttachments[i]);
                 }
             }
         };

         var generateConfirmation = function() {
             var GENERATE_CONFIRMATION_URL = contextPath + "/rest/publicBenefits/reimbursements/v"
                 + entityObject.reimbursementDelivery.reimbursementPattern.id
                 + "/reimbursement/generateconfirmation";
             GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM).result.then(function(result) {
                 if (!result) {
                     return;
                 }
                 var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Zapisuję dane"});

                 $http.post(GENERATE_CONFIRMATION_URL, entityObject).then(function(response) {
                     GryfPopups.setPopup("success", "Sukces", "Poprawnie wygenerowano" +
                         " potwierdzenie dofinansowania");
                     loadReimbursement(response.data);
                 }, function(response) {
                     GryfPopups.setPopup("error", "Błąd", "Nie udało się wygenerować" +
                         " potwierdzenia dofinansowania.");
                     GryfPopups.showPopup();
                     GryfExceptionHandler.handleSavingError(response.data, violations, null);
                 }).finally(function() {
                     GryfModals.closeModal(modalInstance);
                 })
             });
         };

         return {
             loadReimbursement: loadReimbursement,
             getNewEntityObject: getNewEntityObject,
             getNewViolations: getNewViolations,
             openDeliveryLov: openDeliveryLov,
             loadVersionSpecificContent: loadVersionSpecificContent,
             openEnterpriseLov: openEnterpriseLov,
             save: save,
             correct: correct,
             verify: verify,
             settle: settle,
             complete: complete,
             cancel: cancel,
             getGrantOwnerAidProduct: getGrantOwnerAidProduct,
             createInitialReimbursement: createInitialReimbursement,
             getSex: getSex,
             getAttachmentTypes: getAttachmentTypes,
             removeItemFromList: removeItemFromList,
             populateMandatoryAttachments: populateMandatoryAttachments,
             generateConfirmation: generateConfirmation
         };
     }]);
