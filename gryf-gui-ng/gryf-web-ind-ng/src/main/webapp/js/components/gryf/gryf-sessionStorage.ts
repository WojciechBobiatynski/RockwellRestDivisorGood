var gryfSessionStorage = (function() {
    var HISTORY_URL = "historyURL";
    var MESSAGES = "messages";

    var getListLocation = function() {
        return location.href.replace(location.hash, "")
    };

    var setUrlToSessionStorage = function(url) {
        if (!url) {
            url = window.location.href;
        }
        var historyURL = JSON.parse(sessionStorage.getItem('historyURL'));
        if (!historyURL) {
            historyURL = {};
            history.current = url;
            historyURL.prev = "";
            sessionStorage.setItem(HISTORY_URL, JSON.stringify(historyURL));
            return;
        }

        if (url !== historyURL.current) {
            historyURL.prev = historyURL.current;
            historyURL.current = url;
            sessionStorage.setItem(HISTORY_URL, JSON.stringify(historyURL));

        }
    };

    var getUrlFromSessionStorage = function() {
        var historyURL = JSON.parse(sessionStorage.getItem(HISTORY_URL));

        if (!historyURL) {
            return getListLocation();
        }
        if (historyURL.prev) {
            return historyURL.prev;
        }
        return getListLocation();
    };

    var setMessageTo = function(type, title, message) {
        sessionStorage.setItem(MESSAGES, JSON.stringify({type: type, title: title, message: message}));
    };

    var getMessage = function() {
        var msg = JSON.parse(sessionStorage.getItem(MESSAGES));
        sessionStorage.removeItem(MESSAGES);
        return msg;
    };

    return {
        setUrlToSessionStorage: setUrlToSessionStorage,
        getUrlFromSessionStorage: getUrlFromSessionStorage,
        setMessageTo: setMessageTo,
        getMessage: getMessage
    }
})();