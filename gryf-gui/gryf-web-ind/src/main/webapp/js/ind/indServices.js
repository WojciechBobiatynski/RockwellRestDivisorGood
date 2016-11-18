/**
 * Created by adziobek on 20.10.2016.
 */
"use strict";

angular.module("gryf.ind").factory("IndService",
    ["$http", "GryfModals", "GryfPopups", function($http, GryfModals, GryfPopups) {

        var FIND_IND_URL = contextPath + "/rest/ind/";
        var SEND_REIMBURSMENT_PIN_URL = contextPath + "/rest/ind/reimbursmentPin/resend";
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

        var sendPIN = function (trainingInstanceId) {
            var messageText = {message: "PIN zostanie wysłany na adres email: " + indObject.entity.verificationEmail};
            GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM, messageText).result.then(function (result) {
                if (result) {
                    sendPINAction(trainingInstanceId);
                   // GryfPopups.setPopup("success", "", "PIN został pomyślnie wysłany");
                   // GryfPopups.showPopup();
                }
            });
        };

        var sendPINAction = function (trainingInstanceId) {

            var promise;
            promise = $http.post(SEND_REIMBURSMENT_PIN_URL + '/'+ trainingInstanceId);

            promise.then(function () {
                GryfPopups.setPopup("success", "Sukces", "PIN został pomyślnie wysłany");
                GryfPopups.showPopup();
            });

            promise.error(function (error) {
                GryfPopups.setPopup("error", "Błąd", "Nie udało się wysłać PINU");
                GryfPopups.showPopup();

           /*     var conflictCallbacksObject = {
                    refresh: function () {
                        loadContract();
                    },
                    force: function () {
                        contract.entity.version = error.version;
                        save().then(function () {
                            GryfPopups.showPopup();
                        });
                    }
                };

                GryfExceptionHandler.handleSavingError(error, violations, conflictCallbacksObject);*/
            });

           /* promise.finally(function () {
                GryfModals.closeModal(modalInstance);
            });*/

            return promise;
        }

        return {
            load: load,
            getNewModel: getNewModel,
            sendPIN: sendPIN,
            sendPINAction: sendPINAction
        }
    }]);



