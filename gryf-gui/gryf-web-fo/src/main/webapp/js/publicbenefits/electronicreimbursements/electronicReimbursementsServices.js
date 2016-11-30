angular.module('gryf.electronicreimbursements').factory("electronicReimbursementSearchService",
    ['$http', 'GryfModals', 'GryfHelpers', 'GryfTables', function($http, GryfModals, GryfHelpers, GryfTables) {

        var FIND_RMBS_LIST_URL = contextPath + "/rest/publicBenefits/electronic/reimbursements/list";
        var FIND_RMBS_STATUSES_LIST_URL = contextPath + "/rest/publicBenefits/electronic/reimbursements/statuses";


        var elctRmbsCriteria = new ElctRmbsCriteria();
        var searchResultOptions = new SearchResultOptions();
        var elctRmbsModel = new ElctRmbsModel();

        function ElctRmbsCriteria() {
            this.rmbsNumber =  null,
            this.trainingName= null,
            this.pesel= null,
            this.participantName= null,
            this.participantSurname= null,
            this.rmbsDateFrom= null,
            this.rmbsDateTo= null,
            this.rmbsStatus= null,
            this.sortTypes= [],
            this.sortColumns= [],
            this.limit= 10
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

        var getNewElctRmbsCriteria = function() {
            elctRmbsCriteria = new ElctRmbsCriteria();
            return elctRmbsCriteria;
        };

        var getSearchResultOptions = function() {
            return searchResultOptions;
        };

        var getNewSearchResultOptions = function() {
            searchResultOptions = new SearchResultOptions();
            return searchResultOptions;
        };

        var getElctRmbsModel = function () {
            return elctRmbsModel;
        };

        var find = function(findUrl) {
            var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING);

            GryfHelpers.transformDatesToString(elctRmbsModel.foundRmbs);
            if (!findUrl) {
                findUrl = FIND_RMBS_LIST_URL;
            }
            var promise = $http.get(findUrl, {params: elctRmbsCriteria});
            promise.then(function(response) {
                elctRmbsModel.foundRmbs = response.data;
                searchResultOptions.overflow = response.data.length > searchResultOptions.displayLimit;
            }, function() {
                searchResultOptions.badQuery = true;
            });

            promise.finally(function() {
                GryfModals.closeModal(modalInstance);
            });
            return promise;
        };

        var loadMore = function() {
            elctRmbsCriteria.limit += searchResultOptions.displayLimitIncrementer;
            searchResultOptions.displayLimit += searchResultOptions.displayLimitIncrementer;
            return find();
        };

        var loadReimbursementsStatuses = function() {
            var promise = $http.get(FIND_RMBS_STATUSES_LIST_URL);
            promise.then(function (response) {
                elctRmbsModel.rmbsStatuses = response.data;
            });
            return promise;
        };

        return {
            getNewCriteria: getNewElctRmbsCriteria,
            getSearchResultOptions: getSearchResultOptions,
            getNewSearchResultOptions: getNewSearchResultOptions,
            getElctRmbsModel: getElctRmbsModel,
            find: find,
            loadMore: loadMore,
            loadReimbursementsStatuses: loadReimbursementsStatuses
        };
    }]);

angular.module("gryf.electronicreimbursements").factory("AnnounceEReimbursementService",
    ["$http", "$routeParams", "GryfModals", "GryfExceptionHandler", "GryfPopups",
        function($http, $routeParams, GryfModals, GryfExceptionHandler, GryfPopups) {

            var FIND_RMBS_URL = contextPath + "/rest/publicBenefits/electronic/reimbursements/";
            var CHANGE_STATUS_URL = contextPath + "/rest/publicBenefits/electronic/reimbursements/status";
            var NEW_CORRECTION_DATE_URL = contextPath + "/rest/publicBenefits/electronic/reimbursements/correctionDate";

            var eReimbObject = new EReimbObject();
            var correctionObject = new CorrectionObject();
            var violations = {};

            var getNewModel = function() {
                eReimbObject = new EReimbObject();
                return eReimbObject;
            };

            var getViolations = function() {
                return violations;
            };

            var getNewViolations = function() {
                violations = {};
                return violations;
            };

            function EReimbObject() {
                this.entity = {
                    ermbsId: null,
                    trainingInstanceId: null,
                    products: null,
                    sxoIndAmountDueTotal: null,
                    indSxoAmountDueTotal: null,
                    attachments: null,
                    statusCode: null,
                    returnAccountPayment: null
                }
            }

            function CorrectionObject() {
                this.isNewCorrect = false;
                this.entity = {
                    ermbsId: null,
                    correctionReason: null,
                    requiredDate: null
                }
            }

            var sendToCorrect = function() {
                if(!correctionObject.isNewCorrect) {
                    GryfModals.openModal(GryfModals.MODALS_URL.ERROR_INFO, {
                        message: "Nie dodano nowej korekty!"
                    });
                    return;
                }
                var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Wczytuję dane"});
                var promise = $http.post(CHANGE_STATUS_URL + "/tocorrect", correctionObject.entity);
                //promise.then(function(response) {
                //    eReimbObject.entity = response.data;
                //});
                promise.finally(function () {
                    GryfModals.closeModal(modalInstance);
                });
                return promise;
            };
/*
            var save = function(additionalParam) {
                var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Zapisuję dane"});

                var promise;
                if (eReimbObject.entity.trainingId) {
                    promise = $http.put(TRAINING_URL + eReimbObject.entity.trainingId, eReimbObject.entity, {params: additionalParam});
                } else {
                    promise = $http.post(TRAINING_URL, eReimbObject.entity, {params: additionalParam});
                }

                promise.then(function() {
                    GryfPopups.setPopup("success", "Sukces", "Szkolenie poprawnie zapisane");
                    GryfPopups.showPopup();
                });

                promise.error(function(error) {
                    GryfPopups.setPopup("error", "Błąd", "Nie udało się zapisać szkolenia");
                    GryfPopups.showPopup();

                    var conflictCallbacksObject = {
                        refresh: function() {
                            load();
                        },
                        force: function() {
                            eReimbObject.entity.version = error.version;
                            save().then(function() {
                                GryfPopups.showPopup();
                            });
                        }
                    };

                    GryfExceptionHandler.handleSavingError(error, violations, conflictCallbacksObject);
                });

                promise.finally(function() {
                    GryfModals.closeModal(modalInstance);
                });

                return promise;
            };*/

            var findById = function(rmbsId) {
                if ($routeParams.id || rmbsId) {
                    var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Wczytuję dane"});
                    var promise = $http.get(FIND_RMBS_URL + ($routeParams.id ? $routeParams.id : rmbsId));
                    promise.then(function(response) {
                        eReimbObject.entity = response.data;
                    });
                    promise.finally(function() {
                        GryfModals.closeModal(modalInstance);
                    });
                    return promise;
                }
            };

            var getNewRequiredCorrectionDate = function() {
                return $http.get(NEW_CORRECTION_DATE_URL).success(function(date) {
                    correctionObject.isNewCorrect = true;
                    correctionObject.entity.requiredDate = date;
                    correctionObject.entity.ermbsId = eReimbObject.entity.ermbsId;
                });;
            };

            var getCorrectionObject = function() {
                return correctionObject;
            };

            return {
                getNewModel: getNewModel,
                getViolation: getViolations,
                getNewViolations: getNewViolations,
                findById: findById,
                sendToCorrect: sendToCorrect,
                getCorrectionObject: getCorrectionObject,
                getNewRequiredCorrectionDate: getNewRequiredCorrectionDate,
                //save: save
            };
        }]);