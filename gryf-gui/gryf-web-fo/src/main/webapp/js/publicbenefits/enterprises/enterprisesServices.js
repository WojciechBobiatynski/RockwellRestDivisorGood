"use strict";

angular.module("gryf.enterprises").factory("BrowseEnterprisesService",
    ["$http", "GryfModals", "GryfTables", function($http, GryfModals, GryfTables) {
        var FIND_ENTERPRISES_URL = contextPath + "/rest/publicBenefits/enterprise/list";
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
            if (!restUrl){
                restUrl = FIND_ENTERPRISES_URL;
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
            getNewSearchDTO();
            searchDTO.entity.id = id;
            return $http.get(FIND_ENTERPRISES_URL, {params: searchDTO.entity});
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
            findById: findById,
            findSortedBy: findSortedBy,
            resetSearchResultOptions: resetSearchResultOptions,
            getNewSearchDTO: getNewSearchDTO,
            loadMore: loadMore
        }
    }]);


angular.module("gryf.enterprises").factory("ModifyEnterprisesService",
    ["$http", "$routeParams", "GryfModals", "GryfExceptionHandler", "ZipCodesModel", "GryfPopups",
     '$location', '$route',
     function($http, $routeParams, GryfModals, GryfExceptionHandler, ZipCodesModel, GryfPopups,
              $location, $route) {
        var ENTERPRISE_URL = contextPath + "/rest/publicBenefits/enterprise/";

        var violations = {};
        var enterpriseObject = new EnterpriseObject();

        function EnterpriseObject() {
            this.contactTypes = [];
            this.entity = {
                id: null,
                code: null,
                accountPayment: null,
                accountRepayment: null,
                name: null,
                vatRegNum: null,
                addressInvoice: null,
                zipCodeInvoice: null,
                addressCorr: null,
                zipCodeCorr: null,
                remarks: null,
                contacts: []
            }
        }

        var loadContactTypes = function() {
            var promise = $http.get(contextPath + "/rest/publicBenefits/contactTypes");
            promise.then(function(response) {
                enterpriseObject.contactTypes = response.data;
            });
            return promise;
        };

        var loadEnterprises = function(responseId) {
            if ($routeParams.id || responseId) {
                var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Wczytuję dane"});
                var promise = $http.get(ENTERPRISE_URL + ($routeParams.id ? $routeParams.id : responseId));
                promise.then(function(response) {
                    enterpriseObject.entity = response.data;
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
            if (enterpriseObject.entity.id) {
                promise = $http.put(ENTERPRISE_URL + enterpriseObject.entity.id, enterpriseObject.entity, {params: additionalParam});
            } else {
                promise = $http.post(ENTERPRISE_URL, enterpriseObject.entity, {params: additionalParam});
            }

            promise.then(function() {
                GryfPopups.setPopup("success", "Sukces", "MŚP poprawnie zapisane");
                GryfPopups.showPopup();
            });

            promise.error(function(error) {
                GryfPopups.setPopup("error", "Błąd", "Nie udało się zapisać MŚP");
                GryfPopups.showPopup();

                var conflictCallbacksObject;
                if (error.responseType === GryfExceptionHandler.ERRORS.VAT_REG_NUM_ENTERPRISE_CONFLICT) {
                    conflictCallbacksObject = {
                        refresh: function() {
                            if (!$routeParams.id) {
                                $location.search('id', error.id);
                            }
                            $route.reload();
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
                            if (!$routeParams.id) {
                                $location.search('id', error.id);
                            }
                            $route.reload();
                        },
                        force: function() {
                            enterpriseObject.entity.version = error.version;
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
            return enterpriseObject;
        };

        var getNewModel = function() {
            enterpriseObject = new EnterpriseObject();
            return enterpriseObject;
        };

        var addContact = function() {
            enterpriseObject.entity.contacts.push({});
        };

        var removeContact = function(val) {
            var index = enterpriseObject.entity.contacts.indexOf(val);
            enterpriseObject.entity.contacts.splice(index, 1);
        };


        return {
            loadEnterprises: loadEnterprises,
            loadContactTypes: loadContactTypes,
            save: save,
            getViolations: getViolations,
            getNewViolations: getNewViolations,
            getModel: getModel,
            getNewModel: getNewModel,
            addItemToList: addContact,
            removeItemFromList: removeContact,
            openZipCodesLov: openZipCodesLov
        }
    }]);