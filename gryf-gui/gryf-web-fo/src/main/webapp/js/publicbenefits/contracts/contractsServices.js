/**
 * Created by adziobek on 24.10.2016.
 */
"use strict";

angular.module("gryf.contracts").factory("BrowseContractsService",
    ["$http", "GryfModals", "GryfHelpers", "GryfTables", function($http, GryfModals, GryfHelpers, GryfTables) {
        var FIND_CONTRACTS_URL = contextPath + "/rest/publicBenefits/contract/list";
        var FIND_GRANT_PROGRAM_NAMES_URL = contextPath + "/rest/publicBenefits/grantPrograms";

        var searchDTO = new SearchObjModel();
        var searchResultOptions = new SearchResultOptions();

        function SearchObjModel() {
            this.searchResultList = [];
            this.entity = {
                id: null,
                contractTypeDescription: null,
                pesel: null,
                vatRegNum: null,
                signDateFrom: null,
                signDateTo: null,
                expiryDateFrom: null,
                expiryDateTo: null,
                grantProgramName: null,
                grantProgramOwnerName: null,

                limit: 10,
                sortColumns: [],
                sortTypes: []
            }
        };

        function SearchResultOptions() {
            this.displayLimit = 10;
            this.displayLimitIncrementer = 10;
            this.overflow = false;
            this.badQuery = false;
        };

        var isResultsOverflow = function(zipCodesArray) {
            return zipCodesArray.length > searchResultOptions.displayLimit;
        };

        var find = function(restUrl) {
            var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING);
            GryfHelpers.transformDatesToString(searchDTO.entity);
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

        var getGrantProgramNames = function () {
            return $http.get(FIND_GRANT_PROGRAM_NAMES_URL);
        }

        return {
            getSearchDTO: getSearchDTO,
            getSearchResultOptions: getSearchResultOptions,
            find: find,
            findById: findById,
            findSortedBy: findSortedBy,
            resetSearchResultOptions: resetSearchResultOptions,
            getNewSearchDTO: getNewSearchDTO,
            loadMore: loadMore,
            getGrantProgramNames: getGrantProgramNames
        };
    }]);


angular.module("gryf.contracts").factory("ModifyContractService",
    ['$http', '$routeParams', 'GryfModals', 'GryfPopups', 'GryfExceptionHandler', 'BrowseEnterprisesService', 'BrowseIndividualsService',
        function ($http, $routeParams, GryfModals, GryfPopups, GryfExceptionHandler, BrowseEnterprisesService, BrowseIndividualsService) {

        var FIND_GRANT_PROGRAMS_DICTIONARIES_URL = contextPath + "/rest/publicBenefits/contract/grantProgramsDictionaries";
        var FIND_CONTRACT_TYPES_DICTIONARIES_URL = contextPath + "/rest/publicBenefits/contract/contractTypes";
        var GET_TRAINING_CATEGORY_DICT = contextPath + "/rest/publicBenefits/training/getTrainingCategoriesDict";
        var CONTRACT_URL = contextPath + "/rest/publicBenefits/contract/";
        var POOL_INSTANCES = contextPath + "/rest/publicBenefits/contract/poolInstances/";
        var RESING_URL = contextPath + "/rest/publicBenefits/contract/resign/";

        var grantProgram = new GrantProgram();
        var contractType = new ContractType();
        var contract = new Contract();
        var trainingCategory = new TrainingCategory();
        var violations = {};

        var getViolations = function() {
            return violations;
        };

        var getNewViolations = function() {
            violations = {};
            return violations;
        };

        function TrainingCategory() {
            this.list = [];
        };

        function GrantProgram() {
            this.list = [];
        };

        function ContractType() {
            this.list = [];
        };

        function Contract() {
            this.entity = {
                id : null,
                grantProgram: null,
                contractType : null,
                trainingCategory: null,
                individual : null,
                enterprise : null,
                signDate : null,
                expiryDate : null,
                created : null,
                modified : null
            }
        };

        var getNewGrantPrograms = function () {
            grantProgram = new GrantProgram();
            return grantProgram;
        };

        var getNewContractTypes = function () {
            contractType = new ContractType();
            return contractType;
        };

        var getNewContract = function () {
            contract = new Contract();
            return contract;
        };

        var getNewTrainingCategory = function () {
            trainingCategory = new TrainingCategory();
            return trainingCategory;
        };

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

        var loadContract = function (responseId ) {
            loadGrantPrograms();
            loadContractTypes();
            if ($routeParams.id || responseId) {
                var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Wczytuj?? dane"});
                var promise = $http.get(CONTRACT_URL + ($routeParams.id ? $routeParams.id : responseId));
                promise.then(function(response) {
                    contract.entity = response.data;
                    if(contract.entity.grantProgram) {
                        getTrainingCategoriesDict(contract.entity.grantProgram.id);
                    }
                    getContractPoolInstances();
                });
                promise.finally(function() {
                    GryfModals.closeModal(modalInstance);
                });
                return promise;
            }else {
                loadGrantPrograms();
                loadContractTypes();
            }
        };

        var getTrainingCategoriesDict = function(grantProgramId) {
            var promise;
            promise =  $http.post(GET_TRAINING_CATEGORY_DICT + '/'+ grantProgramId);
            promise.then(function(response) {
                trainingCategory.list = response.data;
            });
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
            var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Zapisuj?? dane"});

            var promise;
            if ($routeParams.id){
                promise = $http.put(CONTRACT_URL + contract.entity.id, contract.entity, {params: additionalParam});
            }else{
                promise = $http.post(CONTRACT_URL, contract.entity, {params: additionalParam});

            }

            promise.then(function() {
                GryfPopups.setPopup("success", "Sukces", "Umowa poprawnie zapisana");
                GryfPopups.showPopup();
            });

            promise.error(function(error) {
                GryfPopups.setPopup("error", "B????d", "Nie uda??o si?? zapisa?? umowy");
                GryfPopups.showPopup();

                var conflictCallbacksObject = {
                    refresh: function() {
                        loadContract();
                    },
                    force: function() {
                        contract.entity.version = error.version;
                        save().then(function() {
                            GryfPopups.showPopup();
                        });
                    }
                };

                GryfExceptionHandler.handleSavingError(error, violations, conflictCallbacksObject);
            });

            promise.finally(function() {
                GryfModals.closeModal(modalInstance);
            });

            return promise;
        };

        var getContractPoolInstances = function(){
            var promise = $http.get(POOL_INSTANCES + contract.entity.id);
            promise.success(function(response) {
                contract.pools = response;
            });
            return promise;
        };

        var resign = function(){
            var promise = $http.post(RESING_URL + contract.entity.id);
            promise.success(function(response) {
                GryfPopups.setPopup("success", "Sukces", "Rezygnacja przeprowadzona pomy??lnie. Zosta??o utworzone rozliczenie.");
                contract.pools = response;
            })
            .error(function(error) {
                GryfPopups.setPopup("error", "B????d", "Akcja nie powiod??a si??");
                var conflictCallbacksObject;
                GryfExceptionHandler.handleSavingError(error, violations, conflictCallbacksObject);
            })
            .finally(function() {
                GryfPopups.showPopup();
            });
            return promise;
        };

        return{
            getNewGrantPrograms: getNewGrantPrograms,
            getNewContractTypes: getNewContractTypes,
            getNewContract: getNewContract,
            loadGrantPrograms: loadGrantPrograms,
            loadContractTypes: loadContractTypes,
            loadContract: loadContract,
            getTrainingCategoriesDict: getTrainingCategoriesDict,
            save: save,
            openEnterpriseLov: openEnterpriseLov,
            openIndividualLov: openIndividualLov,
            getViolation: getViolations,
            getNewViolations: getNewViolations,
            getNewTrainingCategory: getNewTrainingCategory,
            getContractPoolInstances: getContractPoolInstances,
            resign: resign
        }
    }]);