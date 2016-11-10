angular.module("gryf.ti").factory("DashboardService", function($http, GryfModals) {

    var USER_INFO_URL = contextPath + "/rest/userinfo/";

    var getLoggedUserInfo = function () {
        var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Wczytuję dane"});
        var promise = $http.get(USER_INFO_URL + "trainingInstitution");
        promise.then(function(response) {
            response.data.login = login;
        });
        promise.finally(function() {
            GryfModals.closeModal(modalInstance);
        });
        return promise;
    };

    return {
        getLoggedUserInfo: getLoggedUserInfo
    }
});