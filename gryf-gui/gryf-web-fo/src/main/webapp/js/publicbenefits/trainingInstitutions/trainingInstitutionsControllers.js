"use strict";

var scopeBrowseController;

angular.module("gryf.trainingInstitutions").controller("searchform.TrainingInsController",
    ["$scope", "BrowseTrainingInsService", 'GryfPopups', 'GryfModals',
        function ($scope, BrowseTrainingInsService, GryfPopups, GryfModals) {
        scopeBrowseController = $scope;
        $scope.searchObjModel = BrowseTrainingInsService.getSearchDTO();
        $scope.loggedUserTiModel = BrowseTrainingInsService.getLoggedUserTI();
        $scope.searchResultOptions = BrowseTrainingInsService.getSearchResultOptions();
        gryfSessionStorage.setUrlToSessionStorage();
        GryfPopups.showPopup();

        $scope.loadMore = function () {
            BrowseTrainingInsService.loadMore();
        };

        $scope.find = function () {
            $scope.searchResultOptions.badQuery = false;
            BrowseTrainingInsService.find();
        };

        $scope.clear = function () {
            $scope.searchObjModel = BrowseTrainingInsService.getNewSearchDTO();
            $scope.searchResultOptions = BrowseTrainingInsService.resetSearchResultOptions();
        };

        $scope.getSorted = function (sortColumnName) {
            $scope.searchResultOptions.badQuery = false;
            BrowseTrainingInsService.findSortedBy(sortColumnName);
        };

        $scope.getSortingTypeClass = function (columnName) {
            var sortingType = $scope.searchObjModel.entity.sortTypes[0];
            if (columnName == $scope.searchObjModel.entity.sortColumns[0]) {
                return sortingType;
            }
        };

        $scope.loadLoggedUserTI = function () {
            BrowseTrainingInsService.loadLoggedUserTI();
        };
        $scope.loadLoggedUserTI();

        $scope.showSwitchUserTiModal = function () {
            GryfModals.openModal(GryfModals.MODALS_URL.SWITCH_USER_TI, {message: "Resetuj do domyślnego IS."})
                .result.then(function (result) {
                    if (result === 'reset') {
                        BrowseTrainingInsService.resetLoggedUserTI();
                    }
            })
        }
    }]);

var scopeModifyController;
angular.module("gryf.trainingInstitutions").controller("detailsform.TrainingInsController",
    ["$scope", "ModifyTrainingInsService", 'GryfModals', 'GryfPopups', "GryfModulesUrlProvider",
        function ($scope, ModifyTrainingInsService, GryfModals, GryfPopups, GryfModulesUrlProvider) {
            scopeModifyController = $scope;
            $scope.model = ModifyTrainingInsService.getNewModel();
            $scope.violations = ModifyTrainingInsService.getNewViolations();
            GryfPopups.showPopup();

            $scope.goBack = function() {
                var callback = function() {
                    window.location = GryfModulesUrlProvider.getBackUrl(GryfModulesUrlProvider.MODULES.TrainingInstitution);
                };
                $scope.showAcceptModal("Wywołując tę akcję stracisz niezapisane dane", callback);
            };

            $scope.newTrainingInstitution = function() {
                var callback = function() {
                    window.location = GryfModulesUrlProvider.LINKS.TrainingInstitution;
                };
                $scope.showAcceptModal("Wywołując tę akcję stracisz niezapisane dane", callback);
            };

            $scope.loadTI = function (id) {
                ModifyTrainingInsService.load(id);
            };
            $scope.loadTI();

            $scope.loadContactTypes = function () {
                ModifyTrainingInsService.loadContactTypes();
            };
            $scope.loadContactTypes();

            $scope.saveAndAdd = function () {
                var saveCallback = function () {
                    $scope.violations = ModifyTrainingInsService.getNewViolations();
                    ModifyTrainingInsService.save().success(function () {
                        window.location = GryfModulesUrlProvider.LINKS.TrainingInstitution;
                    });
                };
                $scope.showAcceptModal("Ta akcja zapisuje zmiany w IS", saveCallback);
            };

            $scope.save = function () {
                var saveCallback = function () {
                    $scope.violations = ModifyTrainingInsService.getNewViolations();
                    ModifyTrainingInsService.save().success(function (id) {
                        window.location = GryfModulesUrlProvider.getUrlFor(GryfModulesUrlProvider.MODULES.TrainingInstitution, id);
                        ModifyTrainingInsService.load(id);
                    });
                };
                $scope.showAcceptModal("Ta akcja zapisuje zmiany w IS", saveCallback);
            };

            $scope.showAcceptModal = function(messageText, callback) {
                var message = {message: messageText};
                GryfModals.openModal(GryfModals.MODALS_URL.CONFIRM, message).result.then(function(result) {
                    if (!result) {
                        return;
                    }
                    callback();
                });
            };

            $scope.addContact = function () {
                ModifyTrainingInsService.addItemToList();
            };

            $scope.addContact = function (contact) {
                ModifyTrainingInsService.addContact(contact);
            };

            $scope.openInvoiceZipCodesLov = function () {
                ModifyTrainingInsService.openZipCodesLov().result.then(function (chosedItem) {
                    $scope.model.entity.zipCodeInvoice = chosedItem;
                });
            };

            $scope.openCorrZipCodesLov = function () {
                ModifyTrainingInsService.openZipCodesLov().result.then(function (chosedItem) {
                    $scope.model.entity.zipCodeCorr = chosedItem;
                });
            };

            $scope.addTiUser = function () {
                ModifyTrainingInsService.addTiUserToList();
            };

            $scope.sendResetLink = function (user) {
                ModifyTrainingInsService.sendResetLink(user);
            };

            $scope.isModType = function (user) {
                return user.id != null;
            };

            $scope.loadTiUserRoles = function () {
                ModifyTrainingInsService.loadTiUserRoles();
            };
            $scope.loadTiUserRoles();

            $scope.removeContact = function (contact) {
                ModifyTrainingInsService.removeContact(contact);
            };

            $scope.removeUser = function (user) {
                var index = $scope.model.entity.users.indexOf(user);
                $scope.model.entity.users.splice(index, 1);
            };

            $scope.joinTi = function () {
                if ($scope.model.entity.id !== null) {
                    var callback = function () {
                            ModifyTrainingInsService.joinTi();
                    };
                    $scope.showAcceptModal("Wywołując tę akcję stracisz niezapisane dane", callback);
                }
            }

        }]);
