angular.module("gryf.ti").directive("attachments", ['AttachmentService',
    function (AttachmentService) {
        return {
            restrict: 'E',
            scope: {
                model: '='
            },
            templateUrl: contextPath + '/templates/directives/attachment.html',
            controller: function ($scope) {
                $scope.addNew = function(){
                    $scope.model.attachments.push({});
                }
            },
            link: function($scope){
                $scope.$watch('model.grantProgramId', function(newData) {
                    if(newData){
                        AttachmentService.loadAttachmentsTypes($scope.model.grantProgramId).then(function (response) {
                            $scope.model.types = response.data;
                            if($scope.model.attachments === undefined){
                                $scope.model.attachments = [];
                                angular.forEach($scope.model.types, function(type) {
                                    if(type.required) {
                                        $scope.model.attachments.push(type)
                                    }
                                });
                            }
                        });
                    }
                });
            }
        }
    }]);