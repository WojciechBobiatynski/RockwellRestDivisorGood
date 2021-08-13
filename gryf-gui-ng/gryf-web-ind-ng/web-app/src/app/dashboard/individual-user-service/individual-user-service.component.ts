import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'gryf-individual-user-service',
  templateUrl: './individual-user-service.component.html',
  styleUrls: ['./individual-user-service.component.scss']
})

/*
{
  controller: class IndividualUserServiceController {
    public FIND_IND_URL = contextPath + "/rest/ind/";
    public SEND_REIMBURSMENT_PIN_URL = contextPath + "/rest/ind/reimbursmentPin/resend";
    private indObject = new IndObject();

    IndObject() {
      this.entity = {
        firstName: null,
        lastName: null,
        pesel: null,
        products: [],
        trainings: [],
        agreements: []
      }
    }

    private indObject: IndObject;
    getModel = {
      return this.indObject;
    }

  }
};


angular.module("gryf.ind").factory("IndividualUserService", ["$http", "GryfModals", "GryfPopups",
  function($http, GryfModals, GryfPopups) {

    var FIND_IND_URL = contextPath + "/rest/ind/";
    var SEND_REIMBURSMENT_PIN_URL = contextPath + "/rest/ind/reimbursmentPin/resend";
    var indObject = new IndObject();

    function IndObject() {
      this.entity = {
        firstName: null,
        lastName: null,
        pesel: null,
        products: [],
        trainings: [],
        agreements: []
      }
    }

    var getModel = function() {
      return indObject;
    };

    var load = function() {
      var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Wczytuję dane"});
      var promise = $http.get(FIND_IND_URL);
      promise.then(function(response) {
        indObject.entity = response.data;
      });
      promise.finally(function() {
        GryfModals.closeModal(modalInstance);
      });
      return promise;
    };

    var sendPin = function (trainingInstanceId) {
      var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Wczytuję dane"});
      var promise = $http.post(SEND_REIMBURSMENT_PIN_URL + '/'+ trainingInstanceId);

      promise.then(function () {
        GryfPopups.setPopup("success", "Sukces", "PIN został pomyślnie wysłany");
        GryfPopups.showPopup();
      });

      promise.error(function () {
        GryfPopups.setPopup("error", "Błąd", "Nie udało się wysłać PINU");
        GryfPopups.showPopup();
      });

      promise.finally(function() {
        GryfModals.closeModal(modalInstance);
      });

      return promise;
    };

    return {
      load: load,
      getModel: getModel,
      sendPin: sendPin
    }
  }]);
*/

export class IndividualUserServiceComponent implements OnInit {
  constructor() { }
  ngOnInit(): void {
  }
}
