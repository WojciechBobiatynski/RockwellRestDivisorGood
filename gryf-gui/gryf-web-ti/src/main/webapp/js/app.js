angular.module("gryf.ti", ['gryf.privileges', 'gryf.helpers', 'gryf.modals', 'gryf.config', 'ngResource', 'ngFileUpload']).run(function(UserService) {
    UserService.loadUserInfo();
});