/**
 * Created by adziobek on 24.10.2016.
 */
angular.module('gryf.agreements').controller("searchform.AgreementsController",
    ["$scope", "BrowseContractsService", "GryfPopups", function ($scope, BrowseContractsService, GryfPopups) {
        scopeBrowseController = $scope;
        $scope.searchObjModel = BrowseContractsService.getSearchDTO();
        $scope.searchResultOptions = BrowseContractsService.getSearchResultOptions();
        gryfSessionStorage.setUrlToSessionStorage();
        GryfPopups.showPopup();

        $scope.datepicker = {
            signDate: false,
            expiryDate: false
        };

        $scope.openDatepicker = function(value) {
            $scope.datepicker[value] = true;
        };

        $scope.loadMore = function() {
            BrowseContractsService.loadMore();
        };

        $scope.findContracts = function() {
            $scope.searchResultOptions.badQuery = false;
            BrowseContractsService.find();
        };

        $scope.clear = function() {
            $scope.searchObjModel = BrowseContractsService.getNewSearchDTO();
            $scope.searchResultOptions = BrowseContractsService.resetSearchResultOptions();
        };

        $scope.getSortedEnterprises = function(sortColumnName) {
            $scope.searchResultOptions.badQuery = false;
            BrowseContractsService.findSortedBy(sortColumnName);
        };

        $scope.getSortingTypeClass = function(columnName) {
            var sortingType = $scope.searchObjModel.entity.sortTypes[0];
            if (columnName == $scope.searchObjModel.entity.sortColumns[0]) {
                return sortingType;
            }
        }
    }]);

angular.module('gryf.agreements').controller("detailsform.AgreementsController",
    ["$scope", "ModifyContractService", "GryfModals", function ($scope, ModifyContractService, GryfModals) {

        var NEW_INDIVIDUAL_URL =  contextPath + "'/publicbenefits/agreements/#modify";


        $scope.grantProgram = ModifyContractService.getNewGrantPrograms();
        $scope.contractType = ModifyContractService.getNewContractTypes();
        $scope.contract = ModifyContractService.getNewContract();

        $scope.loadGrantPrograms = function () {
            ModifyContractService.loadGrantPrograms();
        };

        $scope.loadContractTypes = function () {
            ModifyContractService.loadContractTypes();
        }
        $scope.loadGrantPrograms();
        $scope.loadContractTypes();

        $scope.openEnterpriseLov = function() {
            ModifyContractService.openEnterpriseLov().result.then(function(chosenEnterprise) {
                $scope.contract.enterprise = chosenEnterprise;
                $scope.$broadcast('propagateEnterpriseData', chosenEnterprise);
            });
        };

        $scope.openIndividualLov = function() {
            ModifyContractService.openIndividualLov().result.then(function(chosenIndividual) {
                $scope.contract.individual = chosenIndividual;
                $scope.$broadcast('propagateIndividualData', chosenIndividual);
            });
        };

        $scope.saveAndAdd = function() {
            $scope.save(NEW_INDIVIDUAL_URL);
        };

        $scope.save = function(redirectUrl) {
            var messageText = {
                message: "Ta akcja zapisuje nową umowę"
            };

            GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM, messageText).result.then(function(result) {
                if (!result) {
                    return;
                }
                executeSave();
            });

            var executeSave = function() {
                ModifyContractService.save().then(function() {
                    if (!redirectUrl){
                        redirectUrl = $scope.getPrevUrl();
                    }
                    window.location = redirectUrl;
                });
            }
        };
    }]);