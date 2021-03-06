"use strict";

angular.module("gryf.trainingInstances").controller("TrainingInstancesSearchController",
    ["$scope", "GryfModals", "BrowseTrainingInsService", "BrowseTrainingService", "TrainingInstanceSearchService",
        function ($scope, GryfModals, BrowseTrainingInsService, BrowseTrainingService, TrainingInstanceSearchService) {

    $scope.searchDTO = TrainingInstanceSearchService.getNewSearchDTO();
    $scope.searchResultOptions = TrainingInstanceSearchService.getSearchResultOptions();
    $scope.trainingModel = TrainingInstanceSearchService.getTrainingModel();
    $scope.statusesDictionary = null;

    $scope.find = function() {
        $scope.searchResultOptions.badQuery = false;
        TrainingInstanceSearchService.find();
    };

    $scope.getSortedBy = function(sortColumnName) {
        $scope.searchResultOptions.badQuery = false;
        TrainingInstanceSearchService.findSortedBy(sortColumnName);
    };

    $scope.getSortingTypeClass = function(columnName) {
        return TrainingInstanceSearchService.getSortingTypeClass($scope.searchDTO, columnName);
    };

    $scope.clear = function() {
        $scope.searchDTO = TrainingInstanceSearchService.getNewSearchDTO();
        $scope.searchResultOptions = TrainingInstanceSearchService.getNewSearchResultOptions();
    };

    $scope.datepicker = {
        isTrainingStartDateFromOpened: false,
        isTrainingStartDateToOpened: false,
        isTrainingEndDateFromOpened: false,
        isTrainingEndDateToOpened: false
    };

    $scope.openDatepicker = function(value) {
        $scope.datepicker[value] = true;
    };

    TrainingInstanceSearchService.getTiStatuses().success(function(data) {
        $scope.statusesDictionary = data;
    });

    $scope.loadMore = function() {
        TrainingInstanceSearchService.loadMore();
    };

    $scope.openInstitutionLov = function() {
        GryfModals.openLovModal(GryfModals.MODALS_URL.LOV_TI, BrowseTrainingInsService, 'lg').result.then(function(chosenTI) {
            $scope.searchDTO.trainingInstitutionName = chosenTI.name;
            $scope.searchDTO.trainingInstitutionVatRegNum = chosenTI.vatRegNum;
        });
    };

    $scope.openTrainingLov = function() {
        GryfModals.openLovModal(GryfModals.MODALS_URL.LOV_TRAININGS, BrowseTrainingService, 'lg').result.then(function(chosenTI) {
            $scope.searchDTO.trainingId = chosenTI.trainingId;
            $scope.searchDTO.trainingName = chosenTI.name;
        });
    };

    $scope.clear();

}]);

angular.module("gryf.trainingInstances").controller("TrainingInstanceModifyController", ["$scope", "$routeParams",
    "GryfModals", "GryfModulesUrlProvider", "BrowseTrainingInsService", "BrowseTrainingService", "TrainingInstanceSearchService", "TrainingInstanceModifyService", "$modal",
function ($scope, $routeParams, GryfModals, GryfModulesUrlProvider, BrowseTrainingInsService, BrowseTrainingService, TrainingInstanceSearchService, TrainingInstanceModifyService, $modal) {

    $scope.trainingInstanceModel = TrainingInstanceSearchService.getTrainingInstanceModel();
    $scope.pinCode = null;
    $scope.violations = TrainingInstanceModifyService.getViolations();

    $scope.MODULES = GryfModulesUrlProvider.MODULES;
    $scope.getUrlFor = GryfModulesUrlProvider.getUrlFor;

    if($routeParams.id) {
        TrainingInstanceSearchService.findDetailsById($routeParams.id);
    }

    TrainingInstanceSearchService.getTiStatuses().success(function(data) {
        $scope.statusesDictionary = data;
    });

    $scope.isInStatus = function(statusId) {
        return $scope.trainingInstanceModel.entity.trainingInstanceStatusId === statusId;
    };

    $scope.cancelTrainingReservation = function() {
        TrainingInstanceModifyService.cancelTrainingReservation($scope.trainingInstanceModel.entity.trainingInstanceId,
                                                                $scope.trainingInstanceModel.entity.trainingInstanceVersion)
            .then(function() {
                TrainingInstanceSearchService.findDetailsById($routeParams.id);
            });
    };

    $scope.confirmReservationPIN = function() {
        var messageText = {
            message: "Ilo???? bon??w zosta??a zmieniona. Czy jeste?? pewny, ??e chcesz zmieni?? ilo???? bon??w?"
        };
        if($scope.trainingInstanceModel.entity.newReservationNum != $scope.trainingInstanceModel.entity.productAssignedNum) {
            GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM, messageText).result.then(function (result) {
                if (!result) {
                    return;
                }
                TrainingInstanceModifyService.confirmPin($scope.trainingInstanceModel.entity.trainingInstanceId, $scope.trainingInstanceModel.entity.pinCode,
                    $scope.trainingInstanceModel.entity.trainingInstanceVersion, $scope.trainingInstanceModel.entity.newReservationNum);
            });
        }else{
            TrainingInstanceModifyService.confirmPin($scope.trainingInstanceModel.entity.trainingInstanceId, $scope.trainingInstanceModel.entity.pinCode,
                $scope.trainingInstanceModel.entity.trainingInstanceVersion, $scope.trainingInstanceModel.entity.newReservationNum);
        }
    };

    $scope.openConfirmModal = function(){
        var modalInstance = $modal.open({
            templateUrl: contextPath + "/templates/modals/modal-confirmPin.html",
            controller: "TrainingInstanceModifyController",
            resolve: {
                data: function () {
                    return $scope.trainingInstanceModel;
                }
            }
        });
    };

    $scope.cancelTrainingReimbursement = function() {
        var messageText = {
            message: "Rozliczenie szkolenia zostanie anulowane. Status rezerwacji szkolenia zmieni si?? z 'Rozliczone' na 'Odbyte'. Czy potwierdzasz operacj???"
        };
        GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM, messageText).result.then(function (result) {
            if (!result) {
                return;
            }
            TrainingInstanceModifyService.cancelTrainingReimbursement($scope.trainingInstanceModel.entity.trainingInstanceId,
                $scope.trainingInstanceModel.entity.trainingInstanceVersion)
                .then(function() {
                    TrainingInstanceSearchService.findDetailsById($routeParams.id);
                });
        });
    };

    $scope.cancelTrainingInstanceDone = function() {
        var messageText = {
            message: "Odbyte szkolenie zostanie anulowane. Status rezerwacji szkolenia zmieni si?? z 'Odbyte' na 'Anulowane'. Czy potwierdzasz operacj???"
        };
        GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM, messageText).result.then(function (result) {
            if (!result) {
                return;
            }
            TrainingInstanceModifyService.cancelTrainingInstanceDone($scope.trainingInstanceModel.entity.trainingInstanceId,
                $scope.trainingInstanceModel.entity.trainingInstanceVersion)
                .then(function() {
                    TrainingInstanceSearchService.findDetailsById($routeParams.id);
                });
        });
    };

    $scope.rejectTrainingInstanceReimb = function() {
        var messageText = {
            message: "Rozliczenie szkolenia zostanie odrzucone. Status szkolenia zmieni si?? z 'Rozliczone' na 'Nie rozliczone'. Czy potwierdzasz operacj???"
        };
        GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM, messageText).result.then(function (result) {
            if (!result) {
                return;
            }
            TrainingInstanceModifyService.rejectTrainingInstanceReimb($scope.trainingInstanceModel.entity.trainingInstanceId,
                $scope.trainingInstanceModel.entity.trainingInstanceVersion)
                .then(function() {
                    TrainingInstanceSearchService.findDetailsById($routeParams.id);
                });
        });
    };

    $scope.openReduceModal = function(){
        var reduceModalInstance = $modal.open({
            templateUrl: contextPath + "/templates/modals/modal-reduceProductNum.html",
            controller: "TrainingInstanceModifyController",
            resolve: {
                data: function () {
                    return $scope.trainingInstanceModel;
                }
            }
        });
    };

    $scope.confirmReduceProductAssignedNum = function() {
        var messageTextNoChange = {
            message: "Ilo???? bon??w nie zosta??a zmieniona."
        };
        var messageTextChange = {
            message: "Ilo???? bon??w zosta??a zmieniona. Potwierdzasz decyzj???"
        };
        if($scope.trainingInstanceModel.entity.newReservationNum != $scope.trainingInstanceModel.entity.productAssignedNum) {
            GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM, messageTextChange).result.then(function (result) {
                if (result) {
                    TrainingInstanceModifyService.reduceProductAssignedNum($scope.trainingInstanceModel.entity.trainingInstanceId,
                        $scope.trainingInstanceModel.entity.trainingInstanceVersion, $scope.trainingInstanceModel.entity.newReservationNum);
                } else {
                    return;
                }
            });
        }
    };

    $scope.isNewProductNum = function() {
        return (($scope.trainingInstanceModel.entity.newReservationNum) && ($scope.trainingInstanceModel.entity.productAssignedNum != $scope.trainingInstanceModel.entity.newReservationNum));
    };

}]);