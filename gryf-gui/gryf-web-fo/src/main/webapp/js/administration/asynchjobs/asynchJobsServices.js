angular.module("gryf.asynchjobs").factory("AsynchJobsSearchService",
    ['$http', 'GryfModals', 'GryfPopups', 'GryfExceptionHandler', 'GryfHelpers', "GryfTables",
        function ($http, GryfModals, GryfPopups, GryfExceptionHandler, GryfHelpers, GryfTables) {
        var REST_URL = contextPath + "/rest/administration/asynchjobs";

        var FIND_ASYNCH_JOB_LIST_URL = REST_URL + "/list";
        var FIND_STATUSES_DICTIONARY_URL = REST_URL + "/dictionary/statuses";
        var FIND_TYPES_DICTIONARY_URL = REST_URL + "/dictionary/types";

        var searchObjModel = new SearchObjModel();
        var searchResultOptions = new SearchResultOptions();
        var dictionaries = {
            jobStatus: null,
            jobTypes: null
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
                findUrl = FIND_ASYNCH_JOB_LIST_URL;
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
            return $http.get(FIND_STATUSES_DICTIONARY_URL).success(function(data) {
                dictionaries.jobStatus = data;
            });
        };

        var findJobTypes = function() {
            return $http.get(FIND_TYPES_DICTIONARY_URL).success(function(data) {
                dictionaries.jobTypes = data;
            });
        };

        var getTiStatuses = function () {
            return $http.get(TRAINING_INSTANCE_STATUSES_LIST_URL).error(function() {
                GryfPopups.setPopup("error", "Błąd", "Nie można pobrać słownika statusów instancji szkoleń");
                GryfPopups.showPopup();
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
            getTiStatuses: getTiStatuses,
            find: find,
            findJobTypes: findJobTypes,
            findJobStatuses: findJobStatuses,
            findSortedBy: findSortedBy,
            getSortingTypeClass: getSortingTypeClass,
            loadMore: loadMore
        };
    }]);


angular.module("gryf.asynchjobs").factory("AsynchJobsModifyService", ['$http', 'GryfModals', 'GryfPopups', 'GryfExceptionHandler', 'GryfHelpers', "GryfTables",
function ($http, GryfModals, GryfPopups, GryfExceptionHandler, GryfHelpers, GryfTables) {

    var TRAINING_RESERVATION_URL = contextPath + "/trainingInstance/";
    var violations = {};

    var cancelTrainingReservation = function(trainingInstanceId) {
        var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Zapisuję"});

        return $http.put(TRAINING_RESERVATION_URL + "cancelTrainingReservation/" + trainingInstanceId
        ).success(function() {
            GryfPopups.setPopup("success", "Sukces", "Anulowano zapis osoby na szkolenie");
            GryfPopups.showPopup();
        }).error(function(error) {
            GryfPopups.setPopup("error", "Błąd", "Nie udało się anulować zapisu na szkolenie");
            GryfPopups.showPopup();

            GryfExceptionHandler.handleSavingError(error, violations, null);

        }).finally(function() {
            GryfModals.closeModal(modalInstance);
        });
    };

    var confirmPin = function(trainingInstanceId, pinCode) {
        var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Zapisuję"});

        return $http.put(TRAINING_RESERVATION_URL + "confirmPin/" + trainingInstanceId + "/" + pinCode
        ).success(function() {
            GryfPopups.setPopup("success", "Sukces", "Potwierdzono uczestnictwo w szkoleniu");
            GryfPopups.showPopup();
        }).error(function(error) {
            GryfPopups.setPopup("error", "Błąd", "Nie udało się potwierdzić uczestnictwa w szkoleniu");
            GryfPopups.showPopup();

            GryfExceptionHandler.handleSavingError(error, violations, null);

        }).finally(function() {
            GryfModals.closeModal(modalInstance);
        });
    };

    var getViolations = function() {
        return violations;
    };

    return {
        confirmPin: confirmPin,
        cancelTrainingReservation: cancelTrainingReservation,
        getViolations: getViolations
    };
}]);