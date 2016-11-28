angular.module("gryf.ti").directive("attachments", ['AttachmentService',
    function (AttachmentService) {
        return {
            restrict: 'E',
            scope: {
                trainingInstance: '=',
                model: '='
            },
            templateUrl: contextPath + '/templates/directives/attachment.html',
            controller: function ($scope) {
                $scope.modelTypes = {};

                $scope.addNew = function () {
                    $scope.model.attachments.push({});
                };

                $scope.deleteAtt = function (item) {
                    var index = $scope.model.attachments.indexOf(item);
                    $scope.model.attachments.splice(index, 1);
                };

                $scope.onItemClick = function (item, index) {
                    item.changed = true;
                    item.index = index;
                }

            },
            link: function ($scope) {

                function Attachment(code, maxFileSize, required) {
                    this.code = code,
                    this.maxFileSize = maxFileSize,
                    this.required = required
                };

                $scope.$watch('trainingInstance.grantProgramId', function (newData) {
                    if (newData) {
                        AttachmentService.loadAttachmentsTypes($scope.trainingInstance.grantProgramId).then(function (response) {
                            $scope.modelTypes.types = response.data;
                            if ($scope.model.attachments === undefined || $scope.model.attachments === null) {
                                $scope.model.attachments = [];
                            }
                            angular.forEach($scope.modelTypes.types, function (type) {
                                if (type.required) {
                                    var result = false;
                                    angular.forEach($scope.model.attachments, function (att) {
                                        if (att.code == type.code) {
                                            result = true;
                                        }
                                    });
                                    if (!result) {
                                        $scope.model.attachments.push(new Attachment(type.code, type.maxBytesPerFile, type.required))
                                    }
                                }
                            });
                        });
                    }
                });
            }
        }
    }]);