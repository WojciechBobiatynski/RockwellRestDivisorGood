var app = angular.module('gryf.config', ['ui.router', 'ui.bootstrap', 'gryf.modals', 'ngAnimate', 'gryf.popups',
     'gryf.privileges', 'gryf.tables', 'gryf.exceptionHandler', 'gryf.helpers']);

angular.module('gryf.config').factory('generalExceptionHandlerInterceptor', ['$q', '$injector', "$rootScope", "$filter", "$interpolate",
function($q, $injector, $rootScope, $filter, $interpolate) {

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

                        var recipient = $rootScope.FEEDBACK_EMAIL;
                        var subject = "?subject=[Gryf] Zgłoszenie błędu";

                        var params = {
                            requestTime: $filter('date')(new Date(), 'yyyy-MM-dd HH:mm:ss'),
                            requestUrl: rejection.config.url,
                            errorCause: rejection.data.cause,
                            className: rejection.data.className,
                            methodName: rejection.data.methodName,
                            lineNumber: rejection.data.lineNumber
                        };

                        var body = "&body=" + $interpolate("Przyczyna błędu:     {{errorCause}}%0D%0A" +
                                                           "Adres żądania:         {{requestUrl}}%0D%0A" +
                                                           "Data wystąpienia:   {{requestTime}}%0D%0A" +
                                                           "Nazwa klasy:            {{className}}%0D%0A" +
                                                           "Nazwa metody:       {{methodName}}%0D%0A" +
                                                           "Numer linii:              {{lineNumber}}%0D%0A" +
                                                           "------------------------------------------------------%0D%0A" +
                                                           "Prosimy o opisanie w jakim momencie wystąpił błąd:")(params);

                        var additionalInfo = {
                            feedback: {
                                mailtoText: "mailto:" + recipient + subject + body
                            }
                        };
                        GryfModals.openModal(GryfModals.MODALS_URL.UNSUPPORTED_ERROR_INFO, additionalInfo);
                    }]);
                }
            }
            return $q.reject(rejection);
        },
        response: function(response) {
            document.getElementById('timeoutBox').style.opacity = 0;
            document.getElementById('timeoutBox').style.visibility = 'hidden';
            clearTimeout(timers.messageKeeper);
            clearTimeout(timers.timeoutKeeper);
            clearInterval(timers.timerIntervalKeeper);

            timers.messageKeeper = setTimeout(function() {
                document.getElementById('timeoutBox').style.visibility = 'visible';
                document.getElementById('timeoutBox').style.opacity = 1;
                var timeLeft = 59;
                document.getElementById('timerCounter').innerHTML = timeLeft.toString();
                timers.timerIntervalKeeper = setInterval(function() {
                    document.getElementById('timerCounter').innerHTML = timeLeft.toString();
                    timeLeft--;
                }, 1000)
            }, sessionTimeoutInMs - 60000);

            timers.timeoutKeeper = setTimeout(function() {
                document.getElementById('logoutForm').submit();//na wszelki wypadek jakby jeszcze chwile sesja trwała
                location.href = contextPath + '/login';
            }, sessionTimeoutInMs);
            return response;
        }
    };
}]);

angular.module('gryf.config').config(['$httpProvider', function($httpProvider) {
    $httpProvider.defaults.headers.common["X-CSRF-TOKEN"] = xsrf;
    $httpProvider.interceptors.push('generalExceptionHandlerInterceptor');
}]);