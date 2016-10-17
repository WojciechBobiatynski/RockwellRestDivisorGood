/**
 * Created by adziobek on 07.10.2016.
 */
'use strict';
angular.module('gryf.changePassword', ['gryf.config']);
angular.module('gryf.changePassword').value('VALIDATION_MESSAGES',
    {
        'required': 'Pole jest wymagane !',
        'pattern': 'Pole ma niewłaściwy format !',
        'duplicate': 'Nowe i stare hasło nie mogą być identyczne !',
        'notDuplicate': 'Wpisz identyczne hasła !'
    });

angular.module('gryf.changePassword').config(['$routeProvider', function ($routeProvider) {
    $routeProvider
        .when('/',
            {
                templateUrl: contextPath + '/templates/changepassword/changePassword.html',
            }
        )
        .when('/changePasswordSuccess',
            {
                templateUrl: contextPath + '/templates/changepassword/changePasswordSuccess.html'
            }
        )
        .otherwise(
            {
                redirectTo: '/'
            }
        )
}]);

angular.module('gryf.changePassword').controller('ValidationController', function ($scope, $http, $location) {
    //Minimum 8 znaków, 1 duża i mała litera, 1 liczba i znak specjalny
    $scope.passwordRegex=/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&]{8,}$/;
    $scope.password ;
    $scope.errorMessage ;
    $scope.submitForm = function() {
        if($scope.changePasswordForm.$invalid) {
            $scope.changePasswordForm.oldPassword.$setDirty();
            $scope.changePasswordForm.newPassword.$setDirty();
            $scope.changePasswordForm.newPasswordRepeated.$setDirty();
            return;
        }
        if(($scope.password.oldPassword == $scope.password.newPassword) || ($scope.password.newPassword != $scope.password.newPasswordRepeated)){
            if($scope.password.oldPassword == $scope.password.newPassword) {
                $scope.changePasswordForm.newPassword.$error = {'duplicate': true};
            }
            if($scope.password.newPassword != $scope.password.newPasswordRepeated) {
                $scope.changePasswordForm.newPasswordRepeated.$error = {'notDuplicate': true};
            }
            return;
        }
        $http({
            method: 'POST',
            url: contextPath + '/changePasswordFormData',
            data: $scope.password
        }).then(function successCallback(response){
            if(response.data.status == "OK"){
                $location.path("/changePasswordSuccess");
            }else{
                $scope.errorMessage = response.data.message;
                $location.path("/");
            }
        }, function errorCallback(response){
            $scope.errorMessage = response.data.message;
            $location.path("/");
        });
    };
})

angular.module('gryf.changePassword').directive('localValidationMsg', localValidationMsg);
function localValidationMsg(VALIDATION_MESSAGES) {
    return {
        restrict: 'E',
        replace: true,
        scope: {
            formField: '=',
        },
        template: [
            '<span style="color:red">',
            '<span class="message" ng-repeat="(errName, errState) in formField.$error" ng-if="formField.$touched">',
            '<span ng-bind="messages[errName]"></span><br>',
            '</span>',
            '</span>',
        ].join(''),
        link: function(scope) {
            scope.messages = VALIDATION_MESSAGES;
        }
    }
};
