'use strict';

/*
 * Responsible for modal windows across application
 */
angular.module('gryf.modals', ['ui.bootstrap']);
angular.module('gryf.modals').factory('GryfModals', ['$rootScope', '$modal', function($rootScope, $modal) {
    var MODALS_URL = {
        WORKING: contextPath + "/templates/modals/modal-working.html",
        CONFIRM: contextPath + "/templates/modals/modal-confirm.html",
        VALIDATION: contextPath + "/templates/modals/modal-validation.html",
        VALIDATION_WITH_CONFIRM: contextPath + "/templates/modals/modal-validation-confirm.html",
        CONFLICT: contextPath + "/templates/modals/modal-conflict.html",
        VAT_REG_NUM_CONFLICT: contextPath + "/templates/modals/modal-conflict-vat-reg-num.html",
        PESEL_CONFLICT: contextPath + "/templates/modals/modal-conflict-pesel.html",
        LOV_ZIPCODES: {
            templateUrl: contextPath + "/templates/modals/modal-zipCodes-lov.html",
            restURL: "" //standard rest url for searching on dedicated form
        },
        LOV_ENTERPRISES: {
            templateUrl: contextPath + "/templates/modals/modal-enterprises-lov.html",
            restURL: "" //standard rest url for searching on dedicated form
        },
        LOV_INDIVIDUALS: {
            templateUrl: contextPath + "/templates/modals/modal-individuals-lov.html",
            restURL: "" //standard rest url for searching on dedicated form
        },
        LOV_TI: {
            templateUrl: contextPath + "/templates/modals/modal-trainingInstitution-lov.html",
            restURL: "" //standard rest url for searching on dedicated form
        },
        LOV_TRAINEES: {
            templateUrl: contextPath + "/templates/modals/modal-trainees-lov.html",
            restURL: "" //standard rest url for searching on dedicated form
        },
        LOV_DELIVERY: {
            templateUrl: contextPath + "/templates/modals/modal-delivery-lov.html",
            restURL: contextPath + "/rest/publicBenefits/reimbursements/reimbursableDeliveries"
        },
        ERROR_INFO: contextPath + "/templates/modals/modal-error.html",
        INVALID_OBJECT_ID: contextPath + "/templates/modals/modal-invalid-object-id.html"
    };
    var MODAL_RESULT = {
        REFRESH: "refresh",
        FORCE: "force"
    };

    var openLovModal = function(modalUrls, HandlerService, size) {
        return $modal.open({
            templateUrl: modalUrls.templateUrl,
            keyboard: false,
            size: size,
            controller: function($scope) {
                $scope.find();
                $scope.$on('keydown:27', function(onEvent, keypressEvent) {
                    $scope.$dismiss();
                });
                // TODO MGU dopracowac dla generycznego przypadku, ponizej
                // przykladdla lov-a dostawy na rozliczeniu

                //HandlerService.getDeliveryStatuses().then(function(response) {
                //    $scope.deliveryStatuses = response.data;
                //})
            },
            scope: angular.extend($rootScope.$new(), {
                list: [],
                //TODO MGU jw.
                //deliveryStatuses: null,
                model: HandlerService.getSearchDTO(),
                searchResultOptions: HandlerService.getSearchResultOptions(),
                find: function() {
                    var handler = this;
                    HandlerService.find(modalUrls.restURL).then(function(response) {
                        handler.list = response.data;
                    });
                },
                loadMore: function() {
                    var handler = this;
                    HandlerService.loadMore().then(function(response) {
                        handler.list = response.data;
                    });
                }
            })
        });
    };

    var openModal = function(templateUrl, additionalTextData) {
        if (!additionalTextData) {
            additionalTextData = {};
        }
        return $modal.open({
            templateUrl: templateUrl,
            keyboard: false,
            controller: function($scope) {
                $scope.$on('keydown:13', function(onEvent, keypressEvent) {
                    $scope.$close(true);
                });
                $scope.$on('keydown:27', function(onEvent, keypressEvent) {
                    $scope.$dismiss();
                });
            },
            scope: angular.extend($rootScope.$new(), {
                workingReason: additionalTextData.label,
                violations: additionalTextData.violations,
                staleData: additionalTextData.staleData,
                conflictedObjects: additionalTextData.conflictedObjects,
                message: additionalTextData.message
            })
        });
    };

    var closeModal = function(modalInstance) {
        modalInstance.opened.then(function() {
            setTimeout(function() {
                modalInstance.close();
            }, 300);
        });
    };

    return {
        openLovModal: openLovModal,
        openModal: openModal,
        closeModal: closeModal,
        MODALS_URL: MODALS_URL,
        MODAL_RESULT: MODAL_RESULT
    };
}]);

angular.module('gryf.modals').directive('keypressEvents', [
    '$document', '$rootScope', function($document, $rootScope) {
        return {
            restrict: 'A',
            link: function() {
                $document.bind('keydown', function(e) {
                    $rootScope.$broadcast('keydown:' + e.which, e);
                });
            }
        };
    }
]);


angular.module('gryf.exceptionHandler', ['gryf.modals']);
angular.module('gryf.exceptionHandler').factory('GryfExceptionHandler', ['GryfModals', function(GryfModals) {
    var ERRORS = {
        STALE_USER_DATA: "STALE_USER_DATA",
        VALIDATION_ERROR: "VALIDATION_ERROR",
        VALIDATION_WITH_CONFIRM_ERROR: "VALIDATION_WITH_CONFIRM_ERROR",
        VAT_REG_NUM_ENTERPRISE_CONFLICT: "VAT_REG_NUM_ENTERPRISE_CONFLICT",
        PESEL_INDIVIDUAL_CONFLICT: "PESEL_INDIVIDUAL_CONFLICT",
        VAT_REG_NUM_TRAINING_INSTITUTION_CONFLICT: "VAT_REG_NUM_TRAINING_INSTITUTION_CONFLICT"

    };

    var handleSavingError = function(error, scopeViolationsObject, conflictModalCallbacks) {
        switch (error.responseType) {
            case ERRORS.VAT_REG_NUM_ENTERPRISE_CONFLICT:
                var conflictedObjects = error.enterprises;
                GryfModals.openModal(GryfModals.MODALS_URL.VAT_REG_NUM_CONFLICT,
                    {
                        message: error.message,
                        conflictedObjects: conflictedObjects
                    }).result.then(function(result) {
                        switch (result) {
                            case GryfModals.MODAL_RESULT.REFRESH:
                                conflictModalCallbacks.refresh();
                                break;
                            case GryfModals.MODAL_RESULT.FORCE:
                                conflictModalCallbacks.force();
                                break;
                        }
                    });
                break;
            case ERRORS.PESEL_INDIVIDUAL_CONFLICT:
                var conflictedObjects = error.individuals;
                GryfModals.openModal(GryfModals.MODALS_URL.PESEL_CONFLICT,
                    {
                        message: error.message,
                        conflictedObjects: conflictedObjects
                    }).result.then(function(result) {
                        switch (result) {
                            case GryfModals.MODAL_RESULT.REFRESH:
                                conflictModalCallbacks.refresh();
                                break;
                            case GryfModals.MODAL_RESULT.FORCE:
                                conflictModalCallbacks.force();
                                break;
                        }
                    });
                break;
            case ERRORS.VAT_REG_NUM_TRAINING_INSTITUTION_CONFLICT:
                var conflictedObjects = error.trainingInstitutions;
                GryfModals.openModal(GryfModals.MODALS_URL.VAT_REG_NUM_CONFLICT,
                    {
                        message: error.message,
                        conflictedObjects: conflictedObjects
                    }).result.then(function(result) {
                        switch (result) {
                            case GryfModals.MODAL_RESULT.REFRESH:
                                conflictModalCallbacks.refresh();
                                break;
                            case GryfModals.MODAL_RESULT.FORCE:
                                conflictModalCallbacks.force();
                                break;
                        }
                    });
                break;
            case ERRORS.STALE_USER_DATA:
                var staleData = {};
                staleData.modifiedUser = error.modifiedUser;
                staleData.modifiedTimestamp = error.modifiedTimestamp;
                staleData.message = error.message;
                staleData.containObjectInfo = error.containObjectInfo;

                GryfModals.openModal(GryfModals.MODALS_URL.CONFLICT, {staleData: staleData})
                    .result
                    .then(function(result) {
                        switch (result) {
                            case GryfModals.MODAL_RESULT.REFRESH:
                                conflictModalCallbacks.refresh();
                                break;
                            case GryfModals.MODAL_RESULT.FORCE:
                                conflictModalCallbacks.force();
                                break;
                        }
                    });
                break;
            case ERRORS.VALIDATION_ERROR:
                GryfModals.openModal(GryfModals.MODALS_URL.VALIDATION, {violations: error.violations});
                error.violations.forEach(function(element) {
                    scopeViolationsObject[element.path] = element;
                });
                break;
            case ERRORS.VALIDATION_WITH_CONFIRM_ERROR:
                GryfModals.openModal(GryfModals.MODALS_URL.VALIDATION_WITH_CONFIRM, {violations: error.violations})
                    .result
                    .then(function(result) {
                        if (result === GryfModals.MODAL_RESULT.FORCE) {
                            var pathArray = [];
                            error.violations.forEach(function(element) {
                                pathArray.push(element.path);
                            });
                            conflictModalCallbacks.force(pathArray)
                        }
                    });
                error.violations.forEach(function(element) {
                    scopeViolationsObject[element.path] = element;
                });
                break;
        }
    };

    return {
        handleSavingError: handleSavingError,
        ERRORS: ERRORS
    }
}]);

angular.module('gryf.exceptionHandler').directive('gryfValidationMsg', function() {
    return {
        restrict: 'E',
        replace: true,
        scope: true,
        template: "<span class='msg msg-error' ng-show='violations[path]'>{{violations[path].message}}</span>",
        link: function(scope, element, attrs) {
            scope.path = attrs['path'];
        }
    }
});

angular.module('gryf.privileges', []);
angular.module('gryf.privileges').directive('gryfPrivilege', function() {
    return {
        restrict: 'A',
        priority: -1,
        link: function(scope, element, attrs) {
            if (!attrs['gryfPrivilege']) {
                return;
            }
            if (!privileges[attrs['gryfPrivilege']]) {
                var elemTagName = element[0].tagName;

                if (elemTagName === 'A') {
                    element.remove();
                }

                if (elemTagName === 'INPUT' || elemTagName === 'SELECT' || elemTagName === 'TEXTAREA') {
                    element.prop('disabled', true);
                }
            }
        }
    };
});

angular.module('gryf.privileges').directive('gryfLinkPrivilege', function() {
    return {
        restrict: 'A',
        priority: -1,
        link: function(scope, element, attrs) {
            if (!attrs['gryfLinkPrivilege']) {
                return;
            }
            if (!privileges[attrs['gryfLinkPrivilege']]) {

                var resetAttribute = function(attribute, attributeString) {
                    if (attribute) {
                        if (attribute.length) {
                            attrs.$set(attributeString, "");
                        }
                    }
                };

                var elemTagName = element[0].tagName;

                if (elemTagName === 'A') {
                    attrs.$observe('ngHref', function(ngHref) {
                        resetAttribute(ngHref, 'ngHref');
                    });

                    attrs.$observe('href', function(href) {
                        resetAttribute(href, "href");
                    });

                    attrs.$addClass("disabledLink");
                }
            }
        }
    };
});

angular.module("gryf.privileges").directive('gryfDisabledForm', function() {
    return {
        restrict: 'A',
        link: function(scope, elem, attr) {
            elem.find('input').prop('disabled', true);
            elem.find('select').prop('disabled', true);
            elem.find('a').remove();
        }
    }
});

angular.module("gryf.privileges").directive("gryfDataModificationFlags", function() {
    return {
        restrict: 'A',
        require: '?ngModel',
        link: function(scope, elem, attr, ngModel) {
            var flags = attr["gryfDataModificationFlags"];
            var fileName = scope["item"]["fileName"];

            //check whether it isn't disabled by privileges already
            if (!elem.prop('disabled')) {
                scope.$watch(function() {
                    return ngModel.$modelValue;
                }, function(value) {
                    performCheck(value);
                    if (fileName) {
                        performAttachmentCheck(value);
                    }
                });
            }

            function performCheck(value) {
                elem.prop('disabled', true);

                //insertable
                if (flags.indexOf("I") > -1) {
                    if (value == undefined) {
                        elem.prop('disabled', false);
                    }
                }
                //updatable
                if (flags.indexOf("U") > -1) {
                    if (value !== undefined) {
                        elem.prop('disabled', false);
                        elem.removeClass("disabledLink");
                    }
                }
            }

            function performAttachmentCheck(modelValue) {
                elem.addClass("disabledLink");

                //insertable
                if (flags.indexOf("I") > -1) {
                    if (modelValue == undefined && fileName == undefined) {
                        elem.removeClass("disabledLink");
                    }
                }

                //updatable
                if (flags.indexOf("U") > -1) {
                    if (modelValue !== undefined || fileName !== undefined) {
                        elem.removeClass("disabledLink");
                    }
                }
            }
        }
    };
});

angular.module("gryf.privileges").directive("gryfMandatoryFlag", function() {
    return {
        restrict: 'A',
        scope: {
            gryfMandatoryFlag: '@'
        },
        link: function(scope, elem, attr) {
            var flags = scope.gryfMandatoryFlag;
            //mandatory
            if (flags.indexOf("M") > -1) {
                elem.addClass('required');
            }
        }
    }
});


angular.module('gryf.tables', []);
angular.module('gryf.tables').factory('GryfTables', function() {
    var SORTING_TYPE = {
        ASCENDING: "ASC",
        DESCENDING: "DESC"
    };

    var getSortingTypeClass = function(searchObjectEntity, sortingColumnName) {
        var sortingType = searchObjectEntity.sortTypes[0];
        if (sortingColumnName == searchObjectEntity.sortColumns[0]) {
            return sortingType;
        }
    };

    var sortByColumn = function(searchObjectEntity, sortColumnName) {
        var sortColumns = searchObjectEntity.sortColumns;
        var sortTypes = searchObjectEntity.sortTypes;

        if (sortColumns != null && sortColumns.length > 0 &&
            sortTypes != null && sortTypes.length > 0) {
            if (sortColumns[0] === sortColumnName) {
                searchObjectEntity.sortTypes = (sortTypes[0] === SORTING_TYPE.ASCENDING) ? [SORTING_TYPE.DESCENDING] : [SORTING_TYPE.ASCENDING];
            }
            else {
                searchObjectEntity.sortTypes = ['ASC'];
            }
        } else {
            searchObjectEntity.sortTypes = ['ASC'];
        }
        searchObjectEntity.sortColumns = [sortColumnName];

        return searchObjectEntity;
    };

    return {
        sortByColumn: sortByColumn,
        getSortingTypeClass: getSortingTypeClass
    }
});

angular.module('gryf.popups', ['ngAnimate', 'toastr']);
angular.module('gryf.popups').factory('GryfPopups', ['toastr', function(toastr) {
    var showPopup = function() {
        var msg = gryfSessionStorage.getMessage();

        if (msg) {
            switch (msg.type) {
                case "success":
                    toastr.success(msg.message, msg.title);
                    break;
                case "error":
                    toastr.error(msg.message, msg.title);
                    break;
                case "info":
                    toastr.info(msg.message, msg.title);
                    break;
                case "warning":
                    toastr.warning(msg.message, msg.title);
                    break;
            }
        }
    };

    var setPopup = function(type, title, message) {
        gryfSessionStorage.setMessageTo(type, title, message);
    };

    var showNow = function() {
        toastr.success("amazin success", "truly amazing success");
    };

    return {
        setPopup: setPopup,
        showPopup: showPopup,
        showNow: showNow
    };
}]);

angular.module('gryf.helpers', []);
angular.module('gryf.helpers').factory('GryfHelpers', [function() {
    
    var copyPropertiesIfExist = function(destination, source) {
        for (var k in source) {
            if (source.hasOwnProperty(k) && destination.hasOwnProperty(k)) {
                destination[k] = source[k];
            }
        }
        return destination;
    };

    var parseDateFromTimestamp = function(list) {
        for (var i = 0; i < list.length; i++) {
            var timestamp = list[i]["valueDate"];
            if (timestamp) {
                list[i]["valueDate"] = new Date(timestamp)
            }
        }
    };

    var parseFromTimestampToDate = function(timestamp, withTime) {
        var date = new Date(timestamp);
        var separator = "-";
        var timeSeparator = ":";

        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var day = date.getDate();
        var hours = date.getHours();
        var minutes = date.getMinutes();
        var seconds = date.getSeconds();
        if (withTime) {
            return year + separator + month + separator + day + " " + hours + timeSeparator + minutes + timeSeparator + seconds
        }

        return year + separator + month + separator + day;
    };

    var transformDatesToString = function(entity) {
        for (var el in entity) {
            if (entity.hasOwnProperty(el)) {
                if (Object.prototype.toString.call(entity[el]) === '[object Date]') {
                    entity[el] = transform(entity[el]);
                }
            }
        }

        function transform(date) {
            var separator = "-";
            var year = date.getFullYear();
            var month = date.getMonth() + 1;
            var day = date.getDate();
            return year + separator + (month < 10 ? '0' + month : month) + separator + (day < 10 ? '0' + day : day);
        }
    };


    var isInFormat = function(item, value) {
        var params = item.elementTypeParams;
        var parametersArray;
        if (params) {
            parametersArray = params.split(';');
        } else {
            return false;
        }
        for (var i = 0; i < parametersArray.length; i++) {
            var param = parametersArray[i];
            var paramArray = param.split("=");
            if (paramArray[0].trim() === "FORMAT") {
                return paramArray[1].trim() === value;
            }
        }
        return false;
    };

    var isType = function(item, value) {
        var params = item.elementTypeParams;
        var parametersArray;
        if (params) {
            parametersArray = params.split(';');
        } else {
            return false;
        }
        for (var i = 0; i < parametersArray.length; i++) {
            var param = parametersArray[i];
            var paramArray = param.split("=");
            if (paramArray[0].trim() === "TYPE") {
                return paramArray[1].trim() === value;
            }
        }
        return false;
    };


    return {
        copyPropertiesIfExist: copyPropertiesIfExist,
        parseDateFromTimestamp: parseDateFromTimestamp,
        isInFormat: isInFormat,
        parseFromTimestampToDate: parseFromTimestampToDate,
        transformDatesToString: transformDatesToString,
        isType: isType
    }
}]);

angular.module('gryf.helpers').directive('focusOnF7', [function() {
    return {
        restrict: 'A',
        link: function(scope, elem, attr) {
            scope.$on('keydown:118', function() {
                elem[0].focus();
            });
        }
    }
}]);

angular.module('gryf.helpers').directive('searchOnEnter', ['$rootScope', function($rootScope) {
    return {
        restrict: 'A',
        link: function(scope, elem) {
            elem.bind('keydown', function(e) {
                if (event.which === 13) {
                    $rootScope.$broadcast('triggerFind');
                }
            });
        }
    }
}]);

angular.module('gryf.helpers').factory('GryfModulesUrlProvider', [function() {
    
    var LINKS = {
        GrantApplication : contextPath + "/publicBenefits/grantApplications/#/modify/",
        Order : contextPath + "/publicBenefits/orders/#/modify/",
        Enterprise : contextPath + "/publicBenefits/enterprises/#/modify/",
        Individual : contextPath + "/publicBenefits/individuals/#/modify/"
    };
    
    var MODULES = {
        GrantApplication : 'GrantApplication',
        Order : 'Order',
        Enterprise : 'Enterprise',
        Individual : 'Individual'
    };
    
    var getUrlFor = function(module, objectId){
        return LINKS[module] + objectId;
    };
    
    return {getUrlFor : getUrlFor,
            MODULES   : MODULES};
}]);

angular.module('gryf.helpers').factory('AltShortcutHandler', ['$document', '$rootScope', function($document, $rootScope) {
    var altDown = false;

    $rootScope.$on('keydown:17', function(onEvent, keypressEvent) {
        altDown = true;
    });

    $document.bind("keyup", function(event) {
        if (event.which === 17) {         //altKey
            altDown = false;
        }
    });

    var attachAltShortcut = function(letterCode, callbackFn) {
        $rootScope.$on('keydown:' + letterCode, function(onEvent, keypressEvent) {
            if ((keypressEvent.which === letterCode) && altDown) {
                callbackFn();
            }
        });
    };

    return {
        attachAltShortcut: attachAltShortcut
    };
}]);

