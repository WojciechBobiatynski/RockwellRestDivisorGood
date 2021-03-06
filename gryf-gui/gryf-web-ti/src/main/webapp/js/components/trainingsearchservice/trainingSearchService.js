angular.module("gryf.ti").factory("TrainingSearchService", [ "$http", "GryfModals", "GryfTables", "GryfHelpers", "GryfPopups" ,function($http, GryfModals, GryfTables, GryfHelpers, GryfPopups) {
    var FIND_TRAINING_URL = contextPath + "/rest/training/list";
    var FIND_TRAINING_TO_RESERVE_URL = contextPath + "/rest/training/listToReserve/{0}/{1}";
    var FIND_TRAINING_DETAILS_URL = contextPath + "/rest/training/";
    var FIND_PRECALCULATED_TRAINING_DETAILS_URL = contextPath + "/rest/training/precalculated/";
    var FIND_GRANT_PROGRAM_NAMES_URL = contextPath + "/rest/grantPrograms/list";

    var searchDTO = new SearchObjModel();
    var searchResultOptions = new SearchResultOptions();

    function SearchObjModel() {
        this.searchResultList = [];
        this.entity = {
            grantProgramName:  null,
            grantProgramId: null,
            trainingId: null,
            institutionId: null,
            institutionName: null,
            name: null,
            externalId: null,
            priceFrom: null,
            priceTo: null,
            startDateFrom: null,
            startDateTo: null,
            endDateFrom: null,
            endDateTo: null,
            place: null,
            hoursNumberFrom: null,
            hoursNumberTo: null,
            hourPriceFrom: null,
            hourPriceTo: null,
            categoryCodes: null,
            active: null,


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

    var findToReserve = function(grantProgramId, indId, restUrl) {
        var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING);
        GryfHelpers.transformDatesToString(searchDTO.entity);
        if (!restUrl) {
            restUrl = GryfHelpers.stringFormat(FIND_TRAINING_TO_RESERVE_URL, grantProgramId, indId); //FIND_TRAINING_TO_RESERVE_URL + grantProgramId;
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
        return $http.get(FIND_TRAINING_DETAILS_URL + trainingId).error(function() {
            GryfPopups.setPopup("error", "B????d", "Nie mo??na pobra?? us??ugi o wskazanym id");
            GryfPopups.showPopup();
        });
    };

    var findPrecalculatedDetailsById = function(trainingId, grantProgramId) {
        return $http.get(FIND_PRECALCULATED_TRAINING_DETAILS_URL + trainingId + "/" + grantProgramId).error(function() {
            GryfPopups.setPopup("error", "B????d", "Nie mo??na pobra?? us??ugi o wskazanym id");
            GryfPopups.showPopup();
        });
    };

    var findSortedBy = function(sortColumnName) {
        GryfTables.sortByColumn(searchDTO.entity, sortColumnName);
        return find();
    };

    var findToReserveSortedBy = function(grantProgramId, sortColumnName) {
        GryfTables.sortByColumn(searchDTO.entity, sortColumnName);
        return findToReserve(grantProgramId);
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

    var loadMoreToReserve = function(grantProgramId, indId) {
        searchDTO.entity.limit += searchResultOptions.displayLimitIncrementer;
        searchResultOptions.displayLimit += searchResultOptions.displayLimitIncrementer;
        return findToReserve(grantProgramId, indId);
    };

    var getGrantProgramNames = function () {
        return $http.get(FIND_GRANT_PROGRAM_NAMES_URL);
    }


    return {
        getNewSearchDTO: getNewSearchDTO,
        getSearchDTO: getSearchDTO,
        getNewSearchResultOptions: getNewSearchResultOptions,
        getSearchResultOptions: getSearchResultOptions,
        find: find,
        findSortedBy: findSortedBy,
        loadMore: loadMore,
        findToReserve: findToReserve,
        loadMoreToReserve: loadMoreToReserve,
        findToReserveSortedBy: findToReserveSortedBy,
        findDetailsById: findDetailsById,
        findPrecalculatedDetailsById: findPrecalculatedDetailsById,
        getSortingTypeClass: getSortingTypeClass,
        getGrantProgramNames: getGrantProgramNames
    };
}]);