angular.module("gryf.ti").factory("TrainingReservationService", [ "$http", "GryfModals", "GryfPopups", "GryfExceptionHandler" ,function($http, GryfModals, GryfPopups, GryfExceptionHandler) {

    var TRAINING_RESERVATION_URL = contextPath + "/rest/trainingreservation";
    var FIND_GRANT_PROGRAM_NAMES_URL = contextPath + "/rest/grantPrograms/list";

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
            GryfPopups.setPopup("success", "Sukces", "Osoba została zapisana na usługa");
            GryfPopups.showPopup();

            userTrainingReservationData.data = data;
        }).error(function(error) {
            GryfPopups.setPopup("error", "Błąd", "Nie udało się zapisać osoby na usługa");
            GryfPopups.showPopup();

            GryfExceptionHandler.handleSavingError(error, violations, null);

        }).finally(function() {
            GryfModals.closeModal(modalInstance);
        });
    };

    var cancelTrainingReservation = function(trainingInstanceId, trainingInstanceVersion) {
        var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Zapisuję"});

        return $http.put(TRAINING_RESERVATION_URL + "/cancelTrainingReservation/" + trainingInstanceId + "/" + trainingInstanceVersion
        ).success(function(data) {
            GryfPopups.setPopup("success", "Sukces", "Anulowano zapis osoby na usługa");
            GryfPopups.showPopup();

            userTrainingReservationData.data = data;
        }).error(function(error) {
            GryfPopups.setPopup("error", "Błąd", "Nie udało się anulować zapisu na usługa");
            GryfPopups.showPopup();

            GryfExceptionHandler.handleSavingError(error, violations, null);

        }).finally(function() {
            GryfModals.closeModal(modalInstance);
        });
    };

    var confirmPin = function(trainingInstanceId, pinCode, newReservationNum, trainingInstanceVersion) {
        var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Zapisuję"});

        return $http.put(TRAINING_RESERVATION_URL + "/confirmPin",
            {id: trainingInstanceId, pin: pinCode, newReservationNum: newReservationNum, version: trainingInstanceVersion}
        ).success(function(data) {
            GryfPopups.setPopup("success", "Sukces", "Potwierdzono uczestnictwo w usłudze");
            GryfPopups.showPopup();

            userTrainingReservationData.data = data;
        }).error(function(error) {
            GryfPopups.setPopup("error", "Błąd", "Nie udało się potwierdzić uczestnictwa w usłudze");
            GryfPopups.showPopup();

            GryfExceptionHandler.handleSavingError(error, violations, null);

        }).finally(function() {
            GryfModals.closeModal(modalInstance);
        });
    };

    var getGrantProgramNames = function () {
        return $http.get(FIND_GRANT_PROGRAM_NAMES_URL);
    }

    return {
        loadUserTrainingReservationData: loadUserTrainingReservationData,
        getUserTrainingReservationData: getUserTrainingReservationData,
        resetUserTrainingReservationData: resetUserTrainingReservationData,
        getNewViolations: getNewViolations,
        reserveTraining: reserveTraining,
        cancelTrainingReservation: cancelTrainingReservation,
        confirmPin: confirmPin,
        getGrantProgramNames: getGrantProgramNames
    }
}]);