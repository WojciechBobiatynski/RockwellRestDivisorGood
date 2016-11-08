/**
 * Created by adziobek on 24.10.2016.
 */
var scopeBrowseController;

angular.module('gryf.contracts').controller("searchform.ContractsController",
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

var scopeModifyController;
angular.module('gryf.contracts').controller("detailsform.ContractsController",
    ["$scope", "ModifyContractService", "GryfModals", "GryfPopups", function ($scope, ModifyContractService, GryfModals, GryfPopups) {

        var NEW_CONTRACT_URL =  contextPath + "'/publicbenefits/contracts/#modify";

        scopeModifyController = $scope;
        $scope.grantProgram = ModifyContractService.getNewGrantPrograms();
        $scope.contractType = ModifyContractService.getNewContractTypes();
        $scope.model = ModifyContractService.getNewContract();
        $scope.violations = ModifyContractService.getNewViolations();
        gryfSessionStorage.setUrlToSessionStorage();
        GryfPopups.showPopup();

        $scope.datepicker = {
            signDate: false,
            expiryDate: false
        };

        $scope.openDatepicker = function(value) {
            $scope.datepicker[value] = true;
        };

        $scope.loadContract = function () {
            ModifyContractService.loadContract();
        }
        $scope.loadContract();

        $scope.openEnterpriseLov = function() {
            if($scope.model.entity.contractType.id == 'ENT') {
                ModifyContractService.openEnterpriseLov().result.then(function(chosenEnterprise) {
                    $scope.model.entity.enterprise = chosenEnterprise;
                    $scope.$broadcast('propagateEnterpriseData', chosenEnterprise);
                });
            }
        };

        $scope.openIndividualLov = function() {
            ModifyContractService.openIndividualLov().result.then(function(chosenIndividual) {
                $scope.model.entity.individual = chosenIndividual;
                $scope.$broadcast('propagateIndividualData', chosenIndividual);
            });
        };

        $scope.checkContractData = function () {
            if($scope.model.entity.contractType.id == 'IND') {
                $scope.model.entity.enterprise = null;
            }
        }

        $scope.saveAndAdd = function() {
            $scope.save(NEW_CONTRACT_URL);
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
                $scope.violations = ModifyContractService.getNewViolations();
                ModifyContractService.save();
            }

            $scope.getPrevUrl = function() {
                return gryfSessionStorage.getUrlFromSessionStorage();
            };
        };
    }]);