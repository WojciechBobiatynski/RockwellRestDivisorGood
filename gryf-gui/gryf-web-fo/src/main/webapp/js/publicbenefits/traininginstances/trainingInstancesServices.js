angular.module("gryf.trainingInstances").factory("TrainingInstanceSearchService",
    ['$http', 'GryfModals', 'GryfPopups', 'GryfExceptionHandler', 'GryfHelpers', "GryfTables",
        function ($http, GryfModals, GryfPopups, GryfExceptionHandler, GryfHelpers, GryfTables) {

        var FIND_TRAINING_INSTANCE_DETAILS_URL = contextPath + "/trainingInstance/details/";
        var FIND_TRAINING_INSTANCE_LIST_URL = contextPath + "/trainingInstance/list";
        var TRAINING_INSTANCE_STATUSES_LIST_URL = contextPath + "/trainingInstance/statuses";

        var searchDTO = new SearchObjModel();
        var searchResultOptions = new SearchResultOptions();
        var trainingModel = new TrainingModel();
        var trainingInstanceModel = new TrainingInstanceModel();

        function TrainingInstanceModel() {
            this.entity = {
                trainingInstanceId: null,
                trainingInstanceVersion: null,
                trainingInstitutionId: null,
                trainingInstitutionName: null,
                trainingId: null,
                trainingName: null,
                participantPesel: null,
                participantName: null,
                participantSurname: null,
                startDateFrom: null,
                startDateTo: null,
                endDateFrom: null,
                endDateTo: null,
                trainingStatusId: null,
                opinionDone: null
            };
        }

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
        }

        function TrainingModel() {
            this.trainingStatuses = [];
            this.foundTrainings = [];

        }

        function SearchResultOptions() {
            this.displayLimit = 10;
            this.displayLimitIncrementer = 10;
            this.overflow = false;
            this.badQuery = false;
        }

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
            return $http.get(FIND_TRAINING_INSTANCE_DETAILS_URL + trainingInstanceId)
                .success(function(data) {
                    trainingInstanceModel.entity = data;
                }).error(function() {
                    GryfPopups.setPopup("error", "Błąd", "Nie można pobrać instancji usługi o wskazanym id");
                    GryfPopups.showPopup();
                });
        };

        var getTiStatuses = function () {
            return $http.get(TRAINING_INSTANCE_STATUSES_LIST_URL).error(function() {
                GryfPopups.setPopup("error", "Błąd", "Nie można pobrać słownika statusów instancji usług");
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

        var getTrainingInstanceModel = function () {
            return trainingInstanceModel;
        };

        return {
            getTrainingInstanceModel: getTrainingInstanceModel,
            getNewTrainingModel: getNewTrainingModel,
            getNewSearchDTO: getNewSearchDTO,
            getTiStatuses: getTiStatuses,
            getSearchResultOptions: getSearchResultOptions,
            getNewSearchResultOptions: getNewSearchResultOptions,
            getTrainingModel: getTrainingModel,
            find: find,
            findDetailsById: findDetailsById,
            findSortedBy: findSortedBy,
            getSortingTypeClass: getSortingTypeClass,
            loadMore: loadMore
        };
    }]);


angular.module("gryf.trainingInstances").factory("TrainingInstanceModifyService", ['$http', 'GryfModals', 'GryfPopups', 'GryfExceptionHandler', 'GryfHelpers', "GryfTables","TrainingInstanceSearchService",
function ($http, GryfModals, GryfPopups, GryfExceptionHandler, GryfHelpers, GryfTables,TrainingInstanceSearchService) {

    var TRAINING_RESERVATION_URL = contextPath + "/trainingInstance/";
    var violations = {};

    var cancelTrainingReservation = function(trainingInstanceId, trainingInstanceVersion) {
        var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Zapisuję"});

        return $http.put(TRAINING_RESERVATION_URL + "cancelTrainingReservation/" + trainingInstanceId + "/" + trainingInstanceVersion
        ).success(function() {
            GryfPopups.setPopup("success", "Sukces", "Anulowano zapis osoby na usługa");
            GryfPopups.showPopup();
        }).error(function(error) {
            GryfPopups.setPopup("error", "Błąd", "Nie udało się anulować zapisu na usługa");
            GryfPopups.showPopup();

            GryfExceptionHandler.handleSavingError(error, violations, null);

        }).finally(function() {
            GryfModals.closeModal(modalInstance);
        });
    };

    var confirmPin = function(trainingInstanceId, pinCode, trainingInstanceVersion, newReservationNum) {
            var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Zapisuję"});

            return $http.put(TRAINING_RESERVATION_URL + "confirmPin",
                {id: trainingInstanceId, pin: pinCode, version: trainingInstanceVersion, newReservationNum: newReservationNum}
            ).success(function() {
                GryfPopups.setPopup("success", "Sukces", "Potwierdzono uczestnictwo w usłudze");
                GryfPopups.showPopup();
                TrainingInstanceSearchService.findDetailsById(TrainingInstanceSearchService.getTrainingInstanceModel().entity.trainingInstanceId);
            }).error(function(error) {
                GryfPopups.setPopup("error", "Błąd", "Nie udało się potwierdzić uczestnictwa w usłudze");
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