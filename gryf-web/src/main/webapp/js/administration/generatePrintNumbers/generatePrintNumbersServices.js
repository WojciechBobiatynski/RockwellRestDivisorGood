'use strict';

angular.module('gryf.generatePrintNumbers').factory("GeneratePrintNumbersService",
    ["$http", "GryfModals", "GryfPopups",
        function ($http, GryfModals, GryfPopups) {

            var PRODUCTS_URL = "/rest/administration/products";
            var GENERATE_PRINT_NUMBERS_URL = contextPath + "/rest/administration/generatePrintNumbers";

            var findProducts = function () {
                return $http.get(contextPath + PRODUCTS_URL);
            };

            var generate = function (product) {
                var workingModal = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Zapisuję dane"});

                var generateNumbersPromise = $http.post(GENERATE_PRINT_NUMBERS_URL, product);

                generateNumbersPromise.then(function () {
                    GryfPopups.setPopup("success", "Sukces", "Poprawnie wygenerowano numery dla bonów");
                    GryfPopups.showPopup();
                });

                generateNumbersPromise.error(function (error) {
                    GryfPopups.setPopup("error", "Błąd", "Nie udało się wygenerować numeru dla bonów");
                    GryfPopups.showPopup();
                });

                generateNumbersPromise.finally(function () {
                    GryfModals.closeModal(workingModal);
                });

                return generateNumbersPromise;
            };

            return {
                findProducts: findProducts,
                generate: generate
            }
}]);