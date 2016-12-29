var scopeComplexTypeAddressInfoController;
angular.module("gryf.orders").controller("ComplexTypeAddressInfoController",
    ['$scope', 'GryfModals', 'ZipCodesModel', function($scope, GryfModals, ZipCodesModel) {
        scopeComplexTypeGrantedAddressController = $scope;

        $scope.openCorrZipCodesLov = function() {
            openZipCodesLov().result.then(function (chosedItem) {
                $scope.item.zipCodeCorr = chosedItem;
            });
        };

        var openZipCodesLov = function() {
            var TEMPLATE_URL = GryfModals.MODALS_URL.LOV_ZIPCODES;
            return GryfModals.openLovModal(TEMPLATE_URL, ZipCodesModel);
        };

    }]);