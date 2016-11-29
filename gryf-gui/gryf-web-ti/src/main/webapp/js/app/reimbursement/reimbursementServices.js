angular.module("gryf.ti").factory("ReimbursementsService",
    ['$http', 'GryfModals', 'GryfPopups', 'GryfExceptionHandler', 'GryfHelpers', "GryfTables",
    function ($http, GryfModals, GryfPopups, GryfExceptionHandler, GryfHelpers, GryfTables) {

        var PATH_RMBS = "/rest/reimbursements";
        var FIND_RMBS_LIST_URL = contextPath + PATH_RMBS + "/list";
        var FIND_RMBS_STATUSES_LIST_URL = contextPath + PATH_RMBS + "/statuses";

        var elctRmbsCriteria = new ElctRmbsCriteria();
        var searchResultOptions = new SearchResultOptions();
        var elctRmbsModel = new ElctRmbsModel();

        function ElctRmbsCriteria() {
            this.rmbsNumber = null,
            this.trainingName = null,
            this.pesel = null,
            this.participantName = null,
            this.participantSurname = null,
            this.rmbsDateFrom = null,
            this.rmbsDateTo = null,
            this.rmbsStatus = null,
            this.sortTypes = [],
            this.sortColumns = [],
            this.limit = 10
        };

        function ElctRmbsModel() {
            this.rmbsStatuses = [];
            this.foundRmbs = [];

        }

        function SearchResultOptions() {
            this.displayLimit = 10;
            this.displayLimitIncrementer = 10;
            this.overflow = false;
            this.badQuery = false;
        };

        var getElctRmbsCriteria = function () {
            return elctRmbsCriteria;
        };

        var getNewElctRmbsCriteria = function () {
            elctRmbsCriteria = new ElctRmbsCriteria();
            return elctRmbsCriteria;
        };

        var getSearchResultOptions = function () {
            return searchResultOptions;
        };

        var getNewSearchResultOptions = function () {
            searchResultOptions = new SearchResultOptions();
            return searchResultOptions;
        };

        var getElctRmbsModel = function () {
            return elctRmbsModel;
        };

        var getNewElctRmbsModel = function () {
            elctRmbsModel = new ElctRmbsModel();
            return elctRmbsModel;
        };

        var find = function (findUrl) {
            var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING);

            GryfHelpers.transformDatesToString(elctRmbsModel.foundRmbs);
            if (!findUrl) {
                findUrl = FIND_RMBS_LIST_URL;
            }
            var promise = $http.get(findUrl, {params: elctRmbsCriteria});
            promise.then(function (response) {
                elctRmbsModel.foundRmbs = response.data;
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
            elctRmbsCriteria.limit += searchResultOptions.displayLimitIncrementer;
            searchResultOptions.displayLimit += searchResultOptions.displayLimitIncrementer;
            return find();
        };

        var findSortedBy = function(sortColumnName) {
            GryfTables.sortByColumn(elctRmbsCriteria, sortColumnName);
            return find();
        };

        var getSortingTypeClass = function(entity, columnName) {
            return GryfTables.getSortingTypeClass(entity, columnName);
        };

        return {
            getNewCriteria: getNewElctRmbsCriteria,
            getSearchResultOptions: getSearchResultOptions,
            getNewSearchResultOptions: getNewSearchResultOptions,
            getElctRmbsModel: getElctRmbsModel,
            getNewElctRmbsModel: getNewElctRmbsModel,
            find: find,
            loadMore: loadMore,
            findSortedBy: findSortedBy,
            getSortingTypeClass: getSortingTypeClass
        };
    }]);

angular.module("gryf.ti").factory("ReimbursementsServiceModify",
    ['$http', 'GryfModals', 'GryfPopups', 'GryfExceptionHandler', 'GryfHelpers', 'Upload', function ($http, GryfModals, GryfPopups, GryfExceptionHandler, GryfHelpers, Upload) {
        var PATH_RMBS = "/rest/reimbursements";
        var CREATE_RMBS_BY_ID = contextPath + PATH_RMBS + "/create/";
        var FIND_RMBS_BY_ID = contextPath + PATH_RMBS + "/modify/";
        var SAVE_RMBS = contextPath + PATH_RMBS + "/save";

        var rmbsModel = new RmbsModel();

        function RmbsModel() {
            this.model = null,
            this.trainingInstance = null
        };

        var getRmbsModel = function () {
            return rmbsModel;
        };

        var findById = function(reimbursementId) {
            var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING);

            var promise = $http.get(FIND_RMBS_BY_ID + reimbursementId);
            promise.then(function (response) {
                rmbsModel.model = response.data;
            });

            promise.finally(function () {
                GryfModals.closeModal(modalInstance);
            });
            return promise;
        };

        var createReimbursement = function(trainingInstanceId){
            var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING);

            var promise = $http.get(CREATE_RMBS_BY_ID + trainingInstanceId);
            promise.finally(function () {
                GryfModals.closeModal(modalInstance);
            });

            return promise;
        };

        var save = function() {
            GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM).result.then(function(result) {
                if (!result) {
                    return;
                }
                var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Zapisuję dane"});

                var attachments = {};
                angular.forEach(rmbsModel.model.attachments, function(attachment){
                    if(attachment.index !== undefined){
                        attachments[attachment.index] = attachment.file;
                    }
                });

                Upload.upload({
                    url: SAVE_RMBS,
                    data: {file: attachments, model: Upload.json(rmbsModel.model)}
                }).success(function(response) {
                    GryfPopups.setPopup("success", "Sukces", "Rozliczenie poprawnie zapisane");
                    GryfPopups.showPopup();
                    rmbsModel.model = response;
                }).error(function(response) {
                    GryfPopups.setPopup("error", "Błąd", "Nie udało się zapisać rozliczenia.");
                    GryfPopups.showPopup();
                    GryfExceptionHandler.handleSavingError(response, violations, conflictCallbacksObject);
                }).finally(function() {
                    GryfModals.closeModal(modalInstance);
                });
            });
        };

        var sendToReimburse = function(){

        };

        return {
            getRmbsModel: getRmbsModel,
            findById: findById,
            createReimbursement: createReimbursement,
            save: save,
            sendToReimburse: sendToReimburse
        }

    }]);