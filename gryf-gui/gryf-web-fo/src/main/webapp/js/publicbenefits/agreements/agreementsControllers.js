/**
 * Created by adziobek on 24.10.2016.
 */
angular.module('gryf.agreements').controller("searchform.AgreementsController",
    ["$scope", "AgreementsService", function ($scope, AgreementService) {
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