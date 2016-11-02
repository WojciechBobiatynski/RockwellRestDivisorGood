/**
 * Created by adziobek on 24.10.2016.
 */
"use strict";

angular.module("gryf.agreements").factory("BrowseContractsService",
    ["$http", "GryfModals", "GryfTables", function($http, GryfModals, GryfTables) {
        var FIND_CONTRACTS_URL = contextPath + "/rest/publicBenefits/contract/list";
        var searchDTO = new SearchObjModel();
        var searchResultOptions = new SearchResultOptions();

        function SearchObjModel() {
            this.searchResultList = [];
            this.entity = {
                contractId: null,
                contractTypeDescription: null,
                pesel: null,
                vatRegNum: null,
                signDate: null,
                expiryDate: null,
                grantProgramName: null,
                grantProgramOwnerName: null,

                limit: 10,
                sortColumns: [],
                sortTypes: []
            }
        }

        function SearchResultOptions() {
            this.displayLimit = 10;
            this.displayLimitIncrementer = 10;
            this.overflow = false;
            this.badQuery = false;
        }

        var isResultsOverflow = function(zipCodesArray) {
            return zipCodesArray.length > searchResultOptions.displayLimit;
        };

        var find = function(restUrl) {
            var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING);
            if (!restUrl){
                restUrl = FIND_CONTRACTS_URL;
            }
            var promise = $http.get(restUrl, {params: searchDTO.entity});
            promise.then(function(response) {
                //success
                searchDTO.searchResultList = response.data;
                searchResultOptions.overflow = isResultsOverflow(response.data);
            }, function() {
                //error
                resetSearchResultOptions();
                searchResultOptions.badQuery = true;
            });

            promise.finally(function() {
                GryfModals.closeModal(modalInstance);
            });
            return promise;
        };

        var findById = function(id) {
            getNewSearchDTO();
            searchDTO.entity.id = id;
            return $http.get(FIND_CONTRACTS_URL, {params: searchDTO.entity});
        };

        var getNewSearchDTO = function() {
            searchDTO = new SearchObjModel;
            return searchDTO;
        };

        var resetSearchResultOptions = function() {
            searchResultOptions = new SearchResultOptions;
            return searchResultOptions;
        };

        var getSearchDTO = function() {
            return searchDTO;
        };

        var getSearchResultOptions = function() {
            return searchResultOptions;
        };

        var findSortedBy = function(sortColumnName) {
            GryfTables.sortByColumn(searchDTO.entity, sortColumnName);
            return find();
        };

        var loadMore = function() {
            searchDTO.entity.limit += searchResultOptions.displayLimitIncrementer;
            searchResultOptions.displayLimit += searchResultOptions.displayLimitIncrementer;
            return find();
        };

        return {
            getSearchDTO: getSearchDTO,
            getSearchResultOptions: getSearchResultOptions,
            find: find,
            findById: findById,
            findSortedBy: findSortedBy,
            resetSearchResultOptions: resetSearchResultOptions,
            getNewSearchDTO: getNewSearchDTO,
            loadMore: loadMore
        }
    }]);


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