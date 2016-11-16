angular.module("gryf.ti").factory("TrainingToReimburseService",
    ['$http', 'GryfModals', 'GryfPopups', 'GryfExceptionHandler', 'GryfHelpers', function ($http, GryfModals, GryfPopups, GryfExceptionHandler, GryfHelpers) {
        var FIND_TRAINING_LIST_URL = contextPath + "/rest/trainingToReimburse/list";
        var FIND_TRAINING_STATUSES_LIST_URL = contextPath + "/rest/trainingToReimburse/statuses";

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
            this.trainingStatus = null,
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

        var find = function (findUrl) {
            var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING);

            GryfHelpers.transformDatesToString(trainingCriteria);
            if (!findUrl) {
                findUrl = FIND_TRAINING_LIST_URL;
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

        var loadMore = function () {
            trainingCriteria.limit += searchResultOptions.displayLimitIncrementer;
            searchResultOptions.displayLimit += searchResultOptions.displayLimitIncrementer;
            return find();
        };

        var loadTrainingStatuses = function () {
            var promise = $http.get(FIND_TRAINING_STATUSES_LIST_URL);
            promise.then(function (response) {
                trainingModel.trainingStatuses = response.data;
            });
            return promise;
        };

        return {
            getNewCriteria: getNewTrainingCriteria,
            getSearchResultOptions: getSearchResultOptions,
            getNewSearchResultOptions: getNewSearchResultOptions,
            getTrainingModel: getTrainingModel,
            find: find,
            loadMore: loadMore,
            loadTrainingStatuses: loadTrainingStatuses
        };
    }]);