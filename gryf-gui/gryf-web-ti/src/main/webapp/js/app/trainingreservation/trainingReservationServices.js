angular.module("gryf.ti").factory("TrainingReservationService", function($http, GryfModals, GryfPopups, GryfExceptionHandler) {

    var TRAINING_RESERVATION_URL = contextPath + "/rest/trainingreservation";
    var userTrainingReservationData = {data: null};
    var violations = {};

    var loadUserTrainingReservationData = function(personData) {
        var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Wczytuję dane"});

        return $http.post(TRAINING_RESERVATION_URL + "/userTrainingReservationData/", personData
        ).success(function(data) {
            userTrainingReservationData.data = data;
        }).error(function(error) {
            GryfPopups.setPopup("error", "Błąd", "Nie udało się wczytać danych użytkownika");
            GryfPopups.showPopup();

            GryfExceptionHandler.handleSavingError(error, violations, null);

        }).finally(function() {
            GryfModals.closeModal(modalInstance);
        });
    };

    var getUserTrainingReservationData = function() {
        return userTrainingReservationData;
    };

    var resetUserTrainingReservationData = function() {
        userTrainingReservationData.data = null;
    };

    var getNewViolations = function() {
        violations = {};
        return violations;
    };

    var reserveTraining = function(trainingReservationDto) {
        var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Zapisuję"});

        return $http.post(TRAINING_RESERVATION_URL + "/reserveTraining", trainingReservationDto
        ).success(function(data) {
            GryfPopups.setPopup("success", "Sukces", "Osoba została zapisana na szkolenie");
            GryfPopups.showPopup();

            userTrainingReservationData.data = data;
        }).error(function(error) {
            GryfPopups.setPopup("error", "Błąd", "Nie udało się zapisać osoby na szkolenie");
            GryfPopups.showPopup();

            GryfExceptionHandler.handleSavingError(error, violations, null);

        }).finally(function() {
            GryfModals.closeModal(modalInstance);
        });
    };

    var cancelTrainingReservation = function(trainingInstanceId) {
        var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Zapisuję"});

        return $http.put(TRAINING_RESERVATION_URL + "/cancelTrainingReservation/" + trainingInstanceId
        ).success(function(data) {
            GryfPopups.setPopup("success", "Sukces", "Anulowano zapis osoby na szkolenie");
            GryfPopups.showPopup();

            userTrainingReservationData.data = data;
        }).error(function(error) {
            GryfPopups.setPopup("error", "Błąd", "Nie udało się anulować zapisu na szkolenie");
            GryfPopups.showPopup();

            GryfExceptionHandler.handleSavingError(error, violations, null);

        }).finally(function() {
            GryfModals.closeModal(modalInstance);
        });
    };

    return {
        loadUserTrainingReservationData: loadUserTrainingReservationData,
        getUserTrainingReservationData: getUserTrainingReservationData,
        resetUserTrainingReservationData: resetUserTrainingReservationData,
        getNewViolations: getNewViolations,
        reserveTraining: reserveTraining,
        cancelTrainingReservation: cancelTrainingReservation
    }
});