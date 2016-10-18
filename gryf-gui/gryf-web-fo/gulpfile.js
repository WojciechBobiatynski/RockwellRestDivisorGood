'use strict';

var gulp = require('gulp'),
    del = require('del'),
    filter = require('gulp-filter'),
    debug = require('gulp-debug'),
    newer = require('gulp-newer'),
    concat = require('gulp-concat'),
    mainBowerFiles = require('main-bower-files'),
    embedTemplates = require('gulp-angular-embed-templates'),
    runSequence = require('run-sequence'),
    mergeStream = require('merge-stream'),
    browserSync = require('browser-sync'),
    sourcemaps = require('gulp-sourcemaps'),
    uglify = require('gulp-uglify'),
    ngAnnotate = require('gulp-ng-annotate'),
    fs = require('fs'),
    config = require('./gulp/config.js'),
    webParts = require('./gulp/webParts.json'),
    proxyMiddleware = require('http-proxy-middleware');

gulp.task('styles', function() {
    return gulp.src([config.app+'**/*.css', config.appDev+'**/*.css'])
        .pipe(browserSync.reload({ stream: true }));
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
        .pipe(gulp.dest(config.dist+'js'));
});

gulp.task('build:js', function(cb) {
    var stream = mergeStream();
    for(var targetName in webParts) {
        if(!webParts.hasOwnProperty(targetName)) continue;
        
        var files = webParts[targetName];

        var part = gulp.src(files, {cwd: config.app, base: config.app })
            .pipe(sourcemaps.init())
            .pipe(ngAnnotate())
            .pipe(embedTemplates({ basePath: config.app }))
            .pipe(uglify())
            .pipe(concat(targetName))
            .pipe(sourcemaps.write('.'))
            .pipe(gulp.dest(config.dist));
        
        stream.add(part);
    }
    return stream;
});

gulp.task('build', ['copy-vendor', 'build:js', 'copy-static']);

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
            '!**/templates', '!**/templates/**' ]))
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
