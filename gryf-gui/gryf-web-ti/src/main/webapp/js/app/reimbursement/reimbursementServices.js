angular.module("gryf.ti").factory("ReimbursementsService",
    ['$http', 'GryfModals', 'GryfPopups', 'GryfExceptionHandler', 'GryfHelpers', "GryfTables",
    function ($http, GryfModals, GryfPopups, GryfExceptionHandler, GryfHelpers, GryfTables) {

        var PATH_RMBS = "/rest/reimbursements";
        var FIND_RMBS_LIST_URL = contextPath + PATH_RMBS + "/list";
        var FIND_RMBS_STATUSES_LIST_URL = contextPath + PATH_RMBS + "/statuses";
        var FIND_GRANT_PROGRAM_NAMES_URL = contextPath + "/rest/grantPrograms/list";

        var elctRmbsCriteria = new ElctRmbsCriteria();
        var searchResultOptions = new SearchResultOptions();
        var elctRmbsModel = new ElctRmbsModel();

        function ElctRmbsCriteria() {
            this.grantProgramId = null,
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

        var getGrantProgramNames = function () {
            return $http.get(FIND_GRANT_PROGRAM_NAMES_URL);
        }

        return {
            getNewCriteria: getNewElctRmbsCriteria,
            getSearchResultOptions: getSearchResultOptions,
            getNewSearchResultOptions: getNewSearchResultOptions,
            getElctRmbsModel: getElctRmbsModel,
            getNewElctRmbsModel: getNewElctRmbsModel,
            find: find,
            loadMore: loadMore,
            findSortedBy: findSortedBy,
            getSortingTypeClass: getSortingTypeClass,
            getGrantProgramNames: getGrantProgramNames
        };
    }]);

angular.module("gryf.ti").factory("ReimbursementsServiceModify",
    ['$http', '$q', '$state', 'GryfModals', 'GryfPopups', 'GryfExceptionHandler', 'GryfHelpers', 'Upload', function ($http, $q, $state, GryfModals, GryfPopups, GryfExceptionHandler, GryfHelpers, Upload) {
        var PATH_RMBS = "/rest/reimbursements";
        var CREATE_RMBS_BY_ID = contextPath + PATH_RMBS + "/create/";
        var FIND_RMBS_BY_ID = contextPath + PATH_RMBS + "/modify/";
        var SAVE_RMBS = contextPath + PATH_RMBS + "/save";
        var SEND_TO_REIMBURSE = contextPath + PATH_RMBS + "/send";
        var SAVE_CORRECTION = contextPath + PATH_RMBS + "/savecorr";
        var SEND_CORRECTION = contextPath + PATH_RMBS + "/sendcorr";

        var rmbsModel = new RmbsModel();
        var violations = {};

        function RmbsModel() {
            this.model = null,
            this.trainingInstance = null
        }

        var getNewRmbsModel = function () {
            rmbsModel = new RmbsModel();
            return rmbsModel;
        };

        var getRmbsModel = function () {
            return rmbsModel;
        };

        var getViolations = function () {
            return violations;
        };

        var getNewViolations = function () {
            violations = {};
            return violations;
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

            promise.success(function(data) {
                rmbsModel.model = data;
            });

            promise.error(function (response) {
                var conflictCallbacksObject = {
                    makeAction: function() {
                        $state.go("trainingToReimburse");
                    }
                };
                GryfExceptionHandler.handleSavingError(response, violations, conflictCallbacksObject);
            });

            promise.finally(function () {
                GryfModals.closeModal(modalInstance);
            });

            return promise;
        };

        var save = function(URL) {
            var deferred = $q.defer();

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

                var promise = Upload.upload({
                    url: URL,
                    data: {file: attachments, model: Upload.json(rmbsModel.model)}
                });

                promise.success(function(response) {
                    rmbsModel.model = response;
                });

                promise.error(function(response) {
                    var conflictCallbacksObject = {
                        refresh: function() {
                            if(URL === SAVE_RMBS){
                                createReimbursement(rmbsModel.trainingInstance.trainingInstanceId).success(function(data){
                                    rmbsModel.model = data;
                                })
                            } else {
                                findById(rmbsModel.model.ermbsId).success(function(data){
                                    rmbsModel.model = data;
                                })
                            }
                        },
                        force: function() {
                            rmbsModel.model.version = response.version;
                            save(URL);
                        }
                    };

                    GryfExceptionHandler.handleSavingError(response, violations, conflictCallbacksObject);
                });

                promise.finally(function() {
                    GryfModals.closeModal(modalInstance);
                    deferred.resolve(promise);
                });
            });

            return deferred.promise;
        };

        var saveReimburse = function(){
            save(SAVE_RMBS)
                .then(function() {
                    GryfPopups.setPopup("success", "Sukces", "Zapisano rozliczenie");
                }, function() {
                    GryfPopups.setPopup("error", "Błąd", "Nie udało się zapisać rozliczenia");
                })
                .finally(function() {
                    GryfPopups.showPopup();
                });;
        };

        var sendToReimburse = function(){
            save(SEND_TO_REIMBURSE)
                .then(function() {
                    GryfPopups.setPopup("success", "Sukces", "Wysłano do rozliczenia");
                    $state.go("reimbursements");
                }, function() {
                    GryfPopups.setPopup("error", "Błąd", "Nie udało się wysłać rozliczenia");
                })
                .finally(function() {
                    GryfPopups.showPopup();
                });
        };

        var saveCorrection = function(){
            save(SAVE_CORRECTION)
                .then(function() {
                    GryfPopups.setPopup("success", "Sukces", "Korekta została zapisana");
                }, function() {
                    GryfPopups.setPopup("error", "Błąd", "Nie udało się zapisać korekty");
                })
                .finally(function() {
                    GryfPopups.showPopup();
                });
        };

        var sendCorrection = function(){
            save(SEND_CORRECTION)
                .then(function() {
                    GryfPopups.setPopup("success", "Sukces", "Korekta została wysłana");
                    $state.go("reimbursements");
                }, function() {
                    GryfPopups.setPopup("error", "Błąd", "Nie udało się wysłać korekty");
                })
                .finally(function() {
                    GryfPopups.showPopup();
                });;
        };

        return {
            getNewRmbsModel: getNewRmbsModel,
            getRmbsModel: getRmbsModel,
            findById: findById,
            createReimbursement: createReimbursement,
            saveReimburse: saveReimburse,
            sendToReimburse: sendToReimburse,
            getViolations: getViolations,
            getNewViolations: getNewViolations,
            saveCorrection: saveCorrection,
            sendCorrection: sendCorrection
        }

    }]);