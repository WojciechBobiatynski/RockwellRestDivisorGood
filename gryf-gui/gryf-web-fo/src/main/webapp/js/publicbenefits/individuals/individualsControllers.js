"use strict";

var scopeBrowseController;

angular.module("gryf.individuals").controller("searchform.IndividualController",
    ["$scope", "BrowseIndividualsService", "GryfPopups", function ($scope, BrowseIndividualsService, GryfPopups) {
        scopeBrowseController = $scope;
        $scope.searchObjModel = BrowseIndividualsService.getSearchDTO();
        $scope.searchResultOptions = BrowseIndividualsService.getSearchResultOptions();
        gryfSessionStorage.setUrlToSessionStorage();
        GryfPopups.showPopup();

        $scope.loadMore = function () {
            BrowseIndividualsService.loadMore();
        };

        $scope.findIndividuals = function () {
            $scope.searchResultOptions.badQuery = false;
            BrowseIndividualsService.find();
        };

        $scope.clear = function () {
            $scope.searchObjModel = BrowseIndividualsService.getNewSearchDTO();
            $scope.searchResultOptions = BrowseIndividualsService.resetSearchResultOptions();
        };

        $scope.getSortedIndividuals = function (sortColumnName) {
            $scope.searchResultOptions.badQuery = false;
            BrowseIndividualsService.findSortedBy(sortColumnName);
        };

        $scope.getSortingTypeClass = function (columnName) {
            var sortingType = $scope.searchObjModel.entity.sortTypes[0];
            if (columnName == $scope.searchObjModel.entity.sortColumns[0]) {
                return sortingType;
            }
        };
    }]);

var scopeModifyController;
angular.module("gryf.individuals").controller("detailsform.IndividualController",
    ["$scope", "ModifyIndividualsService", 'GryfModals', 'GryfPopups',
        function ($scope, ModifyIndividualsService, GryfModals, GryfPopups) {
            scopeModifyController = $scope;
            $scope.model = ModifyIndividualsService.getNewModel();
            $scope.violations = ModifyIndividualsService.getNewViolations();
            gryfSessionStorage.setUrlToSessionStorage();
            GryfPopups.showPopup();

            var NEW_INDIVIDUAL_URL = contextPath + "/publicBenefits/individuals/#modify";


            $scope.newIndividual = function () {
                var messageText = {
                    message: "Wywołując tę akcję stracisz niezapisane dane "
                };

                GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM, messageText).result.then(function (result) {
                    if (!result) {
                        return;
                    }
                    window.location = NEW_INDIVIDUAL_URL;
                });
            };

            $scope.getPrevUrl = function () {
                return gryfSessionStorage.getUrlFromSessionStorage();
            };

            $scope.openEnterpriseLov = function () {
                ModifyIndividualsService.openEnterpriseLov().result.then(function (chosenEnterprise) {
                    //TODO: brzydko, można przekazać comparator do funkcji porównującej, ewentualnie dodać nowy indexOf do prototype
                    var isNew = true;
                    $scope.model.entity.enterprises.forEach(function (enterprise) {
                        if (enterprise.id == chosenEnterprise.id) {
                            isNew = false;
                        }
                    });
                    if (isNew) {
                        $scope.model.entity.enterprises.push(chosenEnterprise);
                    }
                });
            };

            $scope.removeEnterprise = function (item) {
                var index = $scope.model.entity.enterprises.indexOf(item);
                $scope.model.entity.enterprises.splice(index, 1);
            };

            $scope.loadIndividual = function (id) {
                ModifyIndividualsService.loadIndividuals(id);
            };
            $scope.loadIndividual();


            $scope.loadContactTypes = function () {
                ModifyIndividualsService.loadContactTypes();
            };
            $scope.loadContactTypes();

            $scope.hasNotPrivilege = function (privilege) {
                return !privileges[privilege];
            };

            $scope.saveAndAdd = function () {
                $scope.save(NEW_INDIVIDUAL_URL);
            };

            $scope.save = function (redirectUrl) {
                var messageText = {
                    message: "Ta akcja zapisuje zmiany Osoby fizycznej"
                };

                GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM, messageText).result.then(function (result) {
                    if (!result) {
                        return;
                    }
                    executeSave();
                });

                var executeSave = function () {
                    $scope.violations = ModifyIndividualsService.getNewViolations();
                    ModifyIndividualsService.save().then(function () {
                        if (!redirectUrl) {
                            redirectUrl = $scope.getPrevUrl();
                        }
                        window.location = redirectUrl;
                    });
                };
            };

            $scope.addContact = function () {
                ModifyIndividualsService.addItemToList();
            };

            $scope.removeContact = function (contact) {
                ModifyIndividualsService.removeItemFromList(contact);
            };

            $scope.openInvoiceZipCodesLov = function () {
                ModifyIndividualsService.openZipCodesLov().result.then(function (chosedItem) {
                    $scope.model.entity.zipCodeInvoice = chosedItem;
                });
            };

            $scope.openCorrZipCodesLov = function () {
                ModifyIndividualsService.openZipCodesLov().result.then(function (chosedItem) {
                    $scope.model.entity.zipCodeCorr = chosedItem;
                });
            };

            $scope.isModType = function() {
                return $scope.model.entity.id != null;
            };

            $scope.generateNewVerCode = function() {
                return ModifyIndividualsService.getNewVerificationCode();
            };

            $scope.sendMailWithVerCode = function() {
                ModifyIndividualsService.sendMailWithVerCode();
            };

            $scope.loadIndUserRoles = function () {
                ModifyIndividualsService.loadIndUserRoles();
            };
            $scope.loadIndUserRoles();
        }]);