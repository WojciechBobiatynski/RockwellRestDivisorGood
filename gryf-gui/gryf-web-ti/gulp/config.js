(function() {
    var argv = require('yargs').argv;
    var configLocal = {};

    try {
        configLocal = require('./config-local');
    } catch(e) {
        // Ignore missing dependencies
    }
    
    var config = {
        app: 'src/main/frontend/',
        appDev: 'src/dev/frontend/',
        webappDev: 'src/main/webapp/',
        webappDevImg: 'src/main/webapp/public/img/',
        loaderDev: 'src/dev/loader/',
        test: 'src/test/javascript/spec/',
        withMap: false,

        work: 'target/work/',
        workDev: 'target/work-dev/',
        dist: 'target/gryf-ti-web/',

        backend: 'http://localhost:38080/ti',
        host: 'localhost',
        port: 33000,
        uiPort: 33001
    };

    for (var attrName in configLocal) {
        config[attrName] = configLocal[attrName];
    }

    if(argv.backend) config.backend = argv.backend;
    if(argv.host) config.host = argv.host;

    module.exports = config;
})();