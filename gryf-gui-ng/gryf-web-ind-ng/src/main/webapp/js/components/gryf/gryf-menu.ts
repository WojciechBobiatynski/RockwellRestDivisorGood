"use strict";

declare var angular;


angular.module('gryf.ind').controller('MenuController',
    ['$scope', 'AltShortcutHandler', "$state", function($scope, AltShortcutHandler, $state) {
        var CODES = {
            "m": 77,
            "i": 73,
            "d": 68,
            "r": 82
        };

        var URL_1 = contextPath + "/";
        var URL_2 = contextPath + "/";

        AltShortcutHandler.attachAltShortcut(CODES.d, function() {
            window.location = URL_2;
        });

        AltShortcutHandler.attachAltShortcut(CODES.r, function() {
            window.location = URL_1;
        });

        $scope.isActive = function(stateName) {
            return $state.current.name == stateName;
        };

    }]);