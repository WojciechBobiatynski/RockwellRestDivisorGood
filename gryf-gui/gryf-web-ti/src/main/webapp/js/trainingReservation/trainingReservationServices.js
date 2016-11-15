angular.module("gryf.ti").factory("TrainingReservationService", function($http, GryfModals, GryfPopups, GryfExceptionHandler) {

    var TRAINING_RESERVATION_URL = contextPath + "/rest/trainingreservation";
    var userTrainingReservationData = {data: null}
    var violations = {};

    var loadUserTrainingReservationData = function(personData) {
        var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Wczytuję dane"});

        return $http.post(TRAINING_RESERVATION_URL + "/userTrainingReservationData/", personData
        ).success(function(data) {
            userTrainingReservationData.data = data;
        }).error(function(error) {
            GryfPopups.setPopup("error", "Błąd", "Nie udało się wczytaj danych użytkownika");
            GryfPopups.showPopup();

            GryfExceptionHandler.handleSavingError(error, violations, null);

        }).finally(function() {
            GryfModals.closeModal(modalInstance);
        });
    }

    var getUserTrainingReservationData = function() {
        return userTrainingReservationData;
    }

    var resetUserTrainingReservationData = function() {
        userTrainingReservationData.data = null;
    }

    var getNewViolations = function() {
        violations = {};
        return violations;
    };

    return {
        loadUserTrainingReservationData: loadUserTrainingReservationData,
        getUserTrainingReservationData: getUserTrainingReservationData,
        resetUserTrainingReservationData: resetUserTrainingReservationData,
        getNewViolations: getNewViolations
    }
});