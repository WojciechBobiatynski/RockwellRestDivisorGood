"use strict";

var scopeBrowseOrdersController;
angular.module('gryf.orders').controller("searchform.OrdersController",
    ['$scope', 'BrowseOrdersService', 'GryfTables', 'GryfPopups',
     function($scope, BrowseOrdersService, GryfTables, GryfPopups) {
         scopeBrowseOrdersController = $scope;
         $scope.searchDTO = BrowseOrdersService.getSearchDTO();
         $scope.searchResultOptions = BrowseOrdersService.getSearchResultOptions();
         gryfSessionStorage.setUrlToSessionStorage();
         GryfPopups.showPopup();

         $scope.datepicker = {
             minRequiredDateFromOpened: false,
             minRequiredDateToOpened: false,
             orderDateFromOpened: false,
             orderDateToOpened: false
         };

         $scope.openDatepicker = function(value) {
             $scope.datepicker[value] = true;
         };

         $scope.find = function() {
             BrowseOrdersService.find();
         };

         $scope.getSortingTypeClass = function(columnName) {
             return GryfTables.getSortingTypeClass($scope.searchDTO.entity, columnName);
         };

         $scope.loadMore = function() {
             BrowseOrdersService.loadMore();
         };

         $scope.getSortedBy = function(columnName) {
             $scope.searchResultOptions.badQuery = false;
             GryfTables.sortByColumn($scope.searchDTO.entity, columnName);
             $scope.find();
         };

         $scope.clear = function() {
             $scope.searchDTO = BrowseOrdersService.getNewSearchDTO();
             $scope.searchResultOptions = BrowseOrdersService.getNewSearchResultOptions();
         };

         BrowseOrdersService.getOrderStatuses().then(function(response) {
             $scope.orderStatuses = response.data;
         });
     }]);

var scopeModifyOrdersController;
angular.module('gryf.orders').controller("detailsform.OrdersController",
    ['$scope', 'ModifyOrdersService', 'GryfHelpers', 'GryfPopups', '$routeParams', function($scope, ModifyOrdersService, GryfHelpers, GryfPopups, $routeParams) {
        scopeModifyOrdersController = $scope;
        gryfSessionStorage.setUrlToSessionStorage();
        GryfPopups.showPopup();

        var ELEMENTS_URL = contextPath + "/templates/publicbenefits/orders/elements/";

        $scope.entityObject = ModifyOrdersService.getEntityObject();
        $scope.orderStatus = ModifyOrdersService.getOrderStatus();
        $scope.violations = ModifyOrdersService.getViolations();
        $scope.isGrantAppFixed = false;

        $scope.isContractLovShow = $routeParams.id ? false : true;
        $scope.openContractLov = function() {
            ModifyOrdersService.openContractLov().result.then(function(chosenContract) {
                ModifyOrdersService.fastSave(chosenContract.id);
            });
        };

        $scope.toggleGrantAppView = function() {
            $scope.isGrantAppFixed = !$scope.isGrantAppFixed;
        };

        $scope.setFileToDelete = function(item) {
            item.fileName = "";
            item.fileToDelete = true;
        };

        $scope.setFileNotToDelete = function(item) {
            item.fileToDelete = false;
        };

        $scope.executeAction = function(actionId) {
            $scope.violations = ModifyOrdersService.getNewViolations();
            var orderId = $scope.entityObject.id;
            ModifyOrdersService.executeAction(actionId, orderId);
        };

        $scope.loadOrder = function() {
            ModifyOrdersService.loadOrder();
        };

        $scope.loadOrder();
        $scope.templateUrl = function(item) {
            return ELEMENTS_URL + item["elementTypeComponentName"] + ".html";
        };

        $scope.getDownloadAttachmentLink = function(id) {
            if (!id) {
                return null;
            }
            return contextPath + "/publicBenefits/orders/downloadAttachment?id=" + id;
        };

        $scope.isInFormat = function(item, value) {
            return GryfHelpers.isInFormat(item, value);
        };

        $scope.isType = function(item, value) {
            return GryfHelpers.isType(item, value);
        };

        $scope.getTextAreaRows = function(item) {
            return getParameterValue(item, "ROWS");
        };

        $scope.getTextAreaCols = function(item) {
            return getParameterValue(item, "COLS");
        };

        $scope.getMaxLength = function(item) {
            return getParameterValue(item, "MAX_LENGTH");
        };
        
        $scope.getHeadingLevel = function(item) {
            return getParameterValue(item, "LEVEL");
        };        

        function getParameterValue(item, parameter) {
            var params = item.elementTypeParams;
            var parametersArray;
            if (params) {
                parametersArray = params.split(';');
            } else {
                return null;
            }
            for (var i = 0; i < parametersArray.length; i++) {
                var param = parametersArray[i];
                var paramArray = param.split("=");
                if (paramArray[0].trim() === parameter) {
                    return paramArray[1].trim();
                }
            }
            return null;
        }
    }]);


var scopePreviewOrdersController;
angular.module('gryf.orders').controller("previewform.OrdersController",
    ['$scope', 'PreviewOrdersService', 'ModifyOrdersService', 'GryfHelpers', 'GryfPopups',
     function($scope, PreviewOrdersService, ModifyOrdersService, GryfHelpers, GryfPopups) {
         scopePreviewOrdersController = $scope;
         var ELEMENTS_URL = contextPath + "/templates/publicbenefits/orders/elements/";
         $scope.entityObject = PreviewOrdersService.getEntityObject();
         $scope.orderStatus = PreviewOrdersService.getOrderStatus();
         gryfSessionStorage.setUrlToSessionStorage();
         GryfPopups.showPopup();

         $scope.loadOrder = function() {
             PreviewOrdersService.loadOrder();
         };

         $scope.loadOrder();

         $scope.templateUrl = function(item) {
             return ELEMENTS_URL + item["elementTypeComponentName"] + ".html";
         };

         $scope.isGrantAppFixed = false;

         $scope.toggleGrantAppView = function() {
             $scope.isGrantAppFixed = !$scope.isGrantAppFixed;
         };

         $scope.setFileToDelete = function(item) {
             item.fileName = "";
             item.fileToDelete = true;
         };

         $scope.setFileNotToDelete = function(item) {
             item.fileToDelete = false;
         };

         $scope.getDownloadAttachmentLink = function(id) {
             if (!id) {
                 return null;
             }
             return contextPath + "/publicBenefits/orders/downloadAttachment?id=" + id;
         };

         $scope.isInFormat = function(item, value) {
             return GryfHelpers.isInFormat(item, value);
         };
         
        $scope.isType = function(item, value) {
            return GryfHelpers.isType(item, value);
        };

        $scope.getTextAreaRows = function(item) {
            return getParameterValue(item, "ROWS");
        };

        $scope.getTextAreaCols = function(item) {
            return getParameterValue(item, "COLS");
        };         

         $scope.getDownloadAttachmentLink = function(id) {
             if (!id) {
                 return null;
             }
             return contextPath + "/publicBenefits/orders/downloadAttachment?id=" + id;
         };

        function getParameterValue(item, parameter) {
            var params = item.elementTypeParams;
            var parametersArray;
            if (params) {
                parametersArray = params.split(';');
            } else {
                return null;
            }
            for (var i = 0; i < parametersArray.length; i++) {
                var param = parametersArray[i];
                var paramArray = param.split("=");
                if (paramArray[0].trim() === parameter) {
                    return paramArray[1].trim();
                }
            }
            return null;
        }

     }]);