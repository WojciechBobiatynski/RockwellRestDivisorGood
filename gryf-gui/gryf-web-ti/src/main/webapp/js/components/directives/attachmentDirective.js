angular.module("gryf.ti").directive("attachments", ['AttachmentService',
    function (AttachmentService) {
        return {
            restrict: 'E',
            scope: {
                model: '='
            },
            templateUrl: contextPath + '/templates/directives/attachment.html',
            controller: function ($scope) {
                $scope.addNew = function () {
                    $scope.model.attachments.push({});
                };

                $scope.deleteAtt = function (item) {
                    var index = $scope.model.attachments.indexOf(item);
                    $scope.model.attachments.splice(index, 1);
                };

            },
            link: function ($scope) {

                function Attachment(code, maxFileSize, required) {
                    this.code = code,
                    this.maxFileSize = maxFileSize,
                    this.required = required
                };

                $scope.$watch('model.trainingInstance.grantProgramId', function (newData) {
                    if (newData) {
                        AttachmentService.loadAttachmentsTypes($scope.model.trainingInstance.grantProgramId).then(function (response) {
                            $scope.model.types = response.data;
                            if ($scope.model.attachments === undefined) {
                                $scope.model.attachments = [];
                            }
                            angular.forEach($scope.model.types, function (type) {
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