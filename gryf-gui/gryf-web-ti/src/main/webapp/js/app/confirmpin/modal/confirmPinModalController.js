angular.module("gryf.ti").controller("ConfirmPinModalController",
    ["$scope", "$stateParams", "TrainingInstanceSearchService", "TrainingReservationService", "GryfModals",
function($scope, $stateParams, TrainingInstanceSearchService, TrainingReservationService, GryfModals) {
    $scope.close = $scope.$close;
    $scope.trainingInstance = {data: null};
    $scope.pinCode = null;
    $scope.newReservationNum = null;

    TrainingInstanceSearchService.findDetailsById($stateParams.trainingInstanceId).success(function(data) {
        $scope.trainingInstance.data = data;
        $scope.newReservationNum = $scope.trainingInstance.data.productAssignedNum;
    });

    $scope.confirmPin = function() {
        var messageText = {
            message: "Ilość bonów została zmieniona. Czy jesteś pewny, że chcesz zmienić ilość bonów?"
        };
        if($scope.newReservationNum != $scope.trainingInstance.data.productAssignedNum) {
            GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM, messageText).result.then(function (result) {
                if (!result) {
                    return;
                }
                $scope.confirmPinSend();
            });
        }else{
            $scope.confirmPinSend();
        }
    };

    $scope.confirmPinSend = function() {
        TrainingReservationService.confirmPin($scope.trainingInstance.data.trainingInstanceId, $scope.pinCode,
            $scope.newReservationNum, $scope.trainingInstance.data.trainingInstanceVersion
        ).then(function () {
            TrainingInstanceSearchService.find();
            $scope.close(true);
        });
    }
}]);