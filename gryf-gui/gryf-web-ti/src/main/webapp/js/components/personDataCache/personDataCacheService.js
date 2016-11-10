angular.module("gryf.ti").factory("PersonDataCacheService", function() {
    var personData = {pesel: null, verificationCode: null};

    return {
        getPesonData: function() {
            return personData;
        },
        setPesonData: function(data) {
            personData = data;
        }
    }
});