var app = angular.module('gryf.config', ['ui.router', 'ui.bootstrap', 'gryf.modals', 'ngAnimate', 'gryf.popups',
     'gryf.privileges', 'gryf.tables', 'gryf.exceptionHandler', 'gryf.helpers']);

angular.module('gryf.config').factory('generalExceptionHandlerInterceptor', ['$q', '$injector', '$location',
    function($q, $injector) {

    return {
        responseError: function(rejection) {
            if (rejection.status === 500) {
                if (rejection.data.responseType === 'INVALID_OBJECT_ID_EXCEPTION'){
                    $injector.invoke(['GryfModals', function(GryfModals) {
                        var additionalInfo = {message: rejection.data.message};
                        GryfModals.openModal(GryfModals.MODALS_URL.INVALID_OBJECT_ID, additionalInfo)
                    }]);                    
                } else { // GENERAL_EXCEPTION
                    $injector.invoke(['GryfModals', function(GryfModals) {
                        var additionalInfo = {message: rejection.data.message};
                        GryfModals.openModal(GryfModals.MODALS_URL.ERROR_INFO, additionalInfo);
                    }]);
                }
            }
            return $q.reject(rejection);
        }
    };
}]);

angular.module('gryf.config').config(['$httpProvider', function($httpProvider) {
    $httpProvider.interceptors.push('generalExceptionHandlerInterceptor');
}]);