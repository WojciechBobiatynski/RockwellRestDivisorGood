"use strict";

angular.module('gryf.menu', ['gryf.privileges', 'gryf.helpers', 'gryf.modals']).controller('MenuController',
    ['$scope', 'AltShortcutHandler', function($scope, AltShortcutHandler) {
        var CODES = {
            "m": 77,
            "i": 73,
            "d": 68,
            "r": 82
        };

        var NEW_DELIVERY_URL = contextPath + "/publicBenefits/reimbursements/#registerDelivery";
        var LIST_DELIVERY_URL = contextPath + "/publicBenefits/reimbursements/#searchDelivery";

        AltShortcutHandler.attachAltShortcut(CODES.d, function() {
            window.location = LIST_DELIVERY_URL;
        });

        AltShortcutHandler.attachAltShortcut(CODES.r, function() {
            window.location = NEW_DELIVERY_URL;
        });


    }]);

angular.element(document).ready(function() {
    angular.bootstrap(document.getElementById('menu'), ['gryf.menu']);
});