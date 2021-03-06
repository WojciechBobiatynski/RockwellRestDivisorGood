"use strict";

angular.module("gryf.trainingInstitutions").factory("BrowseTrainingInsService",
    ["$http", "GryfModals", "GryfTables", function($http, GryfModals, GryfTables) {
        var FIND_TRAINING_INS_URL = contextPath + "/rest/publicBenefits/trainingInstitution/list";
        var PATH_USER_TI = contextPath + "/rest/publicBenefits/fo/userTi";
        var PATH_RESET_USER_TI = contextPath + "/rest/publicBenefits/fo/resetTi";

        var searchDTO = new SearchObjModel();
        var loggedUserTI = new TrainingInstitutionSearchModel();
        var searchResultOptions = new SearchResultOptions();

        function SearchObjModel() {
            this.searchResultList = [];
            this.entity = {
                id: null,
                externalId: null,
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

        function TrainingInstitutionSearchModel() {
            this.entity = {
                id: null,
                externalId: null,
                name: null,
                vatRegNum: null,
                addressInvoice: null,
                zipCodeInvoiceCode: null,
                zipCodeInvoiceCity: null,
                addressCorr: null,
                zipCodeCorrCode: null,
                zipCodeCorrCity: null
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

        var getLoggedUserTI = function () {
            return loggedUserTI;
        };

        var loadLoggedUserTI = function () {
            var promise = $http.get(PATH_USER_TI);
            promise.then(function (response) {
                if (response.data) {
                    loggedUserTI.entity = response.data;
                }
            })
        };

        var resetLoggedUserTI = function () {
            var promise = $http.put(PATH_RESET_USER_TI);
            promise.then(function (response) {
                loadLoggedUserTI();
            },function (error) {
                if (error.status === 404) {
                    GryfModals.openModal(GryfModals.MODALS_URL.INVALID_OBJECT_ID,
                        {message: error.data.message});
                }
            });
            return promise;
        };

        return {
            getSearchDTO: getSearchDTO,
            getSearchResultOptions: getSearchResultOptions,
            find: find,
            findSortedBy: findSortedBy,
            resetSearchResultOptions: resetSearchResultOptions,
            getNewSearchDTO: getNewSearchDTO,
            loadMore: loadMore,
            findById: findById,
            getLoggedUserTI: getLoggedUserTI,
            loadLoggedUserTI: loadLoggedUserTI,
            resetLoggedUserTI: resetLoggedUserTI
        }
    }]);


angular.module("gryf.trainingInstitutions").factory("ModifyTrainingInsService",
    ["$http", "$routeParams", "GryfModals", "GryfExceptionHandler", "ZipCodesModel",
     'GryfPopups', function($http, $routeParams, GryfModals, GryfExceptionHandler, ZipCodesModel, GryfPopups) {
        var TRAINING_INS_URL = contextPath + "/rest/publicBenefits/trainingInstitution/";
        var RESET_LINK_PATH = "reset";

        var PATH_SECURITY = "/security";
        var PATH_TI_USER_ROLES = "/ti/roles";

        var PATH_JOIN_TI = contextPath + "/rest/publicBenefits/fo/joinTi/";

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
                var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Wczytuj?? dane"});
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
            var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Zapisuj?? dane"});

            var promise;
            if (trainingInsObject.entity.id) {
                promise = $http.put(TRAINING_INS_URL + trainingInsObject.entity.id, trainingInsObject.entity, {params: additionalParam});
            } else {
                promise = $http.post(TRAINING_INS_URL, trainingInsObject.entity, {params: additionalParam});
            }

            promise.then(function() {
                GryfPopups.setPopup("success", "Sukces", "IS poprawnie zapisane");
                GryfPopups.showPopup();
            });

            promise.error(function(error) {
                GryfPopups.setPopup("error", "B????d", "Nie uda??o si?? zapisa?? IS");
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
                GryfPopups.setPopup("success", "Sukces", "Widomo???? email z linkiem do resetu has??a zosta??a wys??ana");
                GryfPopups.showPopup();
            });
            promise.error(function (error) {
                GryfPopups.setPopup("error", "B????d", "Nie uda??o si?? wys??a?? wiadomo??ci email");
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

        var joinTi = function (id) {
            if (!($routeParams.id || id)) {
                return;
            }
            var requestId = $routeParams.id ? $routeParams.id : id;
            var promise = $http.put(PATH_JOIN_TI + requestId);
            promise.then(function (response) {
                if (response.data) {
                    var filteredUsers = trainingInsObject.entity.users.filter(function (user) {
                        return user.id === response.data.id;
                    });
                    if (filteredUsers.length === 0) {
                        trainingInsObject.entity.users.push(response.data);
                        GryfPopups.setPopup("success", "Sukces", "Do????czy??e?? do IS.");
                        GryfPopups.showPopup();
                    }
                }
            }, function (error) {
                if (error.status === 404) {
                    GryfModals.openModal(GryfModals.MODALS_URL.INVALID_OBJECT_ID,
                        {message: error.data.message});
                }
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
            loadTiUserRoles: loadTiUserRoles,
            joinTi: joinTi
        }
    }]);
