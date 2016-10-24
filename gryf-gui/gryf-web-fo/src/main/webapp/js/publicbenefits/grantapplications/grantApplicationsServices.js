"use strict";

angular.module('gryf.grantApplications').factory('BrowseGrantApplicationsService',
    ['$http', "GryfModals", "GryfTables", 'GryfHelpers', function($http, GryfModals, GryfTables, GryfHelpers) {
        ////////////////////////////////
        //CONSTANTS
        ////////////////////////////////
        var FIND_APPLICATIONS_URL = contextPath + "/rest/publicBenefits/grantapplication/list";


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
                applyDateFrom: null,
                applyDateTo: null,
                considerationDateFrom: null,
                considerationDateTo: null,
                sortTypes: [],
                sortColumns: [],
                limit: resultLimit["grantApplications"]
            }
        }

        function SearchResultOptions() {
            this.displayLimit = resultLimit["grantApplications"];
            this.displayLimitIncrementer = resultLimit["grantApplications"];
            this.overflow = false;
            this.badQuery = false;
        }


        ////////////////////////////////
        //LOGIC
        ////////////////////////////////
        var find = function(restUrl) {
            var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING);
            GryfHelpers.transformDatesToString(searchDTO.entity);
            if (!restUrl) {
                restUrl = FIND_APPLICATIONS_URL;
            }
            var promise = $http.get(restUrl, {params: searchDTO.entity});
            promise.then(function(response) {
                //success
                searchDTO.searchResultList = response.data;
                searchResultOptions.overflow = response.data.length > searchResultOptions.displayLimit;
            }, function() {
                //error
                getNewSearchResultOptions();
                searchResultOptions.badQuery = true;
            });

            promise.finally(function() {
                GryfModals.closeModal(modalInstance);
            });
            return promise;
        };

        var loadMore = function() {
            searchDTO.entity.limit += searchResultOptions.displayLimitIncrementer;
            searchResultOptions.displayLimit += searchResultOptions.displayLimitIncrementer;
            return find();
        };

        var getSortedBy = function(sortColumnName) {
            GryfTables.sortByColumn(searchDTO.entity, sortColumnName);
            return find();
        };

        var getSortingTypeClass = function(entity, columnName) {
            return GryfTables.getSortingTypeClass(entity, columnName);
        };


        ////////////////////////////////
        //GETTERS
        ////////////////////////////////

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

        return {
            find: find,
            loadMore: loadMore,
            getSortedBy: getSortedBy,
            getSortingTypeClass: getSortingTypeClass,
            getSearchDTO: getSearchDTO,
            getNewSearchDTO: getNewSearchDTO,
            getSearchResultOptions: getSearchResultOptions,
            getNewSearchResultOptions: getNewSearchResultOptions
        }
    }]);


angular.module('gryf.grantApplications').factory('ModifyGrantApplicationService',
    ['$http', 'GryfModals', 'BrowseEnterprisesService', 'ZipCodesModel', '$routeParams',
     'GryfExceptionHandler', 'Upload', 'Dictionaries', 'angularLoad', '$injector', 'GryfPopups', '$route',
     'GryfHelpers',
     function($http, GryfModals, BrowseEnterprisesService, ZipCodesModel, $routeParams,
              GryfExceptionHandler, Upload, Dictionaries, angularLoad, $injector, GryfPopups, $route,
              GryfHelpers) {

         var APPLICATION_URL = contextPath + "/rest/publicBenefits/grantapplication/";
         var APPLICATION_SAVE_URL = contextPath + "/rest/publicBenefits/grantapplication/save/";
         var APPLICATION_UPDATE_URL = contextPath + "/rest/publicBenefits/grantapplication/update/";
         var APPLICATION_APPLY_URL = contextPath + "/rest/publicBenefits/grantapplication/apply/";
         var APPLICATION_EXECUTE_URL = contextPath + "/rest/publicBenefits/grantapplication/execute/";
         var APPLICATION_REJECT_URL = contextPath + "/rest/publicBenefits/grantapplication/reject/";

         var ENTITY_TYPE_DICT = "ENT_ENTITY_TYPES";
         var ENT_SIZE_TYPES = "ENT_SIZE_TYPES";

         var FIND_ENTITY_TYPES_URL = contextPath + "/rest/publicBenefits/dictionaries/" + ENTITY_TYPE_DICT;
         var FIND_ENT_SIZE_TYPES_URL = contextPath + "/rest/publicBenefits/dictionaries/" + ENT_SIZE_TYPES;

         var VERSION_MODEL_FOLDER = contextPath + "/js/publicbenefits/grantapplications/versions/";


         var entityObject = new EntityObject();
         var violations = {};

         var dictionaries = {
             entityTypes: [],
             enterpriseSizes: []
         };

         function EntityObject() {
             //ApplicationDTO
             this.grantProgram = null;
             this.id = null;
             this.applicationVersion = {
                 id: null,
                 name: null,
                 templateName: "default",
                 attachmentRequirementList: null
             };
             this.enterprise = null;
             this.status = {id: null};
             this.rejectionReason = null;
             this.applyDate = null;
             this.considerationDate = null;
             this.version = null;
             this.attachments = [];
             this.receiptDate = null;
         }


         var getEntityObject = function() {
             entityObject = new EntityObject();
             return entityObject;
         };

         var openEnterpriseLov = function() {
             var TEMPLATE_URL = GryfModals.MODALS_URL.LOV_ENTERPRISES;
             return GryfModals.openLovModal(TEMPLATE_URL, BrowseEnterprisesService, "lg");
         };

         var openZipCodesLov = function() {
             var TEMPLATE_URL = GryfModals.MODALS_URL.LOV_ZIPCODES;
             return GryfModals.openLovModal(TEMPLATE_URL, ZipCodesModel);
         };

         var getEntityTypes = function() {
             $http.get(FIND_ENTITY_TYPES_URL).then(function(response) {
                 dictionaries.entityTypes = response.data;
             });
         };

         var getEnterpriseSizes = function() {
             $http.get(FIND_ENT_SIZE_TYPES_URL).then(function(response) {
                 dictionaries.enterpriseSizes = response.data;
             });
         };

         var getDictionaries = function() {
             getEntityTypes();
             getEnterpriseSizes();

             return dictionaries;
         };

         var removeItemFromList = function(itemToDelete, list) {
             var index = list.indexOf(itemToDelete);
             list.splice(index, 1);
         };

         var addItemToList = function(list) {
             list.push({});
         };

         var loadVersionSpecificContent = function() {
             var templateName = entityObject.applicationVersion.templateName;
             if (templateName && templateName != "default") {
                 var promise = angularLoad.loadScript(VERSION_MODEL_FOLDER + templateName + ".js");
                 promise.then(function() {
                     resetVersionSpecificDTO();
                 });
                 return promise;
             }
         };

         var loadApplication = function(responseId) {
             GryfPopups.showPopup();
             if ($routeParams.id || responseId) {
                 var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Wczytuję dane"});
                 var promise = $http.get(APPLICATION_URL + ($routeParams.id ? $routeParams.id : responseId));
                 promise.then(function(response) {
                     copyPropertiesIfExist(entityObject, response.data);
                 });
                 promise.finally(function() {
                     GryfModals.closeModal(modalInstance);
                 });
                 return promise;
             }
         };

         var save = function() {
             if (!entityObject.applicationVersion.id) {
                 GryfModals.openModal(GryfModals.MODALS_URL.ERROR_INFO, {
                     message: "Musisz wybrać wersję wniosku by móc go zapisać"
                 });
                 return;
             }

             GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM).result.then(function(result) {
                 if (!result) {
                     return;
                 }

                 var files = findAllFileAttachments();
                 var mixinString = flatEntitiesToString();

                 var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Zapisuję dane"});
                 var sendSaveRequest = function(url) {
                     Upload.upload({
                         url: url,
                         data: mixinString,
                         file: files
                     }).success(function(id) {
                         GryfPopups.setPopup("success", "Sukces", "Wniosek poprawnie zapisany w" +
                             " statusie 'Nowy'");
                         loadApplication(id);
                     }).error(function(data) {
                         GryfPopups.setPopup("error", "Błąd", "Nie udało się zapisać wniosku.");
                         GryfPopups.showPopup();
                         var conflictCallbacksObject = {
                             refresh: function() {
                                 loadApplication();
                             },
                             force: function() {
                                 entityObject.version = data.version;
                                 save();
                             }
                         };
                         GryfExceptionHandler.handleSavingError(data, violations, conflictCallbacksObject);
                     }).finally(function() {
                         GryfModals.closeModal(modalInstance);
                     });
                 };

                 if (entityObject.id) {
                     sendSaveRequest(APPLICATION_UPDATE_URL + entityObject.applicationVersion.id + '/' + entityObject.id);
                     return;
                 }
                 sendSaveRequest(APPLICATION_SAVE_URL + entityObject.applicationVersion.id);
             });
         };

         var apply = function(checkVatRegNumDup, acceptedViolationsPathList) {
             if (!checkVatRegNumDup) {
                 checkVatRegNumDup = true;
             }

             if (!entityObject.applicationVersion.id) {
                 GryfModals.openModal(GryfModals.MODALS_URL.ERROR_INFO, {
                     message: "Musisz wybrać wersję wniosku by móc go zapisać"
                 });
                 return;
             }

             if (!acceptedViolationsPathList) {
                 acceptedViolationsPathList = [];
             }

             GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM).result.then(function(result) {
                 if (!result) {
                     return;
                 }

                 var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Zapisuję dane"});
                 var files = findAllFileAttachments();
                 var mixinString = flatEntitiesToString();


                 var sendApplyRequest = function(url, method) {
                     Upload.upload({
                         url: url,
                         data: mixinString,
                         file: files,
                         fields: {
                             'acceptedViolationsParam': acceptedViolationsPathList
                         },
                         method: method
                     }).success(function(id) {
                         GryfPopups.setPopup("success", "Sukces", "Wniosek poprawnie zapisany w" +
                             " statusie 'Złożony'");
                         loadApplication(id);
                     }).error(function(error) {
                         GryfPopups.setPopup("error", "Błąd", "Nie udało się zapisać wniosku w" +
                             " statusie 'Złożony'.");
                         GryfPopups.showPopup();

                         var conflictCallbacksObject;
                         if (error.responseType === GryfExceptionHandler.ERRORS.VALIDATION_WITH_CONFIRM_ERROR) {
                             conflictCallbacksObject = {
                                 force: function(acceptedViolationsPathList) {
                                     apply(undefined, acceptedViolationsPathList);
                                 }
                             }
                         } else {
                             conflictCallbacksObject = {
                                 refresh: function() {
                                     loadApplication();
                                 },
                                 force: function() {
                                     entityObject.version = data.version;
                                     apply();
                                 }
                             };
                         }

                         GryfExceptionHandler.handleSavingError(error, violations, conflictCallbacksObject);
                     }).finally(function() {
                         GryfModals.closeModal(modalInstance);
                     });
                 };

                 if (entityObject.id) {
                     sendApplyRequest(APPLICATION_APPLY_URL + entityObject.applicationVersion.id
                         + '/' + entityObject.id, "POST");
                     return;
                 }
                 sendApplyRequest(APPLICATION_APPLY_URL + entityObject.applicationVersion.id, "POST");
             });
         };

         var execute = function(checkVatRegNumDup) {
             if (checkVatRegNumDup == undefined) {
                 checkVatRegNumDup = true;
             }

             GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM).result.then(function(result) {
                 if (!result) {
                     return;
                 }

                 var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Zapisuję dane"});
                 var files = findAllFileAttachments();
                 var mixinString = flatEntitiesToString();

                 var sendExecuteRequest = function(url, method) {
                     Upload.upload({
                         url: url,
                         data: mixinString,
                         file: files,
                         fields: {
                             'checkVatRegNumDup': checkVatRegNumDup
                         },
                         method: method
                     }).success(function() {
                         GryfPopups.setPopup("success", "Sukces", "Wniosek poprawnie zapisany w" +
                             " statusie 'Realizowany'");
                         $route.reload();
                     }).error(function(error) {
                         GryfPopups.setPopup("error", "Błąd", "Nie udało się zapisać wniosku w" +
                             " statusie 'Realizowany'.");
                         GryfPopups.showPopup();

                         var conflictCallbacksObject;
                         if (error.responseType === GryfExceptionHandler.ERRORS.VAT_REG_NUM_ENTERPRISE_CONFLICT) {
                             conflictCallbacksObject = {
                                 refresh: function() {
                                     loadApplication();
                                 },
                                 force: function() {
                                     execute(false);
                                 }
                             };
                         } else {
                             conflictCallbacksObject = {
                                 refresh: function() {
                                     loadApplication();
                                 },
                                 force: function() {
                                     entityObject.version = data.version;
                                     apply();
                                 }
                             };
                         }

                         GryfExceptionHandler.handleSavingError(error, violations, conflictCallbacksObject);
                     }).finally(function() {
                         GryfModals.closeModal(modalInstance);
                     });
                 };

                 sendExecuteRequest(APPLICATION_EXECUTE_URL + entityObject.id, "POST");

             });
         };

         var reject = function() {
             GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM).result.then(function(result) {
                 if (!result) {
                     return;
                 }

                 var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Zapisuję dane"});
                 var files = findAllFileAttachments();
                 var mixinString = flatEntitiesToString();

                 var sendRejectRequest = function(url, method) {
                     Upload.upload({
                         url: url,
                         data: mixinString,
                         file: files,
                         method: method
                     }).success(function() {
                         GryfPopups.setPopup("success", "Sukces", "Wniosek poprawnie zapisany w" +
                             " statusie 'Odrzucony'");
                         $route.reload();
                     }).error(function(error) {
                         GryfPopups.setPopup("error", "Błąd", "Nie udało się zapisać wniosku w" +
                             " statusie 'Odrzucony'.");
                         GryfPopups.showPopup();

                         var conflictCallbacksObject = {
                             refresh: function() {
                                 loadApplication();
                             },
                             force: function() {
                                 entityObject.version = data.version;
                                 reject();
                             }
                         };

                         GryfExceptionHandler.handleSavingError(error, violations, conflictCallbacksObject);
                     }).finally(function() {
                         GryfModals.closeModal(modalInstance);
                     });
                 };

                 sendRejectRequest(APPLICATION_REJECT_URL + entityObject.id, "POST");

             });
         };

         var getViolations = function() {
             return violations;
         };

         var getNewViolations = function() {
             violations = {};
             return violations;
         };

         var addMandatoryAttachments = function() {
             if (entityObject.id) {
                 return;
             }
             var attachmentRequirementList = entityObject.applicationVersion.attachmentRequirementList;
             if (attachmentRequirementList) {
                 for (var i = 0; i < attachmentRequirementList.length; i++) {
                     var item = attachmentRequirementList[i];
                     entityObject.attachments.push({
                         name: item.name,
                         remarks: item.remarks,
                         mandatory: true
                     });
                 }
             }
         };

         var populateVersionSpecificModel = function(data) {
             var versionSpecificDTO = getVersionSpecificDTO();
             copyPropertiesIfExist(versionSpecificDTO, data);
         };


         function flatEntitiesToString() {
             var versionSpecificDTO = getVersionSpecificDTO();
             extend(entityObject, versionSpecificDTO);
             return angular.toJson(entityObject);
         }

         function validateAttachmentsSize(files, attachmentParent) {
             for (var i = 0; i < files.length; i++) {
                 if (files[i].size > Number(attachmentMaxSize)) {
                     attachmentParent.file.pop();
                     GryfModals.openModal(GryfModals.MODALS_URL.ERROR_INFO, {
                         message: "Plik jest zbyt duży"
                     });
                 }
             }
         }

         function findAllFileAttachments() {
             var resultArray = [];
             for (var i = 0; i < entityObject.attachments.length; i++) {
                 var attachmentFileField = entityObject.attachments[i].file;
                 if (attachmentFileField) {
                     if (attachmentFileField.length) {
                         resultArray.push(entityObject.attachments[i].file[0]);
                         entityObject.attachments[i].fileIncluded = true;
                     }
                 }
             }
             return resultArray;
         }


         ////////////////////////////////
         // PRIVATE FUNCTIONS
         ///////////////////////////////
         function extend(destination, source) {
             for (var k in source) {
                 if (source.hasOwnProperty(k)) {
                     destination[k] = source[k];
                 }
             }
             return destination;
         }

         /**
          * Copy properties from source to destination
          * only when destination has property with given name
          * @param destination object
          * @param source object
          */
         function copyPropertiesIfExist(destination, source) {
             for (var k in source) {
                 if (source.hasOwnProperty(k) && destination.hasOwnProperty(k)) {
                     destination[k] = source[k];
                 }
             }
             return destination;
         }

         function getVersionSpecificDTO() {
             var templateName = entityObject.applicationVersion.templateName;
             try {
                 var ModelService = $injector.get(templateName + "Service");
             } catch (error) {
                 console.log("can't find Service with name: " + templateName + "Service");
                 return {};
             }
             return ModelService.getEntityVersionObject();
         }

         function resetVersionSpecificDTO() {
             var templateName = entityObject.applicationVersion.templateName;
             try {
                 var ModelService = $injector.get(templateName + "Service");
             } catch (error) {
                 console.log("can't find Service with name: " + templateName + "Service")
                 return {};
             }
             return ModelService.getNewEntityVersionObject();
         }

         return {
             getEntityObject: getEntityObject,
             openEnterpriseLov: openEnterpriseLov,
             getDictionaries: getDictionaries,
             openZipCodesLov: openZipCodesLov,
             removeItemFromList: removeItemFromList,
             addItemToList: addItemToList,
             save: save,
             apply: apply,
             execute: execute,
             reject: reject,
             loadApplication: loadApplication,
             getViolations: getViolations,
             getNewViolations: getNewViolations,
             flatEntitiesToString: flatEntitiesToString,
             addMandatoryAttachments: addMandatoryAttachments,
             validateAttachmentsSize: validateAttachmentsSize,
             loadVersionSpecificContent: loadVersionSpecificContent,
             populateVersionSpecificModel: populateVersionSpecificModel
         }
     }]);


angular.module('gryf.grantApplications').factory('Dictionaries', ['$http', function($http) {
    var FIND_STATUSES_URL = contextPath + "/rest/publicBenefits/dictionaries/GR_APPLICATION_STATUSES";

    var getStatuses = function() {
        return $http.get(FIND_STATUSES_URL);
    };

    var STATUSES = {
        "NEW": {
            id: "NEW",
            name: "Nowy"
        },
        "APPLIED": {
            id: "APPLIED",
            name: "Złożony"
        },
        "EXECUTED": {
            id: "EXECUTED",
            name: "Realizowany"
        },
        "REJECTED": {
            id: "REJECTED",
            name: "Odrzucony"
        }
    };

    return {
        getStatuses: getStatuses,
        STATUSES: STATUSES
    }
}]);

angular.module('gryf.grantApplications').directive('gryfEditableOnlyInNew',
    ['Dictionaries', function(Dictionaries) {
        return {
            restrict: 'A',
            scope: false,
            link: function(scope, element, attr) {
                var privilegesAttrs = attr["gryfEditableOnlyInNew"].split(",");
                var currentStatus = scope.entityObject.status.id;

                //TODO naprawic dla istniejących statusów - nie wykonuje sie poprawnie (currenStatus undefined)
                //propozycja - dodac watcha na statusie i wykonywac ponizszy kod/
                if (currentStatus) {
                    for (var i = 0; i < privilegesAttrs.length; i++) {
                        if (privileges[privilegesAttrs[i]]) {
                            if (currentStatus === Dictionaries.STATUSES.NEW.id) {
                                return;
                            }
                        }
                    }
                    if (element[0].tagName === "INPUT") {
                        attr.$set("readonly", "true");
                    } else if (element[0].tagName === "A") {
                        element.addClass("disabledLink");
                    } else {
                        attr.$set("disabled", "true");
                    }
                }
            }
        }
    }]);

angular.module("gryf.grantApplications").directive('gryfEditableInApplied',
    ['Dictionaries', function(Dictionaries) {
        return {
            restrict: 'A',
            scope: false,
            link: function(scope, element, attr) {
                var privilegesAttrs = attr["gryfEditableInApplied"].split(",");
                var currentStatus = scope.entityObject.status.id;
                for (var i = 0; i < privilegesAttrs.length; i++) {
                    if (privileges[privilegesAttrs[i]]) {
                        if (currentStatus === Dictionaries.STATUSES.APPLIED.id) {
                            attr.$set("readonly", "");
                            attr.$set("disabled", "");
                            element.removeClass("disabledLink");
                            return;
                        }
                    }
                }

            }
        }
    }]);

angular.module("gryf.grantApplications").directive('gryfSearchOnEnter',
    ['BrowseEnterprisesService', function(BrowseEnterprisesService) {
        return {
            restrict: 'A',
            link: function(scope, element, attr) {
                element.bind("keydown keypress", function(event) {
                    if (event.which === 13) {
                        scope.$apply(function() {
                            BrowseEnterprisesService.findById(scope.entityObject.enterprise.id).then(function(response) {
                                var item = response.data.shift();
                                if (item) {
                                    scope.entityObject.enterprise = item;
                                    scope.$broadcast('propagateEnterpriseData', item);
                                }
                            });
                        });
                        event.preventDefault();
                    }
                })
            }
        }
    }]);

