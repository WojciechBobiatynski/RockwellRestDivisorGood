/**
 * Created by adziobek on 24.10.2016.
 */
"use strict";

angular.module("gryf.agreements").factory("ModifyContractService",
    ['$http', 'GryfModals', 'BrowseEnterprisesService', 'BrowseIndividualsService', function ($http, GryfModals, BrowseEnterprisesService, BrowseIndividualsService) {

        var FIND_GRANT_PROGRAMS_DICTIONARIES_URL = contextPath + "/rest/publicBenefits/contract/grantProgramsDictionaries";

        var grantProgram = new GrantProgram();
        var contract = new Contract();
        function GrantProgram() {
            this.list = [];
        }

        function Contract() {
            this.grantProgramName = null,
            this.grantProgramOwnerName = null,
            this.contractId = null,
            this.contractType = null,
            this.individual = null,
            this.enterprise = null,
            this.signDate = null,
            this.expiryDate = null,
            this.created = null,
            this.modified = null

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

        var openEnterpriseLov = function () {
            var TEMPLATE_URL = GryfModals.MODALS_URL.LOV_ENTERPRISES;
            return GryfModals.openLovModal(TEMPLATE_URL, BrowseEnterprisesService, "lg");
        };

        var openIndividualLov = function () {
            var TEMPLATE_URL = GryfModals.MODALS_URL.LOV_INDIVIDUALS;
            return GryfModals.openLovModal(TEMPLATE_URL, BrowseIndividualsService, "lg");
        };

        return{
            getNewGrantPrograms: getNewGrantPrograms,
            getNewContract: getNewContract,
            loadGrantPrograms: loadGrantPrograms,
            save: save,
            openEnterpriseLov: openEnterpriseLov,
            openIndividualLov: openIndividualLov
        }
    }]);