angular.module("gryf.ti").factory("TrainingReservationService", function($http, GryfModals, GryfPopups, GryfExceptionHandler) {

    var TRAINING_RESERVATION_URL = contextPath + "/rest/trainingreservation";
    var userProductInstancePool = {data: null}
    var violations = {};

    var loadProductInstancePoolsOfUser = function(personData) {
        var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Wczytuję dane"});

        return $http.post(TRAINING_RESERVATION_URL + "/availableProductPool/", personData
        ).success(function(data) {
            userProductInstancePool.data = data;
        }).error(function(error) {
            GryfPopups.setPopup("error", "Błąd", "Nie udało się wczytaj danych użytkownika");
            GryfPopups.showPopup();

            GryfExceptionHandler.handleSavingError(error, violations, null);

        }).finally(function() {
            GryfModals.closeModal(modalInstance);
        });
    }

    var getProductInstancePoolsOfUser = function() {
        return userProductInstancePool;
    }

    var resetProductInstancePoolsOfUser = function() {
        userProductInstancePool.data = null;
    }

    var getNewViolations = function() {
        violations = {};
        return violations;
    };

    return {
        loadProductInstancePoolsOfUser: loadProductInstancePoolsOfUser,
        getProductInstancePoolsOfUser: getProductInstancePoolsOfUser,
        resetProductInstancePoolsOfUser: resetProductInstancePoolsOfUser,
        getNewViolations: getNewViolations
    }
});