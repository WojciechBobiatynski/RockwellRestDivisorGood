"use strict";

angular.module("gryf.ti", ['gryf.privileges', 'gryf.helpers', 'gryf.modals', 'gryf.config', 'ngResource', 'ngFileUpload', 'ui.mask'])
.run(["$http", "$rootScope", "UserService",
    function($http, $rootScope, UserService) {
        UserService.loadUserInfo();

        $http.get(contextPath + "/rest/training/feedbackEmail").success(function(data) {
            $rootScope.FEEDBACK_EMAIL = data[0];
        });
    }]
);