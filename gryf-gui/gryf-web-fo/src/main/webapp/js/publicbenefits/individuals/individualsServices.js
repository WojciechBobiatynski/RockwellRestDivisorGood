"use strict";

angular.module("gryf.individuals").factory("BrowseIndividualsService",
    ["$http", "GryfModals", "GryfTables", function ($http, GryfModals, GryfTables) {
        var FIND_INDIVIDUALS_URL = contextPath + "/rest/publicBenefits/individual/list";
        var searchDTO = new SearchObjModel();
        var searchResultOptions = new SearchResultOptions();

        function SearchObjModel() {
            this.searchResultList = [];
            this.entity = {
                id: null,
                firstName: null,
                lastName: null,
                pesel: null,
                documentNumber: null,
                documentType: null,
                sex: null,
                addressInvoice: null,
                zipCodeInvoiceCode: null,
                zipCodeInvoiceCity: null,
                addressCorr: null,
                zipCodeCorrCode: null,
                zipCodeCorrCity: null,
                enterprise: null,

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

        var isResultsOverflow = function (zipCodesArray) {
            return zipCodesArray.length > searchResultOptions.displayLimit;
        };

        var find = function (restUrl) {
            var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING);
            if (!restUrl) {
                restUrl = FIND_INDIVIDUALS_URL;
            }
            var promise = $http.get(restUrl, {params: searchDTO.entity});
            promise.then(function (response) {
                //success
                searchDTO.searchResultList = response.data;
                searchResultOptions.overflow = isResultsOverflow(response.data);
            }, function () {
                //error
                resetSearchResultOptions();
                searchResultOptions.badQuery = true;
            });

            promise.finally(function () {
                GryfModals.closeModal(modalInstance);
            });
            return promise;
        };

        var findById = function (id) {
            getNewSearchDTO();
            searchDTO.entity.id = id;
            return $http.get(FIND_INDIVIDUALS_URL, {params: searchDTO.entity});
        };

        var getNewSearchDTO = function () {
            searchDTO = new SearchObjModel;
            return searchDTO;
        };

        var resetSearchResultOptions = function () {
            searchResultOptions = new SearchResultOptions;
            return searchResultOptions;
        };

        var getSearchDTO = function () {
            return searchDTO;
        };

        var getSearchResultOptions = function () {
            return searchResultOptions;
        };

        var findSortedBy = function (sortColumnName) {
            GryfTables.sortByColumn(searchDTO.entity, sortColumnName);
            return find();
        };

        var loadMore = function () {
            searchDTO.entity.limit += searchResultOptions.displayLimitIncrementer;
            searchResultOptions.displayLimit += searchResultOptions.displayLimitIncrementer;
            return find();
        };

        return {
            getSearchDTO: getSearchDTO,
            getSearchResultOptions: getSearchResultOptions,
            find: find,
            findById: findById,
            findSortedBy: findSortedBy,
            resetSearchResultOptions: resetSearchResultOptions,
            getNewSearchDTO: getNewSearchDTO,
            loadMore: loadMore
        }
    }]);


angular.module("gryf.individuals").factory("ModifyIndividualsService",
    ["$http", "$routeParams", "GryfModals", "GryfExceptionHandler", "ZipCodesModel", "GryfPopups", "BrowseEnterprisesService",
        '$location', '$route',
        function ($http, $routeParams, GryfModals, GryfExceptionHandler, ZipCodesModel, GryfPopups, BrowseEnterprisesService,
                  $location, $route) {
            var INDIVIDUAL_URL = contextPath + "/rest/publicBenefits/individual/";

            var ENTITY_TYPE_DICT = "ENT_ENTITY_TYPES";
            var ENT_SIZE_TYPES = "ENT_SIZE_TYPES";

            var VER_CODE_GENERATE_PATH = "verification/generate/";
            var VER_CODE_SEND_PATH = "verification/send";

            var FIND_ENTITY_TYPES_URL = contextPath + "/rest/publicBenefits/dictionaries/" + ENTITY_TYPE_DICT;
            var FIND_ENT_SIZE_TYPES_URL = contextPath + "/rest/publicBenefits/dictionaries/" + ENT_SIZE_TYPES;

            var violations = {};
            var individualObject = new IndividualObject();

            var dictionaries = {
                entityTypes: [],
                enterpriseSizes: []
            };

            function IndividualObject() {
                this.contactTypes = [];
                this.entity = {
                    id: null,
                    code: null,
                    accountPayment: null,
                    accountRepayment: null,
                    firstName: null,
                    lastName: null,
                    pesel: null,
                    documentNumber: null,
                    documentType: null,
                    sex: null,
                    addressInvoice: null,
                    zipCodeInvoice: null,
                    addressCorr: null,
                    zipCodeCorr: null,
                    remarks: null,
                    verificationCode: null,
                    lastLoginDate: null,
                    enterprises: [],
                    contacts: []
                };
            };

            var openEnterpriseLov = function () {
                var TEMPLATE_URL = GryfModals.MODALS_URL.LOV_ENTERPRISES;
                return GryfModals.openLovModal(TEMPLATE_URL, BrowseEnterprisesService, "lg");
            };

            var getEntityTypes = function () {
                $http.get(FIND_ENTITY_TYPES_URL).then(function (response) {
                    dictionaries.entityTypes = response.data;
                });
            };

            var getEnterpriseSizes = function () {
                $http.get(FIND_ENT_SIZE_TYPES_URL).then(function (response) {
                    dictionaries.enterpriseSizes = response.data;
                });
            };

            var getDictionaries = function () {
                getEntityTypes();
                getEnterpriseSizes();

                return dictionaries;
            };

            var loadContactTypes = function () {
                var promise = $http.get(contextPath + "/rest/publicBenefits/contactTypes");
                promise.then(function (response) {
                    individualObject.contactTypes = response.data;
                });
                return promise;
            };

            var loadIndividuals = function (responseId) {
                if ($routeParams.id || responseId) {
                    var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Wczytuję dane"});
                    var promise = $http.get(INDIVIDUAL_URL + ($routeParams.id ? $routeParams.id : responseId));
                    promise.then(function (response) {
                        individualObject.entity = response.data;
                    });
                    promise.finally(function () {
                        GryfModals.closeModal(modalInstance);
                    });
                    return promise;
                }
            };


            var save = function (additionalParam) {
                var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Zapisuję dane"});

                var promise;
                if (individualObject.entity.id) {
                    promise = $http.put(INDIVIDUAL_URL + individualObject.entity.id, individualObject.entity, {params: additionalParam});
                } else {
                    promise = $http.post(INDIVIDUAL_URL, individualObject.entity, {params: additionalParam});
                }

                promise.then(function () {
                    GryfPopups.setPopup("success", "Sukces", "Osoba fizyczna poprawnie zapisana");
                });

                promise.error(function (error) {
                    GryfPopups.setPopup("error", "Błąd", "Nie udało się zapisać Osoby fizycznej");
                    GryfPopups.showPopup();

                    var conflictCallbacksObject;
                    if (error.responseType === GryfExceptionHandler.ERRORS.PESEL_INDIVIDUAL_CONFLICT) {
                        conflictCallbacksObject = {
                            refresh: function () {
                                if (!$routeParams.id) {
                                    $location.search('id', error.id);
                                }
                                $route.reload();
                            },
                            force: function () {
                                save({checkPeselDup: false}).then(function () {
                                    GryfPopups.showPopup();
                                });
                            }
                        };
                    } else {
                        conflictCallbacksObject = {
                            refresh: function () {
                                if (!$routeParams.id) {
                                    $location.search('id', error.id);
                                }
                                $route.reload();
                            },
                            force: function () {
                                individualObject.entity.version = error.version;
                                save().then(function () {
                                    GryfPopups.showPopup();
                                });
                            }
                        };
                    }

                    GryfExceptionHandler.handleSavingError(error, violations, conflictCallbacksObject);
                });

                promise.finally(function () {
                    GryfModals.closeModal(modalInstance);
                });

                return promise;
            };

            var openZipCodesLov = function () {
                var TEMPLATE_URL = GryfModals.MODALS_URL.LOV_ZIPCODES;
                return GryfModals.openLovModal(TEMPLATE_URL, ZipCodesModel);
            };

            var getViolations = function () {
                return violations;
            };

            var getNewViolations = function () {
                violations = {};
                return violations;
            };

            var getModel = function () {
                return individualObject;
            };

            var getNewModel = function () {
                individualObject = new IndividualObject();
                return individualObject;
            };

            var addContact = function () {
                individualObject.entity.contacts.push({});
            };

            var removeContact = function (val) {
                var index = individualObject.entity.contacts.indexOf(val);
                individualObject.entity.contacts.splice(index, 1);
            };

            var getNewVerificationCode = function () {
                var promise = $http.get(INDIVIDUAL_URL + VER_CODE_GENERATE_PATH + individualObject.entity.id );
                promise.then(function (response) {
                    individualObject.entity.verificationCode = response.data;
                    GryfPopups.setPopup("success", "Sukces", "Udało się zmienić kod weryfikacyjny");
                    GryfPopups.showPopup();
                });
            };

            var sendMailWithVerCode = function () {
                var promise = $http.post(INDIVIDUAL_URL + VER_CODE_SEND_PATH, individualObject.entity);
                promise.then(function (response) {
                    GryfPopups.setPopup("success", "Sukces", "Widomość email została wysłana");
                    GryfPopups.showPopup();
                });
                promise.error(function (error) {
                    GryfPopups.setPopup("error", "Błąd", "Nie udało się wysłać wiadomości email");
                    GryfPopups.showPopup();
                });
            };

            return {
                loadIndividuals: loadIndividuals,
                loadContactTypes: loadContactTypes,
                save: save,
                getViolations: getViolations,
                getNewViolations: getNewViolations,
                getModel: getModel,
                getNewModel: getNewModel,
                addItemToList: addContact,
                removeItemFromList: removeContact,
                openZipCodesLov: openZipCodesLov,
                openEnterpriseLov: openEnterpriseLov,
                getDictionaries: getDictionaries,
                getNewVerificationCode: getNewVerificationCode,
                sendMailWithVerCode: sendMailWithVerCode
            };
        }]);