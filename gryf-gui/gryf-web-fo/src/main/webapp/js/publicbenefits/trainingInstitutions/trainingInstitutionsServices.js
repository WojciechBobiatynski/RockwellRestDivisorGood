"use strict";

angular.module("gryf.trainingInstitutions").factory("BrowseTrainingInsService",
    ["$http", "GryfModals", "GryfTables", function($http, GryfModals, GryfTables) {
        var FIND_TRAINING_INS_URL = contextPath + "/rest/publicBenefits/trainingInstitution/list";
        var searchDTO = new SearchObjModel();
        var searchResultOptions = new SearchResultOptions();

        function SearchObjModel() {
            this.searchResultList = [];
            this.entity = {
                id: null,
                name: null,
                vatRegNum: null,
                addressInvoice: null,
                zipCodeInvoiceCode: null,
                zipCodeInvoiceCity: null,
                addressCorr: null,
                zipCodeCorrCode: null,
                zipCodeCorrCity: null,

                limit: 10,
                sortColumns: [],
                sortTypes: []
            }
        }

        function SearchResultOptions() {
            this.displayLimit = 10;
            this.displayLimitIncrementer = 10;
            this.overflow = false;
            this.badQuery = false;
        }

        var isResultsOverflow = function(zipCodesArray) {
            return zipCodesArray.length > searchResultOptions.displayLimit;
        };

        var find = function(restUrl) {
            var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING);
            if (!restUrl) {
                restUrl = FIND_TRAINING_INS_URL;
            }
            var promise = $http.get(restUrl, {params: searchDTO.entity});
            promise.then(function(response) {
                //success
                searchDTO.searchResultList = response.data;
                searchResultOptions.overflow = isResultsOverflow(response.data);
            }, function() {
                //error
                resetSearchResultOptions();
                searchResultOptions.badQuery = true;
            });

            promise.finally(function() {
                GryfModals.closeModal(modalInstance);
            });
            return promise;
        };

        var findById = function(id) {
            searchDTO = new SearchObjModel;
            searchDTO.entity.id = id;
            return find();
        };

        var getNewSearchDTO = function() {
            searchDTO = new SearchObjModel;
            return searchDTO;
        };

        var resetSearchResultOptions = function() {
            searchResultOptions = new SearchResultOptions;
            return searchResultOptions;
        };

        var getSearchDTO = function() {
            return searchDTO;
        };

        var getSearchResultOptions = function() {
            return searchResultOptions;
        };

        var findSortedBy = function(sortColumnName) {
            GryfTables.sortByColumn(searchDTO.entity, sortColumnName);
            return find();
        };

        var loadMore = function() {
            searchDTO.entity.limit += searchResultOptions.displayLimitIncrementer;
            searchResultOptions.displayLimit += searchResultOptions.displayLimitIncrementer;
            return find();
        };

        return {
            getSearchDTO: getSearchDTO,
            getSearchResultOptions: getSearchResultOptions,
            find: find,
            findSortedBy: findSortedBy,
            resetSearchResultOptions: resetSearchResultOptions,
            getNewSearchDTO: getNewSearchDTO,
            loadMore: loadMore,
            findById: findById
        }
    }]);


angular.module("gryf.trainingInstitutions").factory("ModifyTrainingInsService",
    ["$http", "$routeParams", "GryfModals", "GryfExceptionHandler", "ZipCodesModel",
     'GryfPopups', function($http, $routeParams, GryfModals, GryfExceptionHandler, ZipCodesModel, GryfPopups) {
        var TRAINING_INS_URL = contextPath + "/rest/publicBenefits/trainingInstitution/";
        var RESET_LINK_PATH = "reset";

        var PATH_SECURITY = "/security";
        var PATH_TI_USER_ROLES = "/ti/roles";

        var violations = {};
        var trainingInsObject = new TrainingInsObject();

        function TrainingInsObject() {
            this.contactTypes = [];
            this.tiUserRoles = [];
            this.entity = {
                id: null,
                code: null,
                accountPayment: null,
                name: null,
                vatRegNum: null,
                addressInvoice: null,
                zipCodeInvoice: null,
                addressCorr: null,
                zipCodeCorr: null,
                remarks: null,
                contacts: [],
                users: []
            };
        }

        var loadContactTypes = function() {
            var promise = $http.get(contextPath + "/rest/publicBenefits/contactTypes");
            promise.then(function(response) {
                trainingInsObject.contactTypes = response.data;
            });
            return promise;
        };

        var load = function(responseId) {
            if ($routeParams.id || responseId) {
                var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Wczytuję dane"});
                var promise = $http.get(TRAINING_INS_URL + ($routeParams.id ? $routeParams.id : responseId));
                promise.then(function(response) {
                    trainingInsObject.entity = response.data;
                });
                promise.finally(function() {
                    GryfModals.closeModal(modalInstance);
                });
                return promise;
            }
        };


        var save = function(additionalParam) {
            var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Zapisuję dane"});

            var promise;
            if (trainingInsObject.entity.id) {
                promise = $http.put(TRAINING_INS_URL + trainingInsObject.entity.id, trainingInsObject.entity, {params: additionalParam});
            } else {
                promise = $http.post(TRAINING_INS_URL, trainingInsObject.entity, {params: additionalParam});
            }

            promise.then(function() {
                GryfPopups.setPopup("success", "Sukces", "IS poprawnie zapisane");
            });

            promise.error(function(error) {
                GryfPopups.setPopup("error", "Błąd", "Nie udało się zapisać IS");
                GryfPopups.showPopup();

                var conflictCallbacksObject;
                if (error.responseType === GryfExceptionHandler.ERRORS.VAT_REG_NUM_TRAINING_INSTITUTION_CONFLICT) {
                    conflictCallbacksObject = {
                        refresh: function() {
                            load();
                        },
                        force: function() {
                            save({checkVatRegNumDup: false}).then(function() {
                                GryfPopups.showPopup();
                            });
                        }
                    };
                } else {
                    conflictCallbacksObject = {
                        refresh: function() {
                            load();
                        },
                        force: function() {
                            trainingInsObject.entity.version = error.version;
                            save().then(function() {
                                GryfPopups.showPopup();
                            });
                        }
                    };
                }

                GryfExceptionHandler.handleSavingError(error, violations, conflictCallbacksObject);
            });

            promise.finally(function() {
                GryfModals.closeModal(modalInstance);
            });

            return promise;
        };

        var openZipCodesLov = function() {
            var TEMPLATE_URL = GryfModals.MODALS_URL.LOV_ZIPCODES;
            return GryfModals.openLovModal(TEMPLATE_URL, ZipCodesModel);
        };

        var getViolations = function() {
            return violations;
        };

        var getNewViolations = function() {
            violations = {};
            return violations;
        };

        var getModel = function() {
            return trainingInsObject;
        };

        var getNewModel = function() {
            trainingInsObject = new TrainingInsObject();
            return trainingInsObject;
        };

        var addContact = function() {
            trainingInsObject.entity.contacts.push({});
        };

        var removeContact = function(val) {
            var index = trainingInsObject.entity.contacts.indexOf(val);
            trainingInsObject.entity.contacts.splice(index, 1);
        };

        var addTiUserToList = function() {
            trainingInsObject.entity.users.push({});
        };

        var sendResetLink = function(user) {
            var promise = $http.post(TRAINING_INS_URL + RESET_LINK_PATH + '/', user);
            promise.then(function (response) {
                GryfPopups.setPopup("success", "Sukces", "Widomość email z linkiem do resetu hasła została wysłana");
                GryfPopups.showPopup();
            });
            promise.error(function (error) {
                GryfPopups.setPopup("error", "Błąd", "Nie udało się wysłać wiadomości email");
                GryfPopups.showPopup();
            });
        };

        var loadTiUserRoles = function() {
            var promise = $http.get(contextPath + PATH_SECURITY + PATH_TI_USER_ROLES);
            promise.then(function(response) {
                trainingInsObject.tiUserRoles = response.data;
            });
            return promise;
        };

        return {
            load: load,
            loadContactTypes: loadContactTypes,
            save: save,
            getViolations: getViolations,
            getNewViolations: getNewViolations,
            getModel: getModel,
            getNewModel: getNewModel,
            addContact: addContact,
            removeContact: removeContact,
            openZipCodesLov: openZipCodesLov,
            addTiUserToList: addTiUserToList,
            sendResetLink: sendResetLink,
            loadTiUserRoles: loadTiUserRoles
        }
    }]);