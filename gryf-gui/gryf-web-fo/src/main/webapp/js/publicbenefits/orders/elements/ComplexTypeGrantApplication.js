"use strict";

angular.module("gryf.orders").factory("ComplexTypeGrantApplicationService", ['$http', function($http) {
    var APPLICATION_URL = contextPath + "/rest/publicBenefits/grantapplication/";

    var entityVersionObject = {
        regon: null,
        enterpriseEstablishedYear: null,
        enterpriseName: null,
        vatRegNum: null,
        entityType: null,
        addressInvoice: null,
        zipCodeInvoice: null,
        enterpriseSize: null,
        vouchersNumber: null,
        addressCorr: null,
        zipCodeCorr: null,
        accountRepayment: null,
        pkdList: [],
        contacts: [],
        deliveryContacts: []
    };

    var getEntityVersionObject = function() {
        return entityVersionObject;
    };

    var loadApplication = function(id) {
        var promise = $http.get(APPLICATION_URL + id);
        promise.then(function(response) {
            copyPropertiesIfExist(entityVersionObject, response.data);
        });
        return promise;
    };

    function copyPropertiesIfExist(destination, source) {
        for (var k in source) {
            if (source.hasOwnProperty(k) && destination.hasOwnProperty(k)) {
                destination[k] = source[k];
            }
        }
        return destination;
    }

    return {
        getEntityVersionObject: getEntityVersionObject,
        loadApplication: loadApplication
    }
}]);

var scopeComplexTypeGrantApplicationController;
angular.module("gryf.orders").controller("ComplexTypeGrantApplicationController",
    ['$scope', 'ComplexTypeGrantApplicationService', function($scope, ComplexTypeGrantApplicationService) {
        scopeComplexTypeGrantApplicationController = $scope;
        $scope.grantAppReady = false;

        $scope.entityVersionObject = ComplexTypeGrantApplicationService.getEntityVersionObject();
        $scope.grantApplicationId = $scope.entityObject.elements[$scope.$index].grantApplicationId;
        $scope.grantApplicationTemplateName = null;

        var initializeController = function(templateName) {
            angular.module("gryf.orders").controller(templateName + "Controller", function() {
            });
        };

        $scope.getDownloadAttachmentLink = function(attachment) {
            return contextPath + "/publicBenefits/grantApplications/downloadAttachment?id=" + attachment.id;
        };

        ComplexTypeGrantApplicationService.loadApplication($scope.grantApplicationId).then(function(response) {
            $scope.grantApplicationTemplateName = response.data.applicationVersion.templateName;
            $scope.grantApplicationAttachments = response.data.attachments;
            initializeController($scope.grantApplicationTemplateName);
            $scope.grantAppReady = true;
        });

        $scope.getApplicationLink = function() {
            return contextPath + "/templates/publicbenefits/grantapplications/versions/" + $scope.grantApplicationTemplateName + ".html";
        }
    }]);

