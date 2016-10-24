var scopeAttrDController;
angular.module("gryf.orders").controller("AttrDController", ['$scope', function($scope) {
    $scope.datepicker = {
        valueDate: false
    };

    $scope.openDatepicker = function(value) {
        $scope.datepicker[value] = true;
    };

}]);