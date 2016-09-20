var scopeComplexTypeBasicOrderInfoController;
angular.module("gryf.orders").controller("ComplexTypeBasicOrderInfoController",
    ['$scope','GryfModulesUrlProvider', function($scope, GryfModulesUrlProvider) {
        scopeComplexTypeBasicOrderInfoController = $scope;

        $scope.getEnterpriseLinkFor = function(enterpriseId) {
            return GryfModulesUrlProvider.getUrlFor(GryfModulesUrlProvider.MODULES.Enterprise, enterpriseId);
        };

    }]);