var scopeReimbursementsPatternV1Controller =
    angular.module('gryf.reimbursements').controller("announce.ReimbursementsPatternV1Controller",
        ['$scope', 'AnnounceReimbursementsService', function($scope, AnnounceReimbursementsService) {
            scopeReimbursementsPatternV1Controller = $scope;
            $scope.choosedTraining = null;
            $scope.trainingIndex = null;
            $scope.grantOwnerAidProduct = null;

            $scope.datepicker = {
                transferDateOpened: false,
                correctionDateOpened: false
            };

            $scope.traineeWindowOpened = false;

            $scope.openDatepicker = function(field) {
                $scope.datepicker[field] = true;
            };

            $scope.closeTraineeLov = function() {
                $scope.traineeWindowOpened = false;
            };

            $scope.openTraineesLov = function(item, trainingIndex) {
                $scope.choosedTraining = item;
                $scope.traineeWindowOpened = true;
                $scope.trainingIndex = trainingIndex;
            };

            $scope.addTrainee = function() {
                if (!$scope.choosedTraining.reimbursementTrainees) {
                    $scope.choosedTraining.reimbursementTrainees = [];
                }
                $scope.choosedTraining.reimbursementTrainees.push({});
                AnnounceReimbursementsService.populateMandatoryAttachments($scope.choosedTraining.reimbursementTrainees);
            };

            $scope.getDownloadAttachmentLink = function(attachement) {
                return contextPath + "/publicBenefits/reimbursements/downloadAttachment?id=" + attachement.id;
            };

            $scope.getDownloadTraineeAttachmentLink = function(attachement) {
                return contextPath + "/publicBenefits/reimbursements/trainee/downloadAttachment?id=" + attachement.id;
            };


            $scope.addAttachmentFor = function(item) {
                if (!item.reimbursementTraineeAttachments) {
                    item.reimbursementTraineeAttachments = [];
                }
                item.reimbursementTraineeAttachments.push({});
            };

            $scope.openEnterpriseLov = function() {
                AnnounceReimbursementsService.openEnterpriseLov().result.then(function(chosenEnterprise) {
                    $scope.entityObject.enterprise = chosenEnterprise;
                });
            };

            AnnounceReimbursementsService.getGrantOwnerAidProduct().then(function(response) {
                $scope.grantOwnerAidProduct = response.data;
            });

            $scope.removeTraining = function(training) {
                var index = $scope.entityObject.reimbursementTrainings.indexOf(training);
                $scope.entityObject.reimbursementTrainings.splice(index, 1);
            };

            $scope.getGrantOwnerAidProductName = function(itemId) {
                if ($scope.grantOwnerAidProduct) {
                    for (var i = 0; i < $scope.grantOwnerAidProduct.length; i++) {
                        if ($scope.grantOwnerAidProduct[i].id === itemId) {
                            return $scope.grantOwnerAidProduct[i].name;
                        }
                    }
                }
            };
        }]
    );
