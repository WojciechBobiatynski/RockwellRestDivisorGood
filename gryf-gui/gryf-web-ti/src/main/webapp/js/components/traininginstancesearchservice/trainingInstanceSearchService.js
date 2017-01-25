angular.module("gryf.ti").factory("TrainingInstanceSearchService",
    ['$http', 'GryfModals', 'GryfPopups', 'GryfExceptionHandler', 'GryfHelpers', "GryfTables",
        function ($http, GryfModals, GryfPopups, GryfExceptionHandler, GryfHelpers, GryfTables) {

        var FIND_TRAINING_STATUSES_LIST_URL = contextPath + "/rest/trainingToReimburse/statuses";
        var FIND_TRAINING_INSTANCE_LIST_URL = contextPath + "/rest/trainingInstance/list";
        var FIND_TRAINING_INSTANCE_DETAILS_URL = contextPath + "/rest/trainingInstance/details/";

        var trainingCriteria = new TrainingCriteria();
        var searchResultOptions = new SearchResultOptions();
        var trainingModel = new TrainingModel();

        function TrainingCriteria() {
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

        var getTrainingCriteria = function () {
            return trainingCriteria;
        };

        var getNewTrainingCriteria = function () {
            trainingCriteria = new TrainingCriteria();
            return trainingCriteria;
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

            GryfHelpers.transformDatesToString(trainingCriteria);
            if (!findUrl) {
                findUrl = FIND_TRAINING_INSTANCE_LIST_URL;
            }
            var promise = $http.get(findUrl, {params: trainingCriteria});
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
                GryfPopups.setPopup("error", "Błąd", "Nie można pobrać instancji usługi o wskazanym id");
                GryfPopups.showPopup();
            });
        };

        var findSortedBy = function(sortColumnName) {
            GryfTables.sortByColumn(trainingCriteria, sortColumnName);
            return find();
        };

        var getSortingTypeClass = function(entity, columnName) {
            return GryfTables.getSortingTypeClass(entity, columnName);
        };

        var loadMore = function () {
            trainingCriteria.limit += searchResultOptions.displayLimitIncrementer;
            searchResultOptions.displayLimit += searchResultOptions.displayLimitIncrementer;
            return find();
        };

        return {
            getNewCriteria: getNewTrainingCriteria,
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