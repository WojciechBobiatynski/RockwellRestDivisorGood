angular.module('gryf.reimbursements').factory("BrowseDeliveriesService",
    ['$http', 'GryfModals', 'GryfHelpers', 'GryfTables', function($http, GryfModals, GryfHelpers, GryfTables) {
        ////////////////////////////////
        //CONSTANTS
        ////////////////////////////////
        var FIND_DELIVERY_URL = contextPath + "/rest/publicBenefits/reimbursements/deliveries";
        var FIND_DELIVERY_STATUSES_URL = contextPath + "/rest/publicBenefits/dictionaries/DELIVERY_STATUSES";

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
                statusId: null,
                enterpriseId: null,
                enterpriseName: null,
                vatRegNum: null,
                addressInvoice: null,
                zipCodeInvoiceCode: null,
                zipCodeInvoiceCity: null,
                applyDate: null,
                considerationDate: null,
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

        var getDeliveryStatuses = function() {
            return $http.get(FIND_DELIVERY_STATUSES_URL);
        };

        var find = function(findUrl) {
            var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING);
            if (!findUrl){
                findUrl = FIND_DELIVERY_URL;
            }

            GryfHelpers.transformDatesToString(searchDTO.entity);
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

        var findById = function(id) {
            searchDTO = new SearchDTO();
            searchDTO.entity.id = id;
            var promise = find();
            searchDTO = new SearchDTO();
            return promise
        };

        var getSearchResultOptions = function() {
            return searchResultOptions;
        };

        var getSearchDTO = function() {
            return searchDTO;
        };

        var loadMore = function() {
            searchDTO.entity.limit += searchResultOptions.displayLimitIncrementer;
            searchResultOptions.displayLimit += searchResultOptions.displayLimitIncrementer;
            return find();
        };

        var getNewSearchDTO = function() {
            searchDTO = new SearchDTO();
            return searchDTO;
        };

        var getNewSearchResultOptions = function() {
            searchResultOptions = new SearchResultOptions();
            return searchResultOptions;
        };

        var getSortedBy = function(sortColumnName) {
            GryfTables.sortByColumn(searchDTO.entity, sortColumnName);
            return find();
        };

        var getSortingTypeClass = function(entity, columnName) {
            return GryfTables.getSortingTypeClass(entity, columnName);
        };

        return {
            find: find,
            getDeliveryStatuses: getDeliveryStatuses,
            getSearchDTO: getSearchDTO,
            getSearchResultOptions: getSearchResultOptions,
            loadMore: loadMore,
            getNewSearchDTO: getNewSearchDTO,
            getNewSearchResultOptions: getNewSearchResultOptions,
            getSortedBy: getSortedBy,
            getSortingTypeClass: getSortingTypeClass,
            findById: findById
        }

    }]);


angular.module('gryf.reimbursements').factory("RegisterDeliveriesService",
    ['$http', 'GryfModals', 'GryfPopups', '$routeParams', 'GryfHelpers', 'BrowseTrainingInsService',
     'GryfExceptionHandler', '$location', '$route',
     function($http, GryfModals, GryfPopups, $routeParams, GryfHelpers, BrowseTrainingInsService,
              GryfExceptionHandler, $location, $route) {
         var DELIVERY_URL = contextPath + "/rest/publicBenefits/reimbursements/delivery/";
         var SAVE_DELIVERY_URL = contextPath + "/rest/publicBenefits/reimbursements/delivery/save";
         var SETTLE_DELIVERY_URL = contextPath + "/rest/publicBenefits/reimbursements/delivery/settle";
         var CANCEL_DELIVERY_URL = contextPath + "/rest/publicBenefits/reimbursements/delivery/cancel";
         var FIND_REIMBURSEMENT_PATTERNS_URL = contextPath + "/rest/publicBenefits/reimbursements/reimbursementPatternsDictionaries";

         var entityObject = new EntityObject();
         var violations = {};

         function EntityObject() {
             this.id = undefined;
             this.reimbursementPattern = undefined;
             this.trainingInstitution = undefined;
             this.deliveryAddress = undefined;
             this.deliveryZipCode = undefined;
             this.deliveryCityName = undefined;
             this.plannedReceiptDate = undefined;
             this.requestDate = undefined;
             this.deliveryDate = undefined;
             this.waybillNumber = undefined;
             this.status = {id: null};
             this.sodexoRealization = undefined;
             this.parentId = undefined;
             this.announcementDate = undefined;
             this.deliverySecondary = undefined;
             this.remarks = undefined;
             this.version = undefined;
         }


         var loadDelivery = function(idFromResponse) {
             GryfPopups.showPopup();
             if ($routeParams.id || idFromResponse) {
                 var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Wczytuję dane"});
                 var promise = $http.get(DELIVERY_URL + ($routeParams.id ? $routeParams.id : idFromResponse));
                 promise.then(function(response) {
                     GryfHelpers.copyPropertiesIfExist(entityObject, response.data);
                     if (entityObject.parentId) {
                         entityObject.deliverySecondary = true;
                     }
                 });
                 promise.finally(function() {
                     GryfModals.closeModal(modalInstance);
                 });
                 return promise;
             }
         };

         var settlementDelivery = function() {
             GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM).result.then(function(result) {
                 if (!result) {
                     return;
                 }
                 var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Zapisuję dane"});
                 $http.post(SETTLE_DELIVERY_URL, entityObject).then(function(response) {
                     GryfPopups.setPopup("success", "Sukces", "Dostawa poprawnie rozliczona");
                     loadDelivery(response.data);
                 }, function(response) {
                     GryfPopups.setPopup("error", "Błąd", "Nie udało się rozliczyć dostawy.");
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
                             settlementDelivery();
                         }
                     };
                     GryfExceptionHandler.handleSavingError(response.data, violations, conflictCallbacksObject);
                 }).finally(function() {
                     GryfModals.closeModal(modalInstance);
                 });
             });
         };

         var cancelDelivery = function() {
             GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM).result.then(function(result) {
                 if (!result) {
                     return;
                 }
                 var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Zapisuję dane"});
                 $http.post(CANCEL_DELIVERY_URL, entityObject).then(function(response) {
                     GryfPopups.setPopup("success", "Sukces", "Dostawa poprawnie anulowana");
                     loadDelivery(response.data);
                 }, function(response) {
                     GryfPopups.setPopup("error", "Błąd", "Nie udało się anulować dostawy.");
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
                             cancelDelivery();
                         }
                     };
                     GryfExceptionHandler.handleSavingError(response.data, violations, conflictCallbacksObject);
                 }).finally(function() {
                     GryfModals.closeModal(modalInstance);
                 });
             });
         };

         var save = function(acceptedViolationsPathList) {
             GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM).result.then(function(result) {
                 if (!result) {
                     return;
                 }

                 if (!acceptedViolationsPathList) {
                     acceptedViolationsPathList = [];
                 }
                 entityObject['acceptedViolations'] = acceptedViolationsPathList;
                 if (!entityObject.deliverySecondary) {
                     entityObject.deliverySecondary = undefined;
                 }
                 var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Zapisuję dane"});
                 $http.post(SAVE_DELIVERY_URL, entityObject).then(function(response) {
                     GryfPopups.setPopup("success", "Sukces", "Dostawa poprawnie zapisana");
                     loadDelivery(response.data);
                 }, function(response) {
                     GryfPopups.setPopup("error", "Błąd", "Nie udało się zapisać dostawy.");
                     GryfPopups.showPopup();
                     var conflictCallbacksObject;
                     if (response.data['responseType'] === GryfExceptionHandler.ERRORS.VALIDATION_WITH_CONFIRM_ERROR) {
                         conflictCallbacksObject = {
                             force: function(acceptedViolationsPathList) {
                                 save(acceptedViolationsPathList);
                             }
                         }
                     } else {
                         conflictCallbacksObject = {
                             refresh: function() {
                                 if (!$routeParams.id) {
                                     $location.search('id', response.data.id);
                                 }
                                 $route.reload();
                             },
                             force: function() {
                                 entityObject.version = response.data.version;
                                 save();
                             }
                         };
                     }
                     GryfExceptionHandler.handleSavingError(response.data, violations, conflictCallbacksObject);
                 }).finally(function() {
                     GryfModals.closeModal(modalInstance);
                 })
             });
         };


         var getEntityObject = function() {
             return entityObject;
         };

         var getNewEntityObject = function() {
             entityObject = new EntityObject();
             return entityObject;
         };

         var getViolations = function() {
             return violations;
         };

         var getNewViolations = function() {
             violations = {};
             return violations;
         };

         var getReimbursementPatterns = function() {
             return $http.get(FIND_REIMBURSEMENT_PATTERNS_URL);
         };

         var openTrainingInstitutionLov = function() {
             return GryfModals.openLovModal(GryfModals.MODALS_URL.LOV_TI, BrowseTrainingInsService, 'lg');
         };

         return {
             loadDelivery: loadDelivery,
             getEntityObject: getEntityObject,
             getNewEntityObject: getNewEntityObject,
             getViolations: getViolations,
             getReimbursementPatterns: getReimbursementPatterns,
             openTrainingInstitutionLov: openTrainingInstitutionLov,
             save: save,
             getNewViolations: getNewViolations,
             settlementDelivery: settlementDelivery,
             cancelDelivery: cancelDelivery
         }
     }]);

angular.module("gryf.reimbursements").directive("gryfDeliveryPrivilege", function() {
        return {
            restrict: 'A',
            require: '?ngModel',
            priority: 200,
            link: function(scope, elem, attr, ngModel) {
                var elemTagName = elem[0].tagName;
                var argArray = attr['gryfDeliveryPrivilege'].split(',');
                var flags = argArray.shift().trim();

                if (elemTagName === 'INPUT') {
                    scope.$watch(function() {
                        return ngModel.$modelValue;
                    }, function(modelValue) {
                        performInputCheck(modelValue);
                    });
                }

                if (elemTagName === 'A') {
                    for (var k = 0; k < argArray.length; k++) {
                        if (privileges[argArray[k].trim()]) {
                            return;
                        }
                    }
                    //since ng href is not resolved yet, add observe
                    elem.remove();
                }

                function performInputCheck(modelValue) {
                    //check wether it isn't disabled by another directive already
                    // eg ng-disabled
                    if (elem.prop('disabled')) {
                        return;
                    }

                    elem.prop('disabled', true);

                    for (var i = 0; i < argArray.length; i++) {
                        if (privileges[argArray[i].trim()]) {
                            elem.prop('disabled', false);
                            break;
                        }
                    }

                    //check whether it isn't disabled by privileges already
                    //if it is - we don't need to check on flags
                    if (elem.prop('disabled')) {
                        return;
                    }

                    elem.prop('disabled', true);

                    //insertable
                    if (flags.indexOf("I") > -1) {
                        if (modelValue == undefined) {
                            elem.prop('disabled', false);
                        }
                    }
                    //updatable
                    if (flags.indexOf("U") > -1) {
                        if (modelValue !== undefined) {
                            elem.prop('disabled', false);
                        }
                    }
                }
            }
        }
    }
);