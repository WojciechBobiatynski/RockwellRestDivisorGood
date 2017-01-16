angular.module("gryf.ti").directive("attachments", ['AttachmentService',
    function (AttachmentService) {
        return {
            restrict: 'E',
            scope: {
                model: '=',
                violations: '=',
                isDisabled: '='
            },
            templateUrl: contextPath + '/templates/directives/attachment.html',
            controller: function ($scope) {
                $scope.modelTypes = {};
                $scope.defaultType = {};
                $scope.maxAttachmentSize = 0;
                $scope.defaultMaxAttachmentSizeInMB = 2;

                $scope.addNew = function () {
                    var newAttachment = {};
                    angular.copy($scope.defaultType, newAttachment);
                    $scope.model.attachments.push(newAttachment);
                };

                $scope.deleteAtt = function (item) {
                    if (!item.id) {
                        var index = $scope.model.attachments.indexOf(item);
                        $scope.model.attachments.splice(index, 1);
                    }
                    else {
                        item.markToDelete = true;
                    }
                };

                $scope.onItemClick = function (item, index) {
                    item.changed = true;
                    item.index = index;
                };

                $scope.getDownloadAttachmentLink = function(attachement) {
                    return attachement.id != null ? contextPath + "/rest/reimbursements/downloadAttachment?id=" + attachement.id : '';
                };

                $scope.getMaxAttachmentSizeInMB = function() {
                    if($scope.maxAttachmentSize == 0 || $scope.maxAttachmentSize === NaN){
                        return $scope.defaultMaxAttachmentSizeInMB;
                    }
                    return $scope.maxAttachmentSize / 1024 / 1024;
                };

            },
            link: function ($scope) {

                function Attachment(code, maxFileSize, required) {
                    this.code = code,
                        this.maxFileSize = maxFileSize,
                        this.required = required
                }

                var unregisterCallback = $scope.$watch('model.grantProgramId', function (newData) {
                    if (newData) {
                        unregisterCallback();
                        AttachmentService.loadAttachmentsTypes($scope.model.grantProgramId).then(function (response) {
                            $scope.modelTypes.types = response.data;
                            $scope.modelTypes.types.sort(function(a,b){
                                return a.ordinal - b.ordinal;
                            });
                            if ($scope.model.attachments === undefined || $scope.model.attachments === null) {
                                $scope.model.attachments = [];
                            }
                            angular.forEach($scope.modelTypes.types, function (type) {
                                if(type.maxBytesPerFile > $scope.maxAttachmentSize) {
                                    $scope.maxAttachmentSize = type.maxBytesPerFile;
                                }

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
                                } else {
                                    $scope.defaultType = new Attachment(type.code, type.maxBytesPerFile, type.required);
                                }
                            });
                        });
                    }
                });
            }
        }
    }]);