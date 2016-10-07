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
        .otherwise(
            {
                redirectTo: '/'
            }
        )
}]);

angular.module('gryf.changePassword').controller('ValidationController', function ($scope) {
    //Minimum 8 znaków, 1 duża i mała litera, 1 liczba i znak specjalny
    $scope.passwordRegex=/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&]{8,}$/;
})

