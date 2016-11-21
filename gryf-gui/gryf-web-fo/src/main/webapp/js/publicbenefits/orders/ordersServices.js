"use strict";

angular.module('gryf.orders').factory("BrowseOrdersService",
    ['$http', 'GryfModals', 'GryfHelpers', function($http, GryfModals, GryfHelpers) {
        ////////////////////////////////
        //CONSTANTS
        ////////////////////////////////
        var FIND_ORDERS_URL = contextPath + "/rest/publicBenefits/order/list";
        var FIND_ORDER_STATUSES_URL = contextPath + "/rest/publicBenefits/dictionaries/ORDER_FLOW_STATUSES";
        var FIND_GRANT_PROGRAM_NAMES_URL = contextPath + "/rest/publicBenefits/grantPrograms";


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


        var find = function(restUrl) {
            var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING);

            GryfHelpers.transformDatesToString(searchDTO.entity);
            if (!restUrl){
                restUrl = FIND_ORDERS_URL;
            }
            var promise = $http.get(restUrl, {params: searchDTO.entity});
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

        var getNewSearchResultOptions = function() {
            searchResultOptions = new SearchResultOptions();
            return searchResultOptions;
        };

        var getSearchResultOptions = function() {
            return searchResultOptions;
        };

        var getOrderStatuses = function() {
            return $http.get(FIND_ORDER_STATUSES_URL);
        };

        var getGrantProgramNames = function () {
            return $http.get(FIND_GRANT_PROGRAM_NAMES_URL);
        }

        var loadMore = function() {
            searchDTO.entity.limit += searchResultOptions.displayLimitIncrementer;
            searchResultOptions.displayLimit += searchResultOptions.displayLimitIncrementer;
            return find();
        };

        return {
            find: find,
            getSearchDTO: getSearchDTO,
            getSearchResultOptions: getSearchResultOptions,
            getOrderStatuses: getOrderStatuses,
            loadMore: loadMore,
            getNewSearchDTO: getNewSearchDTO,
            getNewSearchResultOptions: getNewSearchResultOptions,
            getGrantProgramNames: getGrantProgramNames
        }
    }]);

angular.module('gryf.orders').factory("ModifyOrdersService",
    ['$http', 'GryfModals', '$routeParams', 'GryfPopups', 'angularLoad', '$q', 'GryfExceptionHandler',
     'Upload', 'GryfHelpers', '$location', '$route','BrowseContractsService',
     function($http, GryfModals, $routeParams, GryfPopups, angularLoad, $q, GryfExceptionHandler,
              Upload, GryfHelpers, $location, $route, BrowseContractsService) {
         var ORDER_URL = contextPath + "/rest/publicBenefits/order/modify/";
         var TEMPLATE_SPECIFIC_FOLDER = contextPath + "/";
         var ORDER_EXECUTE_ACTION_URL = contextPath + "/rest/publicBenefits/order/action/";

         var entityObject = new EntityObject();
         var violations = {};

         var orderStatus = {
             loaded: false
         };

        function EntityObject() {
            //OrderDTO
            this.id = null;
            this.elements = [];
            this.actions = [];
            this.version = null;
        }

        var getOrderStatus = function() {
            return orderStatus;
        };

        var loadOrder = function(idFromResponse) {
            orderStatus.loaded = false;
            GryfPopups.showPopup();
            if ($routeParams.id || idFromResponse) {
                var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Wczytuję dane"});
                var promise = $http.get(ORDER_URL + ($routeParams.id ? $routeParams.id : idFromResponse));
                promise.then(function(response) {
                    GryfHelpers.copyPropertiesIfExist(entityObject, response.data);
                    GryfHelpers.parseDateFromTimestamp(entityObject.elements);
                    var promisesArray = loadElementSpecificScripts();
                    $q.all(promisesArray).then(function() {
                        orderStatus.loaded = true;
                    }).catch(function() {
                        console.log("Can't load some of the order element JS files");
                    });
                });
                promise.finally(function() {
                    GryfModals.closeModal(modalInstance);
                });
                return promise;
            }
        };

         var openContractLov = function () {
             var TEMPLATE_URL = GryfModals.MODALS_URL.LOV_CONTRACTS;
             return GryfModals.openLovModal(TEMPLATE_URL, BrowseContractsService, "lg");
         };


         var fastSave = function(contractId) {
                 var promise = $http.post(contextPath + "/rest/publicBenefits/order/fastSave/" + contractId);
                 promise.then(function(response) {
                     window.location = contextPath + "/publicBenefits/orders/#/modify/" + response.data;
                 });
                 promise.finally(function() {
                     GryfModals.closeModal(modalInstance);
                 });
                 return promise;
         };

         var executeAction = function(actionId, orderId, acceptedViolationsPathParam) {
             var files = findAllFileAttachments();
             var incomingOrderElementsParam = prepareElementsToSend();
             if (!acceptedViolationsPathParam) {
                 acceptedViolationsPathParam = [];
             }
             var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Zapisuję dane"});
             Upload.upload({
                 url: ORDER_EXECUTE_ACTION_URL + orderId + "/" + actionId + "/" + entityObject.version,
                 file: files,
                 fields: {
                     'incomingOrderElementsParam': incomingOrderElementsParam,
                     'acceptedViolationsParam': acceptedViolationsPathParam
                 }
             }).success(function(id) {
                 GryfPopups.setPopup("success", "Sukces", "Zamówienie poprawnie zapisane");
                 loadOrder(id);
             }).error(function(response) {
                 GryfPopups.setPopup("error", "Błąd", "Nie udało się zapisać zamówienia.");
                 GryfPopups.showPopup();

                 var conflictCallbacksObject;
                 if (response.responseType === GryfExceptionHandler.ERRORS.VALIDATION_WITH_CONFIRM_ERROR) {
                     conflictCallbacksObject = {
                         force: function(acceptedViolationsPathList) {
                             executeAction(actionId, orderId, acceptedViolationsPathList);
                         }
                     }
                 } else {
                     conflictCallbacksObject = {
                         refresh: function() {
                             if (!$routeParams.id) {
                                 $location.search('id', response.id);
                             }
                             $route.reload();
                         },
                         force: function() {
                             entityObject.version = response.version;
                             executeAction(actionId, orderId);
                         }
                     };
                 }

                 GryfExceptionHandler.handleSavingError(response, violations, conflictCallbacksObject);
             }).finally(function() {
                 GryfModals.closeModal(modalInstance);
             });
         };

         function prepareElementsToSend() {
             var elements = entityObject.elements;
             var toSend = [];
             for (var i = 0; i<elements.length; i++){
                 toSend.push({
                     elementTypeComponentName: elements[i].elementTypeComponentName,
                     data: angular.toJson(elements[i])
                 })
             }
             return toSend;
         }

         function findAllFileAttachments() {
             var resultArray = [];
             for (var i = 0; i < entityObject.elements.length; i++) {
                 var attachmentField = entityObject.elements[i].file;
                 if (attachmentField) {
                     if (attachmentField[0]) {
                         resultArray.push(attachmentField[0]);
                         entityObject.elements[i].fileIncluded = true;
                     }
                 }
             }
             return resultArray;
         }

        function loadElementSpecificScripts() {
            var promisesArray = [];
            for (var i = 0; i < entityObject.elements.length; i++) {
                var MODEL_FOLDER_PATH = contextPath + "/js/publicbenefits/orders/elements/";
                var fileName = MODEL_FOLDER_PATH + (entityObject.elements[i])["elementTypeComponentName"] + ".js";
                var promise = angularLoad.loadScript(fileName);
                promisesArray.push(promise);
            }
            return promisesArray;
        }

        var getEntityObject = function() {
            return entityObject;
        };

         var getViolations = function() {
             return violations;
         };

         var getNewViolations = function() {
             violations = {};
             return violations;
         };

        return {
            loadOrder: loadOrder,
            getEntityObject: getEntityObject,
            getOrderStatus: getOrderStatus,
            executeAction: executeAction,
            getViolations: getViolations,
            getNewViolations: getNewViolations,
            openContractLov: openContractLov,
            fastSave: fastSave
        };
    }]);

angular.module('gryf.orders').factory("PreviewOrdersService",
    ['$http', 'GryfModals', '$routeParams', 'GryfPopups', 'angularLoad', '$q', 'GryfExceptionHandler',
     'GryfHelpers',
     function($http, GryfModals, $routeParams, GryfPopups, angularLoad, $q, GryfExceptionHandler, GryfHelpers) {
         var ORDER_URL = contextPath + "/rest/publicBenefits/order/preview/";

         var entityObject = new EntityObject();

         var orderStatus = {
             loaded: false
         };

         function EntityObject() {
             //OrderDTO
             this.id = null;
             this.elements = [];
         }

         var loadOrder = function() {
             orderStatus.loaded = false;
             GryfPopups.showPopup();
             if ($routeParams.id) {
                 var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Wczytuję dane"});
                 var promise = $http.get(ORDER_URL + $routeParams.id);
                 promise.then(function(response) {
                     GryfHelpers.copyPropertiesIfExist(entityObject, response.data);
                     GryfHelpers.parseDateFromTimestamp(entityObject.elements);
                     var promisesArray = loadElementSpecificScripts();
                     $q.all(promisesArray).then(function() {
                         orderStatus.loaded = true;
                     }).catch(function() {
                         console.log("Can't load some of the order element JS files");
                     });
                 });
                 promise.finally(function() {
                     GryfModals.closeModal(modalInstance);
                 });
                 return promise;
             }
         };

         function loadElementSpecificScripts() {
             var promisesArray = [];
             for (var i = 0; i < entityObject.elements.length; i++) {
                 var MODEL_FOLDER_PATH = contextPath + "/js/publicbenefits/orders/elements/";
                 var fileName = MODEL_FOLDER_PATH + (entityObject.elements[i])["elementTypeComponentName"] + ".js";
                 var promise = angularLoad.loadScript(fileName);
                 promisesArray.push(promise);
             }
             return promisesArray;
         }

         var getEntityObject = function() {
             return entityObject;
         };

         var getOrderStatus = function() {
             return orderStatus;
         };

         return {
             loadOrder: loadOrder,
             getEntityObject: getEntityObject,
             getOrderStatus: getOrderStatus
         }

     }]);