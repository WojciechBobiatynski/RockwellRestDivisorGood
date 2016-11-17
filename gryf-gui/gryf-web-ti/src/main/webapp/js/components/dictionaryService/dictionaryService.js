angular.module("gryf.ti").factory("DictionaryService", function($cacheFactory, $http) {
    var REST_URL = contextPath + "/rest/";

    var DICTIONARY = {
        TRAINING_CATEGORIES: "trainingreservation/trainingCategoriesDict",
        REIMBURSEMENT_STATUSES: "reimbursements/statuses",
        TRAINING_TO_REIMBURSE_STATUSES: "trainingToReimburse/statuses"
    };

    var dictionaryCache = $cacheFactory("DictionaryService");

    var loadDictionary = function(dictionaryName) {
        var dictionary = dictionaryCache.get(dictionaryName);
        if (!dictionary) {
            dictionary = [];
            $http.get(REST_URL + dictionaryName).then(function(resp) {
                angular.copy(resp.data, dictionary);
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

