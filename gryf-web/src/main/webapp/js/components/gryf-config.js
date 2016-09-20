var app = angular.module('gryf.config',
    ['ngRoute', 'ui.bootstrap', 'gryf.modals', 'ngAnimate', 'gryf.popups',
     'gryf.privileges', 'gryf.tables', 'gryf.exceptionHandler', 'gryf.helpers']);


angular.module('gryf.config').factory('generalExceptionHandlerInterceptor', ['$q', '$injector', function($q, $injector) {
    var timers = {
        messageKeeper: null,
        timeoutKeeper: null,
        timerIntervalKeeper: null
    };

    document.getElementById('prolongSession').onclick = function() {
        $injector.invoke(['$http', function($http) {
            $http.get(contextPath + '/prolongSession');
        }]);
    };

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
                        GryfModals.openModal(GryfModals.MODALS_URL.ERROR_INFO, additionalInfo)
                    }]);
                }
            }
            return $q.reject(rejection);
        },
        response: function(response) {
            document.getElementById('timeoutBox').style.opacity = 0;
            clearTimeout(timers.messageKeeper);
            clearTimeout(timers.timeoutKeeper);
            clearInterval(timers.timerIntervalKeeper);

            timers.messageKeeper = setTimeout(function() {
                document.getElementById('timeoutBox').style.opacity = 1;
                var timeLeft = 59;
                document.getElementById('timerCounter').innerHTML = timeLeft.toString();
                timers.timerIntervalKeeper = setInterval(function() {
                    document.getElementById('timerCounter').innerHTML = timeLeft.toString();
                    timeLeft--;
                }, 1000)
            }, sessionTimeoutInMs - 60000);

            timers.timeoutKeeper = setTimeout(function() {
                location.href = contextPath + '/logout';
            }, sessionTimeoutInMs);
            return response;
        }
    };
}]);

angular.module('gryf.config').config(['$httpProvider', function($httpProvider) {
    $httpProvider.defaults.headers.common["X-CSRF-TOKEN"] = xsrf;
    $httpProvider.interceptors.push('generalExceptionHandlerInterceptor');
}]);

angular.module('gryf.config').config(['$provide', '$controllerProvider', function($provide, $controllerProvider) {
    app._controller = app.controller;
    app._factory = app.factory;

    // Provider-based controller.
    app.controller = function(name, constructor) {
        $controllerProvider.register(name, constructor);
        return this;
    };
    app.factory = function(name, factory) {
        $provide.factory(name, factory);
        return this;
    };
}]);
