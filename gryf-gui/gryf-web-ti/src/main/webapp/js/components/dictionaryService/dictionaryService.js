angular.module("gryf.ti").factory("DictionaryService", function($cacheFactory, $resource) {
    var REST_URL = contextPath + "/rest/trainingreservation/";

    var DICTIONARIES_NAMES = {
        TRAINING_CATEGORIES: "trainingCategoriesDict"
    };

    var dictionaryCache = $cacheFactory("DictionaryService");
    var dictionaryResource = $resource(REST_URL + ":name", {dictionaryName: "@name"});

    var loadDictionary = function(dictionaryName) {
        var dictionary = dictionaryCache.get(dictionaryName);
        if (!dictionary) {
            dictionary = dictionaryResource.query({name: dictionaryName});
            dictionaryCache.put(dictionaryName, dictionary);
        }
        return dictionary;
    }

    return {
        DICTIONARIES_NAMES: DICTIONARIES_NAMES,
        loadDictionary: loadDictionary
    }
});