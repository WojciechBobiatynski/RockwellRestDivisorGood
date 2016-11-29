angular.module("gryf.trainingInstances").factory("TrainingInstanceSearchService",
    ['$http', 'GryfModals', 'GryfPopups', 'GryfExceptionHandler', 'GryfHelpers', "GryfTables",
        function ($http, GryfModals, GryfPopups, GryfExceptionHandler, GryfHelpers, GryfTables) {

        var FIND_TRAINING_INSTANCE_LIST_URL = contextPath + "/trainingInstance/list";
        var FIND_TRAINING_INSTANCE_DETAILS_URL = contextPath + "/trainingInstance/details/";
        var TRAINING_INSTANCE_STATUSES_LIST_URL = contextPath + "/trainingInstance/statuses";

        var searchDTO = new SearchObjModel();
        var searchResultOptions = new SearchResultOptions();
        var trainingModel = new TrainingModel();

        function SearchObjModel() {
            this.trainingInstanceId = null,
            this.trainingInstitutionId = null,
            this.trainingInstitutionName = null,
            this.trainingId = null,
            this.trainingName = null,
            this.participantPesel = null,
            this.participantName = null,
            this.participantSurname = null,
            this.startDateFrom = null,
            this.startDateTo = null,
            this.endDateFrom = null,
            this.endDateTo = null,
            this.trainingStatusId = null,
            this.sortTypes = [],
            this.sortColumns = [],
            this.limit = 10
        };

        function TrainingModel() {
            this.trainingStatuses = [];
            this.foundTrainings = [];

        };

        function SearchResultOptions() {
            this.displayLimit = 10;
            this.displayLimitIncrementer = 10;
            this.overflow = false;
            this.badQuery = false;
        };

        var getSearchDTO = function () {
            return searchDTO;
        };

        var getNewSearchDTO = function () {
            searchDTO = new SearchObjModel();
            return searchDTO;
        };

        var getSearchResultOptions = function () {
            return searchResultOptions;
        };

        var getNewSearchResultOptions = function () {
            searchResultOptions = new SearchResultOptions();
            return searchResultOptions;
        };

        var getTrainingModel = function () {
            return trainingModel;
        };

        var getNewTrainingModel = function () {
            trainingModel = new TrainingModel();
            return trainingModel;
        };

        var find = function (findUrl) {
            var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING);

            GryfHelpers.transformDatesToString(searchDTO);
            if (!findUrl) {
                findUrl = FIND_TRAINING_INSTANCE_LIST_URL;
            }
            var promise = $http.get(findUrl, {params: searchDTO});
            promise.then(function (response) {
                trainingModel.foundTrainings = response.data;
                searchResultOptions.overflow = response.data.length > searchResultOptions.displayLimit;
            }, function () {
                searchResultOptions.badQuery = true;
            });

            promise.finally(function () {
                GryfModals.closeModal(modalInstance);
            });
            return promise;
        };

        var findDetailsById = function(trainingInstanceId) {
            return $http.get(FIND_TRAINING_INSTANCE_DETAILS_URL + trainingInstanceId).error(function() {
                GryfPopups.setPopup("error", "Błąd", "Nie można pobrać instancji szkolenia o wskazanym id");
                GryfPopups.showPopup();
            });
        };

        var getTiStatuses = function () {
            return $http.get(TRAINING_INSTANCE_STATUSES_LIST_URL).error(function() {
                GryfPopups.setPopup("error", "Błąd", "Nie można pobrać słownika statusów instancji szkoleń");
                GryfPopups.showPopup();
            });
        };

        var findSortedBy = function(sortColumnName) {
            GryfTables.sortByColumn(searchDTO, sortColumnName);
            return find();
        };

        var getSortingTypeClass = function(entity, columnName) {
            return GryfTables.getSortingTypeClass(entity, columnName);
        };

        var loadMore = function () {
            searchDTO.limit += searchResultOptions.displayLimitIncrementer;
            searchResultOptions.displayLimit += searchResultOptions.displayLimitIncrementer;
            return find();
        };

        return {
            getSearchDTO: getSearchDTO,
            getNewSearchDTO: getNewSearchDTO,
            getTiStatuses: getTiStatuses,
            getSearchResultOptions: getSearchResultOptions,
            getNewSearchResultOptions: getNewSearchResultOptions,
            getTrainingModel: getTrainingModel,
            getNewTrainingModel: getNewTrainingModel,
            find: find,
            findSortedBy: findSortedBy,
            findDetailsById: findDetailsById,
            getSortingTypeClass: getSortingTypeClass,
            loadMore: loadMore
        };
    }]);