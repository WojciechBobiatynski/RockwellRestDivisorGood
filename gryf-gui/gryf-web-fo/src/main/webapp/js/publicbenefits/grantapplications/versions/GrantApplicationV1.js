"use strict";

angular.module('gryf.grantApplications').factory('GrantApplicationV1Service', [function() {
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

    var cleanCopyEntityVersionObject = angular.copy(entityVersionObject);

    var getEntityVersionObject = function() {
        return entityVersionObject;
    };

    var getNewEntityVersionObject = function() {
        entityVersionObject = angular.copy(cleanCopyEntityVersionObject);
        return entityVersionObject;
    };


    return {
        getEntityVersionObject: getEntityVersionObject,
        getNewEntityVersionObject: getNewEntityVersionObject
    }
}]);


var scopeGrantApplicationV1;
angular.module('gryf.grantApplications').controller('GrantApplicationV1Controller',
    ['$scope', 'GrantApplicationV1Service', 'ModifyGrantApplicationService',
     function($scope, GrantApplicationV1Service, ModifyGrantApplicationService) {
         scopeGrantApplicationV1 = $scope;
         $scope.entityVersionObject = GrantApplicationV1Service.getEntityVersionObject();

         $scope.$on('propagateEnterpriseData', function(event, enterprise) {
             $scope.entityVersionObject.enterpriseName = enterprise.name;
             $scope.entityVersionObject.vatRegNum = enterprise.vatRegNum;
             $scope.entityVersionObject.addressInvoice = enterprise.addressInvoice;
             $scope.entityVersionObject.zipCodeInvoice = enterprise.zipCodeInvoice;
             $scope.entityVersionObject.addressCorr = enterprise.addressCorr;
             $scope.entityVersionObject.zipCodeCorr = enterprise.zipCodeCorr;
         });

         $scope.openInvoiceZipCodesLov = function() {
             ModifyGrantApplicationService.openZipCodesLov().result.then(function(chosenItem) {
                 $scope.entityVersionObject.zipCodeInvoice = chosenItem;
             });
         };

         $scope.openCorrZipCodesLov = function() {
             ModifyGrantApplicationService.openZipCodesLov().result.then(function(chosenItem) {
                 $scope.entityVersionObject.zipCodeCorr = chosenItem;
             });
         };

         $scope.addContact = function() {
             ModifyGrantApplicationService.addItemToList($scope.entityVersionObject.contacts);
         };

         $scope.addDeliveryContact = function() {
             ModifyGrantApplicationService.addItemToList($scope.entityVersionObject.deliveryContacts);

         };

         $scope.addPkd = function() {
             ModifyGrantApplicationService.addItemToList($scope.entityVersionObject.pkdList);
         };

         $scope.removePkd = function(pkd) {
             ModifyGrantApplicationService.removeItemFromList(pkd, $scope.entityVersionObject.pkdList);
         };

         $scope.removeContact = function(contact) {
             ModifyGrantApplicationService.removeItemFromList(contact, $scope.entityVersionObject.contacts);
         };

         $scope.removeDeliveryContact = function(contact) {
             ModifyGrantApplicationService.removeItemFromList(contact, $scope.entityVersionObject.deliveryContacts);
         };

     }]);
