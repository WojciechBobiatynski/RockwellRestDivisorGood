/**
 * Created by adziobek on 24.10.2016.
 */
angular.module('gryf.contracts').controller("searchform.ContractsController",
    ["$scope", "BrowseContractsService", "GryfPopups", function ($scope, BrowseContractsService, GryfPopups) {
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

        $scope.getSortedBy = function(sortColumnName) {
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
var test;
angular.module('gryf.contracts').controller("detailsform.ContractsController",
    ["$scope", '$routeParams', "ModifyContractService", "GryfModals", "GryfPopups", "GryfModulesUrlProvider",
    function ($scope, $routeParams, ModifyContractService, GryfModals, GryfPopups, GryfModulesUrlProvider) {
        $scope.grantProgram = ModifyContractService.getNewGrantPrograms();
        $scope.contractType = ModifyContractService.getNewContractTypes();
        $scope.model = ModifyContractService.getNewContract();
        $scope.trainingCategory = ModifyContractService.getNewTrainingCategory();
        $scope.violations = ModifyContractService.getNewViolations();
        GryfPopups.showPopup();
        test = $scope;

        $scope.datepicker = {
            signDate: false,
            expiryDate: false
        };
        $scope.isDisabled = false;

        $scope.canEdit = function () {
            if($routeParams.id != null) {
                $scope.isDisabled = true;
            }
        };

        $scope.openDatepicker = function(value) {
            $scope.datepicker[value] = true;
        };

        $scope.loadContract = function () {
            ModifyContractService.loadContract();
        };

        $scope.grantProgramChanged = function () {
            var grantProgramId = $scope.model.entity.grantProgram.id;
            if(grantProgramId) {
                $scope.loadTrainingCategories(grantProgramId);
            }
        };

        $scope.loadTrainingCategories = function (grantProgramId) {
            ModifyContractService.getTrainingCategoriesDict(grantProgramId);
        };

        $scope.loadContract();
        $scope.canEdit();

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
        };

        $scope.newContract = function() {
            var callback = function() {
                $scope.model = ModifyContractService.getNewContract();
                $scope.violations = ModifyContractService.getNewViolations();
                $scope.trainingCategory = ModifyContractService.getNewTrainingCategory();
                window.location = GryfModulesUrlProvider.LINKS.Contract;
            };
            $scope.showAcceptModal("Wywołując tę akcję stracisz niezapisane dane", callback)
        };

        $scope.goBack = function() {
            var callback = function() {
                window.location = GryfModulesUrlProvider.getBackUrl(GryfModulesUrlProvider.MODULES.Contract);
            };
            $scope.showAcceptModal("Wywołując tę akcję stracisz niezapisane dane", callback);
        };

        $scope.saveAndAdd = function () {
            var saveCallback = function () {
                $scope.violations = ModifyContractService.getNewViolations();
                ModifyContractService.save().success(function () {
                    window.location = GryfModulesUrlProvider.LINKS.Contract;
                });
            };
            $scope.showAcceptModal("Ta akcja zapisuje zmiany w Osobie fizycznej", saveCallback);
        };

        $scope.save = function () {
            var saveCallback = function () {
                $scope.violations = ModifyContractService.getNewViolations();
                ModifyContractService.save().success(function (id) {
                    window.location = GryfModulesUrlProvider.getUrlFor(GryfModulesUrlProvider.MODULES.Contract, id);
                    ModifyContractService.loadContract();
                });
            };
            $scope.showAcceptModal("Ta akcja zapisuje zmiany w Umowie", saveCallback);
        };

        $scope.showAcceptModal = function(messageText, callback) {
            var message = {message: messageText};
            GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM, message).result.then(function(result) {
                if (!result) {
                    return;
                }
                callback();
            });
        };

        $scope.hasNotPrivilege = function (privilege) {
            return !privileges[privilege];
        };

        $scope.getPrevUrl = function() {
            return gryfSessionStorage.getUrlFromSessionStorage();
        };

        $scope.resign = function () {
            $scope.showAcceptModal("Zostanie przeprowadzona rezygnacja z umowy", ModifyContractService.resign);
        };

        $scope.resignButtonVisible = function () {
            return !!$scope.model.pools && $scope.model.pools.length > 0;
        }

    }]);