angular.module("gryf.ti", ['gryf.privileges', 'gryf.helpers', 'gryf.modals', 'gryf.config', 'ngResource', 'ngFileUpload', 'ui.mask']).run(function(UserService) {
    UserService.loadUserInfo();
});