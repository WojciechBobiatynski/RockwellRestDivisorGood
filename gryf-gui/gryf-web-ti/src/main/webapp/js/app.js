angular.module("gryf.ti", ['gryf.privileges', 'gryf.helpers', 'gryf.modals', 'gryf.config']).run(function(UserService) {
    UserService.loadUserInfo();
});