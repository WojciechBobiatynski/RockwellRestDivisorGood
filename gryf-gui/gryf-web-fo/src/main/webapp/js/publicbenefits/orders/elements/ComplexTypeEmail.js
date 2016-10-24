var scopeComplexTypeGrantedVouchersInfoController;
angular.module("gryf.orders").controller("ComplexTypeGrantedVouchersInfoController",
    ['$scope', function($scope) {
        scopeComplexTypeGrantedVouchersInfoController = $scope;

        $scope.evaluate = function() {
            var limit = $scope.item.limitEurAmount;
            var entitledPln = $scope.item.entitledPlnAmount;
            var exchange = $scope.item.exchange;
            var entitled = $scope.item.entitledVouchersNumber;

            if ((limit * exchange) >= entitledPln) {
                $scope.item.givenVouchersNumber = entitled;
            } else {
                $scope.item.givenVouchersNumber = Math.floor((limit * exchange) / $scope.item.voucherAidValue);
            }

        }

    }]);