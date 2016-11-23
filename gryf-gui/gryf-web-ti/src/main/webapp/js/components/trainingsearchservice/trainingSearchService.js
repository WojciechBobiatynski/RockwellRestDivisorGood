angular.module("gryf.ti").factory("TrainingSearchService", function($http, GryfModals, GryfTables, GryfHelpers, GryfPopups) {
    var FIND_TRAINING_URL = contextPath + "/rest/trainingreservation/training/list";
    var FIND_SINGLE_TRAINING_URL = contextPath + "/rest/trainingreservation/training/";

    var searchDTO = new SearchObjModel();
    var searchResultOptions = new SearchResultOptions();

    function SearchObjModel() {
        this.searchResultList = [];
        this.entity = {
            trainingId: null,
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
            restUrl = FIND_TRAINING_URL;
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

    var findDetailsById = function(trainingId) {
        return $http.get(FIND_SINGLE_TRAINING_URL + trainingId).error(function() {
            GryfPopups.setPopup("error", "Błąd", "Nie można pobrać szkolenia o wskazanym id");
            GryfPopups.showPopup();
        });
    }

    var findSortedBy = function(sortColumnName) {
        GryfTables.sortByColumn(searchDTO.entity, sortColumnName);
        return find();
    };

    var getSortingTypeClass = function(entity, columnName) {
        return GryfTables.getSortingTypeClass(entity, columnName);
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
        getNewSearchDTO: getNewSearchDTO,
        getSearchDTO: getSearchDTO,
        getNewSearchResultOptions: getNewSearchResultOptions,
        getSearchResultOptions: getSearchResultOptions,
        find: find,
        findSortedBy: findSortedBy,
        findDetailsById: findDetailsById,
        getSortingTypeClass: getSortingTypeClass,
        loadMore: loadMore
    };
});