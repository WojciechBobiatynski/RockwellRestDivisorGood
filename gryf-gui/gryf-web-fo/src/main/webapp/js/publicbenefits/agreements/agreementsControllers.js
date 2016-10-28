/**
 * Created by adziobek on 24.10.2016.
 */
angular.module('gryf.agreements').controller("searchform.AgreementsController",
    ["$scope", "AgreementsService", function ($scope, AgreementService) {
    }]);

angular.module('gryf.agreements').controller("detailsform.AgreementsController",
    ["$scope", "ModifyContractService", function ($scope, ModifyContractService) {

        $scope.grantProgram = ModifyContractService.getNewGrantPrograms();
        $scope.contract = ModifyContractService.getNewContract();
        $scope.selectedGrantProgram = {};

        $scope.loadGrantPrograms = function () {
            ModifyContractService.loadGrantPrograms();
        };
        $scope.loadContract = function () {
            ModifyContractService.loadContract();
        }
        $scope.loadGrantPrograms();

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
    }]);