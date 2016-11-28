angular.module("gryf.ti").factory("DictionaryService", function ($cacheFactory, $http, $q) {
    var REST_URL = contextPath + "/rest/";

    var DICTIONARY = {
        TRAINING_CATEGORIES: "training/categories",
        REIMBURSEMENT_STATUSES: "reimbursements/statuses",
        TRAINING_INSTANCE_STATUSES: "trainingInstance/statuses"
    };

    var dictionaryCache = $cacheFactory("DictionaryService");

    var loadDictionary = function (dictionaryName) {
        var deferred = $q.defer();

        var dictionary = dictionaryCache.get(dictionaryName);
        if (dictionary) {
            deferred.resolve(dictionary);
        } else {
            $http.get(REST_URL + dictionaryName).then(function (resp) {
                dictionaryCache.put(dictionaryName, resp.data);
                deferred.resolve(resp.data);
            });
        }

        return deferred.promise;
    };

    var getRecordById = function(dictionaryName, recordId) {
        var deferred = $q.defer();

        loadDictionary(dictionaryName).then(function(data) {
            angular.forEach(data, function(record) {
                if(record.id === recordId) {
                    deferred.resolve(record);
                }
            });
        });

        return deferred.promise;
    };

    return {
        DICTIONARY: DICTIONARY,
        loadDictionary: loadDictionary,
        getRecordById: getRecordById
    }
});

