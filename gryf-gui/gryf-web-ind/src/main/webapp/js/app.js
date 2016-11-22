"use strict";

angular.module("gryf.ind", ["gryf.privileges", "gryf.helpers", "gryf.modals", "gryf.config"]).run(function(IndividualUserService) {
    IndividualUserService.load();
});