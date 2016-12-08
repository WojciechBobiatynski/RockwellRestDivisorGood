angular.module("gryf.asynchjobs").factory("AsynchJobsSearchService", ["$http", "GryfModals", "GryfPopups", "GryfExceptionHandler", "GryfHelpers", "GryfTables",
    function ($http, GryfModals, GryfPopups, GryfExceptionHandler, GryfHelpers, GryfTables) {
        var REST_URL = contextPath + "/rest/administration/asynchjobs";

        var FIND_ASYNCH_JOBS_LIST_URL = REST_URL + "/list";
        var GET_STATUSES_LIST_DICTIONARY_URL = REST_URL + "/dictionary/statuses";
        var GET_TYPES_LIST_DICTIONARY_URL = REST_URL + "/dictionary/types/";
        var GET_GRANT_PROGRAMS_LIST_DICTIONARY_URL = contextPath + "/rest/publicBenefits/grantPrograms";

        var searchObjModel = new SearchObjModel();
        var searchResultOptions = new SearchResultOptions();

        var dictionaries = {
            jobStatus: null,
            jobTypes: null,
            grantPrograms: null
        };

        function SearchObjModel() {
            this.result = [];
            this.entity = {
                id: null,
                orderId: null,
                type: null,
                description: null,
                status: null,
                createdDateFrom: null,
                createdDateTo: null,

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

        var find = function (findUrl) {
            var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING);

            GryfHelpers.transformDatesToString(searchObjModel.entity);
            if (!findUrl) {
                findUrl = FIND_ASYNCH_JOBS_LIST_URL;
            }
            var promise = $http.get(findUrl, {params: searchObjModel.entity});
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

        var findJobStatuses = function() {
            return $http.get(GET_STATUSES_LIST_DICTIONARY_URL).success(function(data) {
                dictionaries.jobStatus = data;
            });
        };

        var findJobTypes = function(grantProgramId, onlyImportJobs) {
            var pathVariables = "";
            if(grantProgramId) {
                pathVariables = grantProgramId + "/";
            }
            pathVariables += onlyImportJobs;
            return $http.get(GET_TYPES_LIST_DICTIONARY_URL + pathVariables).success(function(data) {
                dictionaries.jobTypes = data;
            });
        };

        var findGrantPrograms = function() {
            return $http.get(GET_GRANT_PROGRAMS_LIST_DICTIONARY_URL).success(function(data) {
                dictionaries.grantPrograms = data;
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
            findJobTypes: findJobTypes,
            findJobStatuses: findJobStatuses,
            findGrantPrograms: findGrantPrograms,
            findSortedBy: findSortedBy,
            getSortingTypeClass: getSortingTypeClass
        };
    }]);


angular.module("gryf.asynchjobs").factory("AsynchJobsModifyService", ["$http", "Upload", "GryfModals", "GryfPopups", "GryfExceptionHandler", "GryfHelpers", "GryfTables",
function ($http, Upload, GryfModals, GryfPopups, GryfExceptionHandler, GryfHelpers, GryfTables) {

    var REST_URL = contextPath + "/rest/administration/asynchjobs";
    var CREATE_IMPORT_JOB_URL = REST_URL + "/createImportJob";
    var FIND_ASYNCH_JOB_DETAILS_URL = REST_URL + "/details/";

    var DETAILS_URL = contextPath + "/administration/asynchjobs/#/modify/";

    var importJobModel = new ImportJobModel();
    var violations = {};

    function ImportJobModel() {
        this.file = null;
        this.entity = {
            id: null,
            orderId: null,
            grantProgramId: 100,
            type: null,
            description: null,
            status: null,
            filePath: null
        };
    }

    var getImportJobModel = function() {
        return importJobModel;
    };

    var getNewImportJobModel = function() {
        importJobModel = new ImportJobModel();
        return importJobModel;
    };

    var getViolations = function() {
        return violations;
    };

    var getNewViolations = function() {
        violations = {};
        return violations;
    };

    var getNewImportJobUrl = function() {
        return DETAILS_URL;
    };

    var createImportJob = function() {
        Upload.upload({
            url: CREATE_IMPORT_JOB_URL,
            data: angular.toJson(importJobModel.entity),
            file: importJobModel.file
        }).success(function (jobId) {
            GryfPopups.setPopup("success", "Sukces", "Rozpoczęto import danych z pliku");
            window.location = DETAILS_URL + jobId;
        }).error(function (error) {
            GryfPopups.setPopup("error", "Błąd", "Nie udało się rozpocząć importu danych z pliku");
            GryfExceptionHandler.handleSavingError(error, violations, null);
        }).finally(function () {
            GryfPopups.showPopup();
        });
    };

    var findDetailsById = function(id) {
        var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING);

        var promise = $http.get(FIND_ASYNCH_JOB_DETAILS_URL + id);
        promise.success(function (data) {
            importJobModel.entity = data;
        });
        promise.finally(function () {
            GryfModals.closeModal(modalInstance);
        });
        return promise;
    };

    return {
        getImportJobModel: getImportJobModel,
        getNewImportJobModel: getNewImportJobModel,
        getViolations: getViolations,
        getNewViolations: getNewViolations,
        findDetailsById: findDetailsById,
        createImportJob: createImportJob,
        getNewImportJobUrl: getNewImportJobUrl
    };
}]);