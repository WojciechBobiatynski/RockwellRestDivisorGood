"use strict";

angular.module('gryf.generatePrintNumbers').controller('GeneratePrintNumbersController',
    ["$scope", "$log", 'GryfModals', 'GeneratePrintNumbersService',
        function ($scope, $log, GryfModals, GeneratePrintNumbersService) {

            $scope.model = {};
            $scope.model.product = {};

            $scope.findProducts = function () {
                GeneratePrintNumbersService.findProducts().then(function (response) {
                    $scope.model.products = response.data;
                });
            };
            $scope.findProducts();

            $scope.generate = function () {

                var messageText = {
                    message: "Wywołując tę akcję wygenerujesz numery dla bonów"
                };
                GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM, messageText).result.then(function (result) {
                    if (!result) {
                        return;
                    }
                    executeGenerate();
                });

                function executeGenerate() {

                    var generatePromise = GeneratePrintNumbersService.generate($scope.model.product);
                    generatePromise.then(function () {
                       //TODO obsługa komunikatów
                    });

                    generatePromise.error(function () {
                        //TODO obsługa komunikatów
                    });

                }
            }
        }]);