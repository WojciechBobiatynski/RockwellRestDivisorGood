angular.module("gryf.ti").factory("TrainingSearchService", function($http, GryfModals, GryfTables) {
    var FIND_TRAINING_INS_URL = contextPath + "/rest/publicBenefits/training/list";
    var GET_TRAINING_CATEGORY_DICT = contextPath + "/rest/publicBenefits/training/getTrainingCategoriesDict";

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
            categoryCode: null,

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

    var resetSearchResultOptions = function() {
        searchResultOptions = new SearchResultOptions;
        return searchResultOptions;
    };

    return {
        getSearchDTO: getSearchDTO,
        getSearchResultOptions: getSearchResultOptions,
        find: find,
        findSortedBy: findSortedBy,
        getTrainingCategoriesDict: getTrainingCategoriesDict,
        getNewSearchDTO: getNewSearchDTO,
        getNewSearchResultOptions: getNewSearchResultOptions,
        getSortingTypeClass: getSortingTypeClass

    };
});