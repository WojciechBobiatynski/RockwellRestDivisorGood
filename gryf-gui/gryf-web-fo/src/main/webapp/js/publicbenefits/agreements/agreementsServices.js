/**
 * Created by adziobek on 24.10.2016.
 */
"use strict";

angular.module("gryf.agreements").factory("ModifyContractService",
    ['$http', 'GryfModals', 'BrowseEnterprisesService', 'BrowseIndividualsService', function ($http, GryfModals, BrowseEnterprisesService, BrowseIndividualsService) {

        var FIND_GRANT_PROGRAMS_DICTIONARIES_URL = contextPath + "/rest/publicBenefits/contract/grantProgramsDictionaries";
        var FIND_CONTRACT_TYPES_DICTIONARIES_URL = contextPath + "/rest/publicBenefits/contract/contractTypes";
        var CONTRACT_URL = contextPath + "/rest/publicBenefits/contract/";

        var grantProgram = new GrantProgram();
        var contractType = new ContractType();
        var contract = new Contract();
        function GrantProgram() {
            this.list = [];
        }

        function ContractType() {
            this.list = [];
        }

        function Contract() {
            this.grantProgram = null,
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

        var getNewContractTypes = function () {
            contractType = new ContractType();
            return contractType;
        };

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

        var loadContractTypes = function() {
            var promise = $http.get(FIND_CONTRACT_TYPES_DICTIONARIES_URL);
            promise.then(function(response) {
                contractType.list = response.data;
            });
            return promise;
        };

        var openEnterpriseLov = function () {
            var TEMPLATE_URL = GryfModals.MODALS_URL.LOV_ENTERPRISES;
            return GryfModals.openLovModal(TEMPLATE_URL, BrowseEnterprisesService, "lg");
        };

        var openIndividualLov = function () {
            var TEMPLATE_URL = GryfModals.MODALS_URL.LOV_INDIVIDUALS;
            return GryfModals.openLovModal(TEMPLATE_URL, BrowseIndividualsService, "lg");
        };

        var save = function(additionalParam) {
            var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Zapisuję dane"});

            var promise;
            promise = $http.post(CONTRACT_URL, contract, {params: additionalParam});

            promise.then(function() {
                GryfPopups.setPopup("success", "Sukces", "Umowa poprawnie zapisana");
            });

            promise.error(function(error) {
                GryfPopups.setPopup("error", "Błąd", "Nie udało się zapisać umowy");
                GryfPopups.showPopup();
            });

            promise.finally(function() {
                GryfModals.closeModal(modalInstance);
            });

            return promise;
        };

        return{
            getNewGrantPrograms: getNewGrantPrograms,
            getNewContractTypes: getNewContractTypes,
            getNewContract: getNewContract,
            loadGrantPrograms: loadGrantPrograms,
            loadContractTypes: loadContractTypes,
            save: save,
            openEnterpriseLov: openEnterpriseLov,
            openIndividualLov: openIndividualLov
        }
    }]);