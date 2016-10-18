'use strict';

var gulp = require('gulp'),
    del = require('del'),
    filter = require('gulp-filter'),
    debug = require('gulp-debug'),
    inject = require('gulp-inject'),
    newer = require('gulp-newer'),
    concat = require('gulp-concat'),
    mainBowerFiles = require('main-bower-files'),
    gulpif = require('gulp-if'),
    angularFilesort = require('gulp-angular-filesort'),
    angularTemplatecache = require('gulp-angular-templatecache'),
    runSequence = require('run-sequence'),
    browserSync = require('browser-sync'),
    minifyCss = require('gulp-minify-css'),
    useref = require('gulp-useref'),
    sourcemaps = require('gulp-sourcemaps'),
    uglify = require('gulp-uglify'),
    ngAnnotate = require('gulp-ng-annotate'),
    wiredep = require('wiredep').stream,
    fs = require('fs'),
    config = require('./gulp/config.js'),
    proxyMiddleware = require('http-proxy-middleware');

gulp.task('preprocess-dev', ['copy-js-work'], function(){
    var injectStyles = gulp.src([
        config.appDev + '/**/*.css'
    ], { read: false });

    var injectScripts = gulp.src([
        config.app + '/**/*.js',
        config.appDev + '/**/*.js'
    ]).pipe(angularFilesort());

    var injectOptions = {
        relative: true,
        ignorePath: ['../../main/frontend'],
        addPrefix: 'SERVER_ADDR'
    };

    var wiredepOpts = {
        ignorePath: '../../..',
        fileTypes: {
            html: {
                replace: {
                    js: '<script src="SERVER_ADDR{{filePath}}"></script>',
                }
            }
        }
    };

    return gulp.src([config.appDev+'**/*.html'])
        .pipe(inject(injectScripts, injectOptions))
        .pipe(inject(injectStyles, injectOptions))
        .pipe(wiredep(wiredepOpts))
        .pipe(gulp.dest(config.workDev))
        .pipe(browserSync.reload({ stream: true }));
});

gulp.task('preprocess', ['copy-js-work'], function(){
    var injectScripts = gulp.src([
        config.app + '/**/*.js'
    ]).pipe(angularFilesort());

    var injectOptions = {
        relative: true,
        ignorePath: '../../main/frontend'
    };

    return gulp.src([config.appDev+'**/*.html'])
        .pipe(inject(injectScripts, injectOptions))
        .pipe(wiredep())
        .pipe(gulp.dest(config.work));
});

gulp.task('styles', function() {
    return gulp.src([config.app+'**/*.css', config.appDev+'**/*.css'])
        .pipe(browserSync.reload({ stream: true }));
});

gulp.task('templates', function() {
    return gulp.src([config.app+'public/tpl/**/*.html'])
        .pipe(angularTemplatecache({
            module: 'mainApp',
            root: '/public/tpl'
        }))
        .pipe(gulp.dest(config.work));
});

gulp.task('vendor', function(cb) {
    function safeMTime(path) {
        try {
            return fs.statSync(path).mtime;
        } catch (e) {
            return 0;
        }
    }

    var bowerMtime=safeMTime('bower.json'),
        gulpMtime=safeMTime('gulpfile.js'),
        vendorMtime=safeMTime('bower_components/vendor.js'),
        baseMtime = Math.max(bowerMtime, gulpMtime);

    if(baseMtime<=vendorMtime) {
        console.log('Using existing vendor.js');
        return cb();
    }
    console.log('Generating new vendor.js');
    var jsFiles = mainBowerFiles();
    jsFiles.push('bower_components/kendo-ui-core/js/cultures/kendo.culture.pl-PL.min.js');
    gulp.src(jsFiles)
        .pipe(sourcemaps.init())
        .pipe(concat('vendor.js'))
        .pipe(uglify())
        .pipe(sourcemaps.write('.'))
        .pipe(gulp.dest('bower_components'))
        .on('end',cb);
});

gulp.task('copy-vendor', ['vendor'], function() {
    return gulp.src('bower_components/vendor.*')
        .pipe(gulp.dest(config.dist+'public/js'));
});

gulp.task('build', ['copy-vendor', 'templates', 'preprocess', 'copy-static'], function() {
    var assets = useref.assets({
        noconcat: true,
        searchPath: [config.work, '.']
    });

    var templatesInjectFile = gulp.src(config.work + 'templates.js', { read: true });
    var templatesInjectOptions = {
        starttag: '<!-- inject:templates -->',
        relative: true
    };

    return gulp.src([config.work+'**/*.html'])
        .pipe(inject(templatesInjectFile, templatesInjectOptions))
        .pipe(assets)
        .pipe(sourcemaps.init())
        .pipe(gulpif('*.js', ngAnnotate()))
        .pipe(gulpif('*.js', uglify()))
        .pipe(gulpif('*.js', concat('public/js/application.js')))
        .pipe(sourcemaps.write('.'))
        .pipe(gulp.dest(config.dist));
});

gulp.task('serve-dev', ['preprocess-dev'], function () {
    console.log('Using address: http://'+config.host+':'+config.port);
    gulp.watch([config.app + '**/*.html', config.app + '**/*.js'], ['preprocess-dev']);
    gulp.watch([config.appDev + '**/*.html', config.appDev + '**/*.js'], ['preprocess-dev']);

    gulp.watch([config.app + '**/*.css', config.appDev + '**/*.css'], function() {
        gulp.start('styles');
    });

    browserSync.init({
        startPath: '/',
        port: config.port,
        server: {
            baseDir: [config.workDev, config.work, config.appDev, config.app],
            middleware: [
                proxyMiddleware('/rest', {target: config.backend}),
                proxyMiddleware('/file', {target: config.backend}),
                proxyMiddleware('/report', {target: config.backend}),
                proxyMiddleware('/parts/content', {target: config.backend})
            ],
            routes: {
                '/bower_components': 'bower_components'
            }
        },
        ui: { port: config.uiPort },
        browser: [],
        rewriteRules: [{
            match: /SERVER_ADDR/g,
            fn: function(x) {
                return 'http://'+config.host+':'+config.port
            }
        }]
    })
});

gulp.task('watch-app', ['build'], function(){
    gulp.watch([config.app + '**/*.html', config.app + '**/*.js'], ['build']);
    gulp.watch([config.app + '**/*.css', config.appDev + '**/*.css'], function() {
        gulp.start('styles');
    });
});

gulp.task('build-eclipse', ['build'], function(){
    gulp.src(config.dist+'**/*.js')
        .pipe(debug('eclipse copy'))
        .pipe(gulp.dest(config.webappDev));
    gulp.src(config.dist+'**/*.css')
        .pipe(debug('eclipse copy'))
        .pipe(gulp.dest(config.webappDev));
    gulp.src(config.dist+'public/img/*.*')
        .pipe(debug('eclipse copy'))
        .pipe(gulp.dest(config.webappDevImg));
});

gulp.task('clean', function(cb) {
    del([config.dist+'public', config.work, config.workDev], cb);
});

gulp.task('copy-static', function () {
    return gulp.src([config.app+'**/*'])
        .pipe(filter(['**/*',
            '!**/js',  '!**/js/**',
            '!**/tpl', '!**/tpl/**' ]))
        .pipe(gulp.dest(config.dist));
});

gulp.task('copy-js-work', function () {
    return gulp.src([config.app+'**/*.js'])
        .pipe(gulp.dest(config.work));
});

gulp.task('clean-build', function() {
    return runSequence(
        'clean',
        'build');
});

gulp.task('default', ['clean-build'], function() {
});
