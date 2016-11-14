/**
 * Created by adziobek on 20.10.2016.
 */
"use strict";

angular.module("gryf.ind").factory("IndService",
    ["$http", "GryfModals", "GryfPopups", function($http, GryfModals, GryfPopups) {

        var FIND_IND_URL = contextPath + "/rest/ind/";
        var indObject = new IndObject();

        function IndObject() {
            this.entity = {
                firstName: null,
                lastName: null,
                pesel: null,
                products: [],
                trainings: [],
                agreements: []
            }
        }

        var getNewModel = function() {
            indObject = new IndObject();
            return indObject;
        };

        var load = function() {
            var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Wczytuję dane"});
                var promise = $http.get(FIND_IND_URL);
                promise.then(function(response) {
                    indObject.entity = response.data;
                });
                promise.finally(function() {
                    GryfModals.closeModal(modalInstance);
                });
                return promise;
        };

        var sendPIN = function() {
            var messageText = {message: "PIN zostanie wysłany na adres email: " + indObject.entity.email};
            GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM, messageText).result.then(function(result) {
                if(result) {
                    GryfPopups.setPopup("success", "", "PIN został pomyślnie wysłany");
                    GryfPopups.showPopup();
                }
            });
        }

        return {
            load: load,
            getNewModel: getNewModel,
            sendPIN: sendPIN
        }
    }]);



