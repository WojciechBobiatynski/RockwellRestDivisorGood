angular.module("gryf.password").factory("PasswordService", ["$http", "GryfModals", "GryfPopups", function ($http, GryfModals, GryfPopups) {

    var CHANG_PASSWORD_URL = contextPath + "/administration/pass/change/confirm";

    var passwordModel = new PasswordModel();

    function PasswordModel() {
        this.currentPassword = null,
        this.newPassword = null,
        this.confirmPassword = null
    };

    var getPasswordModel = function(){
        return passwordModel;
    };

    var changePassword = function(){
        GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM, '').result.then(function(result) {
            if (!result) {
                return;
            }
            var promise = $http.post(CHANG_PASSWORD_URL, passwordModel);
            promise.success(function() {
                GryfPopups.setPopup("success", "Sukces", "Hasło zostało zmienione");
                GryfPopups.showPopup();
            });
            promise.error(function(error) {
                GryfPopups.setPopup("error", "Błąd", "Nie udało się zmienić hasła");
                GryfPopups.showPopup();
            });
        });
    };

    return {
        getPasswordModel: getPasswordModel,
        changePassword: changePassword
    }

}]);