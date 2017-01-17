angular.module("gryf.ti").factory("UserService", ["$http" ,function($http) {

    var USER_INFO_URL = contextPath + "/rest/userinfo/";
    var individualUser = {data: {pesel: null, verificationCode: null}};
    var indUserSearchInfo = {data: {pesel: null, firstName: null, lastName: null}};
    var loggedUserInfo = {data: null};

    var loadUserInfo = function () {
        $http.get(USER_INFO_URL + "trainingInstitution")
        .success(function(data) {
            data.login = login;
            loggedUserInfo.data = data;
        });
    };

    var getIndividualUser = function() {
        return individualUser;
    };

    var getLoggedUserInfo = function() {
        return loggedUserInfo;
    };

    var getIndividualUserSearchInfo = function() {
        return indUserSearchInfo;
    };

    var resetIndividualUser = function() {
        individualUser.data = {pesel: null, verificationCode: null}
    };

    return {
        loadUserInfo: loadUserInfo,
        getLoggedUserInfo: getLoggedUserInfo,
        getIndividualUser: getIndividualUser,
        getIndividualUserSearchInfo: getIndividualUserSearchInfo,
        resetIndividualUser: resetIndividualUser
    }
}]);