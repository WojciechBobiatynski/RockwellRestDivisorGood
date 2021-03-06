!function() {
    "use strict";
    angular.module("angularLoad", []).service("angularLoad", ["$document", "$q", "$timeout", function(a, b, c) {
        function d(a) {
            var d = {};
            return function(e) {
                if ("undefined" == typeof d[e]) {
                    var f = b.defer(), g = a(e);
                    g.onload = g.onreadystatechange = function(a) {
                        c(function() {
                            f.resolve(a)
                        })
                    }, g.onerror = function(a) {
                        c(function() {
                            f.reject(a)
                        })
                    }, d[e] = f.promise
                }
                return d[e]
            }
        }

        this.loadScript = d(function(b) {
            var c = a[0].createElement("script");
            return c.src = b, a[0].body.appendChild(c), c
        }), this.loadCSS = d(function(b) {
            var c = a[0].createElement("link");
            return c.rel = "stylesheet", c.type = "text/css", c.href = b, a[0].head.appendChild(c), c
        })
    }])
}();
//# sourceMappingURL=angular-load.min.js.map