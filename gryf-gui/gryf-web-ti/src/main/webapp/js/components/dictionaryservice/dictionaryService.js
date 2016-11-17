angular.module("gryf.ti").factory("DictionaryService", function ($cacheFactory, $http) {
    var REST_URL = contextPath + "/rest/";

    var DICTIONARY = {
        TRAINING_CATEGORIES: "trainingreservation/trainingCategoriesDict",
        REIMBURSEMENT_STATUSES: "reimbursements/statuses",
        TRAINING_INSTANCE_STATUSES: "trainingInstance/statuses"
    };

    var dictionaryCache = $cacheFactory("DictionaryService");

    var loadDictionary = function (dictionaryName, callback) {
        var dictionary = dictionaryCache.get(dictionaryName);
        if (dictionary && typeof callback === 'function') {
            callback();
        }
        if (!dictionary) {
            dictionary = [];
            $http.get(REST_URL + dictionaryName).then(function (resp) {
                angular.copy(resp.data, dictionary);
                if (typeof callback === 'function') {
                    callback();
                }
            });
            dictionaryCache.put(dictionaryName, dictionary);
        }
        return dictionary;
    };

    return {
        DICTIONARY: DICTIONARY,
        loadDictionary: loadDictionary
    }
});

