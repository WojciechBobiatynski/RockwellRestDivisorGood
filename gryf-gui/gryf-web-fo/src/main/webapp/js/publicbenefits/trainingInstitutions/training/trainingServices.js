"use strict";

angular.module("gryf.training").factory("BrowseTrainingService",
    ["$http", "GryfModals", "GryfHelpers", "GryfTables", "BrowseTrainingInsService", function($http, GryfModals, GryfHelpers, GryfTables, BrowseTrainingInsService) {
        var FIND_TRAINING_INS_URL = contextPath + "/rest/publicBenefits/training/list";
        var GET_TRAINING_CATEGORY_DICT = contextPath + "/rest/publicBenefits/training/getTrainingCategoriesDict";

        var searchDTO = new SearchObjModel();
        var searchResultOptions = new SearchResultOptions();

        function SearchObjModel() {
            this.searchResultList = [];
            this.entity = {
                trainingId: null,
                externalTrainingId: null,
                institutionId: null,
                institutionName: null,
                name: null,
                priceFrom: null,
                priceTo: null,
                startDate: null,
                endDate: null,
                place: null,
                hoursNumberFrom: null,
                hoursNumberTo: null,
                hourPriceFrom: null,
                hourPriceTo: null,
                categoryCodes: null,

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

        var getSearchDTO = function() {
            return searchDTO;
        };

        var getSearchResultOptions = function() {
            return searchResultOptions;
        };

        var getNewSearchDTO = function() {
            searchDTO = new SearchObjModel();
            return searchDTO;
        };

        var getNewSearchResultOptions = function() {
            searchResultOptions = new SearchResultOptions();
            return searchResultOptions;
        };

        var isResultsOverflow = function(result) {
            return result.length > searchResultOptions.displayLimit;
        };

        var find = function(restUrl) {
            var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING);
            GryfHelpers.transformDatesToString(searchDTO.entity);
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

        var findSortedBy = function(sortColumnName) {
            GryfTables.sortByColumn(searchDTO.entity, sortColumnName);
            return find();
        };

        var getSortingTypeClass = function(entity, columnName) {
            return GryfTables.getSortingTypeClass(entity, columnName);
        };

        var getTrainingCategoriesDict = function() {
            return $http.get(GET_TRAINING_CATEGORY_DICT).then(function(response) {
                return response.data;
            });
        };

        var openTrainingInstitutionLov = function() {
            return GryfModals.openLovModal(GryfModals.MODALS_URL.LOV_TI, BrowseTrainingInsService, 'lg');
        };

        var resetSearchResultOptions = function() {
            searchResultOptions = new SearchResultOptions;
            return searchResultOptions;
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
            getTrainingCategoriesDict: getTrainingCategoriesDict,
            getNewSearchDTO: getNewSearchDTO,
            getNewSearchResultOptions: getNewSearchResultOptions,
            getSortingTypeClass: getSortingTypeClass,
            openTrainingInstitutionLov: openTrainingInstitutionLov,
            loadMore: loadMore
        };
    }]);


angular.module("gryf.training").factory("ModifyTrainingService",
    ["$http", "$routeParams", "GryfModals", "GryfExceptionHandler", "ZipCodesModel", "GryfPopups", "BrowseTrainingInsService",
        function($http, $routeParams, GryfModals, GryfExceptionHandler, ZipCodesModel, GryfPopups, BrowseTrainingInsService) {

        var TRAINING_URL = contextPath + "/rest/publicBenefits/training/";
        var GET_TRAINING_CATEGORY_CATALOGS_URL = contextPath + "/rest/publicBenefits/training/categoryCatalogs";
        var GET_TRAINING_CATEGORY_IN_CATALOG = contextPath + "/rest/publicBenefits/training/getTrainingCategoriesInCatalog";

        var trainingObject = new TrainingObject();
        var categoryCatalogs = new CategoryCatalog();
        var trainingCategory = new TrainingCategory();
        var violations = {};

        var getNewModel = function() {
            trainingObject = new TrainingObject();
            return trainingObject;
        };

        var getViolations = function() {
            return violations;
        };

        var getNewViolations = function() {
            violations = {};
            return violations;
        };

        var getNewCategoryCatalogs = function () {
            categoryCatalogs = new CategoryCatalog();
            return categoryCatalogs;
        }

        var getNewTrainingCategory = function () {
            trainingCategory = new TrainingCategory();
            return trainingCategory;
        }

        function TrainingObject() {
            this.entity = {
                trainingInstitution: null,
                institutionName: null,
                name: null,
                price: null,
                startDate: null,
                endDate: null,
                place: null,
                hoursNumber: null,
                hourPrice: null,
                category: null,
                trainingCategoryCatalogId: null,
                reimbursmentConditions: null
            }
        }

        function CategoryCatalog() {
            this.list = [];
        }

        function TrainingCategory() {
            this.list = [];
        }

        var save = function(additionalParam) {
            var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Zapisuję dane"});

            var promise;
            if (trainingObject.entity.trainingId) {
                promise = $http.put(TRAINING_URL + trainingObject.entity.trainingId, trainingObject.entity, {params: additionalParam});
            } else {
                promise = $http.post(TRAINING_URL, trainingObject.entity, {params: additionalParam});
            }

            promise.then(function() {
                GryfPopups.setPopup("success", "Sukces", "Szkolenie poprawnie zapisane");
                GryfPopups.showPopup();
            });

            promise.error(function(error) {
                GryfPopups.setPopup("error", "Błąd", "Nie udało się zapisać szkolenia");
                GryfPopups.showPopup();

                var conflictCallbacksObject = {
                    refresh: function() {
                        load();
                    },
                    force: function() {
                        trainingObject.entity.version = error.version;
                        save().then(function() {
                            GryfPopups.showPopup();
                        });
                    }
                };

                GryfExceptionHandler.handleSavingError(error, violations, conflictCallbacksObject);
            });

            promise.finally(function() {
                GryfModals.closeModal(modalInstance);
            });

            return promise;
        };

        var findById = function(trainingId) {
            if (trainingId) {
                var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Wczytuję dane"});
                var promise = $http.get(TRAINING_URL + trainingId);
                promise.then(function(response) {
                    trainingObject.entity = response.data;
                    if(trainingObject.entity.trainingCategoryCatalogId) {
                        loadTrainingCategoriesByCatalogId(trainingObject.entity.trainingCategoryCatalogId)
                    }
                });
                promise.finally(function() {
                    GryfModals.closeModal(modalInstance);
                });
                return promise;
            }
        };

        var loadTrainingCategoryCatalogs = function() {
            var promise = $http.get(GET_TRAINING_CATEGORY_CATALOGS_URL);
            promise.then(function(response) {
                categoryCatalogs.list = response.data;
                if(categoryCatalogs.list.length == 1) {
                    var catalogId = categoryCatalogs.list[0].id;
                    loadTrainingCategoriesByCatalogId(catalogId);
                }
            });
            return promise;
        };

        var loadTrainingCategoriesByCatalogId = function(catalogId) {
            var promise;
            promise =  $http.post(GET_TRAINING_CATEGORY_IN_CATALOG + '/'+ catalogId);
            promise.then(function(response) {
                trainingCategory.list = response.data;
            });
        };

        var openTrainingInstitutionLov = function() {
            return GryfModals.openLovModal(GryfModals.MODALS_URL.LOV_TI, BrowseTrainingInsService, 'lg');
        };

        var load = function(responseId) {
            if ($routeParams.id || responseId) {
                var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Wczytuję dane"});
                var promise = $http.get(TRAINING_URL + ($routeParams.id ? $routeParams.id : responseId));
                promise.then(function(response) {
                    trainingObject.entity = response.data;
                });
                promise.finally(function() {
                    GryfModals.closeModal(modalInstance);
                });
                return promise;
            }
        };

        return {
            findById: findById,
            getNewModel: getNewModel,
            getNewTrainingCategory: getNewTrainingCategory,
            getNewCategoryCatalogs:getNewCategoryCatalogs,
            loadTrainingCategoryCatalogs: loadTrainingCategoryCatalogs,
            loadTrainingCategoriesByCatalogId: loadTrainingCategoriesByCatalogId,
            getViolation: getViolations,
            getNewViolations: getNewViolations,
            save: save,
            openTrainingInstitutionLov: openTrainingInstitutionLov
        };
    }]);