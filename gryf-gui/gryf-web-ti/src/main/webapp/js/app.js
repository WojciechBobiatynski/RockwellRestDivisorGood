angular.module("gryf.ti", ['gryf.privileges', 'gryf.helpers', 'gryf.modals', 'gryf.config', 'ngResource']).run(function(UserService) {
    UserService.loadUserInfo();
});