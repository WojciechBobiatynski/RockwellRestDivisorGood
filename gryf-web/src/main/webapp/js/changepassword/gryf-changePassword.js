/**
 * Created by adziobek on 07.10.2016.
 */
'use strict';
angular.module('gryf.changePassword', ['gryf.config']);
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

