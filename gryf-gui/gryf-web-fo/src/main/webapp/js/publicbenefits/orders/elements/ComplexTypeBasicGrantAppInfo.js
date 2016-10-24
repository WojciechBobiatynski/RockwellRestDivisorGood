var scopeComplexTypeBasicOrderInfoController;
angular.module("gryf.orders").controller("ComplexTypeBasicGrantAppInfoController",
    ['$scope', 'GryfModulesUrlProvider', function($scope, GryfModulesUrlProvider) {
        scopeComplexTypeBasicOrderInfoController = $scope;

        $scope.getGrantAppLinkFor = function(grantAppId) {
            return GryfModulesUrlProvider.getUrlFor(GryfModulesUrlProvider.MODULES.GrantApplication , grantAppId);
        };

    }]);