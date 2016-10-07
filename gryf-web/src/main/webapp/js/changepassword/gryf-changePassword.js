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
