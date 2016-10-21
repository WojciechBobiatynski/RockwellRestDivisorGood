/**
 * Created by adziobek on 20.10.2016.
 */
"use strict";

angular.module("gryf.ind").factory("IndService",
    ["$http", function($http) {

        var FIND_IND_URL = contextPath + "/rest/ind";
        var indObject = new IndObject();

        function IndObject() {
            this.contactTypes = [];
            this.entity = {
                id: null,
                code: null,
                accountPayment: null,
                name: null,
                vatRegNum: null,
                addressInvoice: null,
                zipCodeInvoice: null,
                addressCorr: null,
                zipCodeCorr: null,
                remarks: null,
                contacts: []
            }
        }
        var getNewModel = function() {
            indObject = new IndObject();
            return indObject;
        };

        var load = function() {
                var promise = $http.get(FIND_IND_URL);
                promise.then(function(response) {
                    indObject.entity = response.data;
                });
                promise.finally(function() {
                    GryfModals.closeModal(modalInstance);
                });
                return promise;
        };

        return {
            load: load,
            getNewModel: getNewModel
        }

    }]);



