angular.module("gryf.asynchjobs").factory("ImportDataRowsSearchService", ["$http", "$routeParams", "GryfModals", "GryfPopups", "GryfExceptionHandler", "GryfHelpers", "GryfTables",
    function ($http, $routeParams, GryfModals, GryfPopups, GryfExceptionHandler, GryfHelpers, GryfTables) {
        var REST_URL = contextPath + "/rest/administration/asynchjobs";

        var FIND_IMPORT_DATA_ROWS_LIST_URL = REST_URL + "/rows";
        var GET_IMPORT_DATA_ROWS_STATUSES_LIST_URL = REST_URL + "/rows/statuses";

        var searchObjModel = new SearchObjModel();
        var searchResultOptions = new SearchResultOptions();

        var dictionaries = {
            rowStatus: null
        };

        function SearchObjModel() {
            this.result = [];
            this.entity = {
                id: null,
                jobId: null,
                rowNumber: null,
                description: null,
                status: null,

                sortTypes: [],
                sortColumns: [],
                limit: 10
            };
        }

        function SearchResultOptions() {
            this.displayLimit = 10;
            this.displayLimitIncrementer = 10;
            this.overflow = false;
            this.badQuery = false;
        }

        var getDictionaries = function() {
            return dictionaries;
        };

        var getSearchObjModel = function () {
            return searchObjModel;
        };

        var getNewSearchObjModel = function () {
            searchObjModel = new SearchObjModel();
            return searchObjModel;
        };

        var getSearchResultOptions = function () {
            return searchResultOptions;
        };

        var getNewSearchResultOptions = function () {
            searchResultOptions = new SearchResultOptions();
            return searchResultOptions;
        };

        var find = function () {
            var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING);

            var jobId = $routeParams.id;
            searchObjModel.entity.jobId = jobId;

            GryfHelpers.transformDatesToString(searchObjModel.entity);
            var promise = $http.get(FIND_IMPORT_DATA_ROWS_LIST_URL, {params: searchObjModel.entity});
            promise.then(function (response) {
                searchObjModel.result = response.data;
                searchResultOptions.overflow = response.data.length > searchResultOptions.displayLimit;
            }, function () {
                searchResultOptions.badQuery = true;
            });

            promise.finally(function () {
                GryfModals.closeModal(modalInstance);
            });
            return promise;
        };

        var findRowStatuses = function() {
            return $http.get(GET_IMPORT_DATA_ROWS_STATUSES_LIST_URL).success(function(data) {
                dictionaries.rowStatus = data;
            });
        };

        var findSortedBy = function(sortColumnName) {
            GryfTables.sortByColumn(searchObjModel.entity, sortColumnName);
            return find();
        };

        var getSortingTypeClass = function(entity, columnName) {
            return GryfTables.getSortingTypeClass(entity, columnName);
        };

        var loadMore = function () {
            searchObjModel.entity.limit += searchResultOptions.displayLimitIncrementer;
            searchResultOptions.displayLimit += searchResultOptions.displayLimitIncrementer;
            return find();
        };

        return {
            getSearchObjModel: getSearchObjModel,
            getDictionaries: getDictionaries,
            getNewSearchObjModel: getNewSearchObjModel,
            getSearchResultOptions: getSearchResultOptions,
            getNewSearchResultOptions: getNewSearchResultOptions,
            find: find,
            loadMore: loadMore,
            findRowStatuses: findRowStatuses,
            findSortedBy: findSortedBy,
            getSortingTypeClass: getSortingTypeClass
        };
}]);