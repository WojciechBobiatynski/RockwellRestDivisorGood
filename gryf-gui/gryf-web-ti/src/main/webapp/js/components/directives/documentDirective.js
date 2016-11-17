angular.module("gryf.ti").directive("documents", [
    function () {
        return {
            restrict: 'E',
            templateUrl: contextPath + '/templates/directives/document.html'
        }
    }]);