/**
 * Created by adziobek on 24.10.2016.
 */
angular.module('gryf.agreements').controller("searchform.AgreementsController",
    ["$scope", "AgreementsService", function ($scope, AgreementService) {
    }]);

angular.module('gryf.agreements').controller("detailsform.AgreementsController",
    ["$scope", "AgreementsService", function ($scope, AgreementService) {

        $scope.grantProgram = AgreementService.getNewGrantPrograms();
        $scope.contract = AgreementService.getNewContract();
        $scope.selectedGrantProgram = {};

        $scope.loadGrantPrograms = function () {
            AgreementService.loadGrantPrograms();
        };
        $scope.loadContract = function () {
            AgreementService.loadContract();
        }
        $scope.loadGrantPrograms();
    }]);