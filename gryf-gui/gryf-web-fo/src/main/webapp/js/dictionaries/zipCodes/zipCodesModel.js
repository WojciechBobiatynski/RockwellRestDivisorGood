'use strict';

angular.module('gryf.dictionaries').factory('ZipCodesModel', ['$http', 'GryfModals', 'GryfTables', function($http, GryfModals, GryfTables) {
    var ZIP_CODES_URL = contextPath + "/rest/dictionaries/zipCodes";
    var MODAL_URL_WORKING = GryfModals.MODALS_URL.WORKING;

    var searchDTO = new SearchDTO();
    var searchResultOptions = new SearchResultOptions();

    // Model objects
    //////////////////////
    function SearchResultOptions() {
        this.displayLimit = 10;
        this.displayLimitIncrementer = 10;
        this.overflow = false;
        this.badQuery = false;
    }

    function SearchDTO() {
        this.statesArray = [];
        this.zipCodesArray = [];
        this.entity = {
            zipCode: null,
            cityName: null,
            active: true,
            stateId: null,
            limit: 10,
            sortColumns: [],
            sortTypes: []
        }
    }

    //------------------

    // Public functions
    //////////////////////
    var findStates = function() {
        return $http.get(contextPath + "/rest/dictionaries/states");
    };

    var isResultsOverflow = function(zipCodesArray) {
        return zipCodesArray.length > searchResultOptions.displayLimit;
    };

    var find = function() {
        var modalInstance = GryfModals.openModal(MODAL_URL_WORKING);
        var promise = $http.get(ZIP_CODES_URL, {params: searchDTO.entity});
        promise.then(function(response) {
            //success
            searchDTO.zipCodesArray = response.data;
            searchResultOptions.overflow = isResultsOverflow(response.data);
        }, function() {
            //error
            getNewSearchDTO();
            findStates();
            getNewSearchResultOptions();
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

    var setSort = function(sortColumn) {
        GryfTables.sortByColumn(searchDTO.entity, sortColumn);
    };

    var loadMore = function() {
        searchDTO.entity.limit += searchResultOptions.displayLimitIncrementer;
        searchResultOptions.displayLimit += searchResultOptions.displayLimitIncrementer;
        return find();
    };

    //public API
    return {
        find: find,
        getSearchDTO: getSearchDTO,
        getNewSearchDTO: getNewSearchDTO,
        getSearchResultOptions: getSearchResultOptions,
        getNewSearchResultOptions: getNewSearchResultOptions,
        isResultsOverflow: isResultsOverflow,
        findStates: findStates,
        setSort: setSort,
        loadMore: loadMore
    };
}]);


angular.module('gryf.dictionaries').factory('ZipCodeService',
    ['$http', '$routeParams', 'GryfModals', 'GryfExceptionHandler', 'GryfPopups',
     function($http, $routeParams, GryfModals, GryfExceptionHandler, GryfPopups) {
         var ZIP_CODE_URL = contextPath + "/rest/dictionaries/zipCode/";
         var MODAL_URL_WORKING = GryfModals.MODALS_URL.WORKING;

         var model = new DataModel();
         var violations = {};

         function DataModel() {
             this.statesArray = [];
             this.entity = {
                 zipCode: null,
                 cityName: null,
                 active: true,
                 state: null,
                 version: null
             };
         }

         var findStates = function() {
             return $http.get(contextPath + "/rest/dictionaries/states");
         };

         var loadZipCode = function() {
             var modalInstance = GryfModals.openModal(MODAL_URL_WORKING, {label: "Wczytuję dane"});
             var promise = $http.get(ZIP_CODE_URL + ($routeParams.id ? $routeParams.id : ""));
             promise.finally(function() {
                 GryfModals.closeModal(modalInstance);
             });
             return promise;
         };

         var saveZipCode = function(zipCodeToSave) {
             var modalInstance = GryfModals.openModal(MODAL_URL_WORKING, {label: "Zapisuję dane"});

             var promise;
             if (zipCodeToSave.id) {
                 promise = $http.put(ZIP_CODE_URL + $routeParams.id, zipCodeToSave);
             } else {
                 promise = $http.post(ZIP_CODE_URL, zipCodeToSave);
             }

             promise.then(function() {
                 model = getModel();
                 GryfPopups.setPopup("success", "Sukces", "Kod pocztowy poprawnie zapisany");
             });

             promise.error(function(error) {
                 GryfPopups.setPopup("error", "Błąd", "Nie udało się zapisać kodu pocztowego");
                 GryfPopups.showPopup();

                 var conflictCallbacksObject = {
                     refresh: function() {
                         loadZipCode();
                     },
                     force: function() {
                         model.entity.version = error.version;
                         saveZipCode(model.entity).then(function() {
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

         var getModel = function() {
             return model;
         };

         var getNewModel = function() {
             model = new DataModel();
             return model;
         };

         var getViolations = function() {
             return violations;
         };

         var getNewViolations = function() {
             violations = {};
             return violations;
         };

         return {
             findStates: findStates,
             getModel: getModel,
             getNewModel: getNewModel,
             loadZipCode: loadZipCode,
             saveZipCode: saveZipCode,
             getViolations: getViolations,
             getNewViolations: getNewViolations
         }
     }]);