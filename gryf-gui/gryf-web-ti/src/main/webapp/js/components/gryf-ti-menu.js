"use strict";

angular.module('gryf.ti', ['gryf.config']);

angular.module('gryf.ti', ['gryf.privileges', 'gryf.helpers', 'gryf.modals', 'gryf.config']).controller('MenuController',
    ['$scope', 'AltShortcutHandler', "$state", function($scope, AltShortcutHandler, $state) {
        var CODES = {
            "m": 77,
            "i": 73,
            "d": 68,
            "r": 82
        };

        var URL1 = contextPath + "/url1";
        var URL2 = contextPath + "/url2";

        AltShortcutHandler.attachAltShortcut(CODES.d, function() {
            window.location = URL1;
        });

        AltShortcutHandler.attachAltShortcut(CODES.r, function() {
            window.location = URL2;
        });

        $scope.isActive = function(stateName) {
            return $state.current.name == stateName;
        };

    }]);