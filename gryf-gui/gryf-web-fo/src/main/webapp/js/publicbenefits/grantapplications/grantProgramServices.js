"use strict";


angular.module("gryf.grantApplications").factory("GrantProgramService",
    ["$http", 'GryfModals', function($http, GryfModals) {

        var FIND_GRANT_PROGRAMS_DICTIONARIES_URL = contextPath + "/rest/publicBenefits/grantapplication/grantProgramsDictionaries";
        var FIND_GRANT_PROGRAMS_VERSIONS_DICTIONARIES_URL = contextPath + "/rest/publicBenefits/grantapplication/grantApplicationVersionsDictionaries/";

        var grantProgram = {
            entity: {},
            list: [],
            versions: []
        };

        var find = function() {
            var promise = $http.get(FIND_GRANT_PROGRAMS_DICTIONARIES_URL);

            promise.then(function(response) {
                //success
                grantProgram.list = response.data;
            });
            return promise;
        };

        var getGrantPrograms = function() {
            if (grantProgram.list.length === 0) {
                return find();
            }
            return grantProgram.list;
        };

        var getVersions = function(enterpriseId) {
            var modalInstance = GryfModals.openModal(GryfModals.MODALS_URL.WORKING, {label: "Wczytuję dane"});

            var promise = $http.get(FIND_GRANT_PROGRAMS_VERSIONS_DICTIONARIES_URL + enterpriseId);
            promise.then(function(response) {
                grantProgram.versions = response.data;
            });
            promise.finally(function() {
                GryfModals.closeModal(modalInstance);
            });
            return promise;
        };


        var getGrantProgram = function() {
            return grantProgram
        };

        return {
            getGrantPrograms: getGrantPrograms,
            getVersions: getVersions,
            getGrantProgram: getGrantProgram
        }
    }]);
