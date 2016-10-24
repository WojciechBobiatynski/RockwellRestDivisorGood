/**
 * Created by adziobek on 24.10.2016.
 */
"use strict";

angular.module("gryf.agreements").factory("AgreementsService",
    ["$http", function($http) {
        var getAllAgreements = function () {
            var allAgreements = [];
            return allAgreements;
        }
        return{
            getAllAgreements:  getAllAgreements
        }
    }]);