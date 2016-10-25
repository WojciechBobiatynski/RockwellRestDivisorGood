/**
 * Created by adziobek on 24.10.2016.
 */
"use strict";

angular.module("gryf.agreements").factory("AgreementsService",
    ["$http", function($http) {

        var FIND_GRANT_PROGRAMS_DICTIONARIES_URL = contextPath + "/rest/publicBenefits/contract/grantProgramsDictionaries";

        var grantProgram = new GrantProgram();
        var contract = new Contract();
        function GrantProgram() {
            this.list = [];
        }

        function Contract() {
            this.entity = {
                grantProgramName: null,
                grantProgramOwnerName: null,
                contractId: null,
                contractType: null,
                individualId: null,
                enterpriseId: null,
                signDate: null,
                expiryDate: null,
                created: null,
                modified: null

            }
        }

        var getNewGrantPrograms = function () {
            grantProgram = new GrantProgram();
            return grantProgram;
        }
        var getNewContract = function () {
            contract = new Contract();
            return contract;
        }

        var loadGrantPrograms = function() {
            var promise = $http.get(FIND_GRANT_PROGRAMS_DICTIONARIES_URL);
            promise.then(function(response) {
                grantProgram.list = response.data;
            });
            return promise;
        };

        var save = function () {

        }

        return{
            getNewGrantPrograms: getNewGrantPrograms,
            getNewContract: getNewContract,
            loadGrantPrograms: loadGrantPrograms,
            save: save
        }
    }]);