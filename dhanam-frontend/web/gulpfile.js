var gulp = require('gulp'), clean = require('gulp-clean'), include = require('gulp-include'),
  concat = require('gulp-concat'), uglify = require('gulp-uglify'), rename = require('gulp-rename'),
  filesize = require('gulp-filesize'), changed = require('gulp-changed'),
  //, less = require('gulp-less'), neat = require('node-neat').includePaths
  //bourbon = require('node-bourbon').includePaths, 
  typescript = require("gulp-typescript"), sourcemaps = require("gulp-sourcemaps"),
  plumber = require('gulp-plumber'), haml = require('gulp-ruby-haml'),
  minifyCSS = require('gulp-minify-css'), sass = require('gulp-sass'),
  nunjucks = require('gulp-nunjucks'), 
  minifyCss = require('gulp-minify-css'),
  tsPath = ['src/ts/**/*.ts'], jsPath = ['src/js/site/*.js'], sassPath = 'src/sass/**/*.scss',
  requirePath = 'src/require/*.js', compilePath = 'build/', cssCompilePath = compilePath + 'css',
  jsCompilePath = compilePath + 'js', connect = require('gulp-connect'),
  delFiles = [compilePath, 'static/scripts/*.min.js', compilePath+'css'], staticPath='';

gulp.task('clean', function() {
  return gulp.src(delFiles, {
      read: false
    })
    .pipe(clean());
});

gulp.task('move', function(){
  // the base option sets the relative root for the set of files,
  // preserving the folder structure
  gulp.src(['src/css/**/*.css'])
  .pipe(gulp.dest('static/work-css'));
});

gulp.task('cssmin', function(){
  // the base option sets the relative root for the set of files,
  // preserving the folder structure
  gulp.src(['static/work-css/**/*.css'])
  .pipe(plumber())
  .pipe(sourcemaps.init())
  .pipe(concat("bundle.un.css"))
  .pipe(sourcemaps.write())
  .pipe(gulp.dest(staticPath+'static/css'))
  .pipe(rename('bundle.min.css'))
  .pipe(minifyCss())
  .pipe(gulp.dest(staticPath+'static/css'))
  .pipe(plumber.stop())
  .pipe(connect.reload())
  .on('error', function (err) {
    console.error('Error', err.message);
  });
});

gulp.task('sass', function() {
  return gulp.src(sassPath)
  .pipe(plumber())
  .pipe(sass({
      // includePaths: require('node-bourbon').with('other/path', 'another/path') 
      // - or - 
      includePaths: require('node-neat').includePaths
      //outputStyle: 'compressed'
    }))
  .pipe(sourcemaps.init())
  .pipe(concat('app.un.css'))
  .pipe(sourcemaps.write())
  .pipe(gulp.dest('static/css'))
  .pipe(rename('app.min.css'))
  .pipe(minifyCss())
  .pipe(gulp.dest(staticPath+'static/css'))
  .pipe(plumber.stop())
  .pipe(connect.reload())
  .on('error', function (err) {
    console.error('Error', err.message);
  });
});
/*
gulp.task('sass', function() {
    return sass(sassPath, { style: 'expanded', sourcemap: true})
        .pipe(sourcemaps.write('maps', {
        includeContent: false,
        sourceRoot: '/src/scss'
    }))
        .pipe(gulp.dest('static/css'))
        .on('error', function (err) {
          console.error('Error', err.message);
        });
});
gulp.task('js', function() {
  return gulp.src(jsCompilePath)
  .pipe(plumber())
  .pipe(concat('home.un.js'))
  .pipe(gulp.dest('static/scripts'))
  .pipe(filesize())
  .pipe(uglify())
  .pipe(rename('app.min.js'))
  .pipe(gulp.dest('static/scripts'))
  .pipe(filesize())
  .pipe(plumber.stop());
});
*/
gulp.task('require', function() {
  return gulp.src(requirePath)
  .pipe(plumber())
  .pipe(gulp.dest(compilePath+'/js'))
  .pipe(uglify())
  .pipe(gulp.dest(staticPath+'static/scripts'))
  .pipe(plumber.stop());
});

gulp.task('typescript', function() {
  var tsresult = gulp.src(tsPath)
  .pipe(plumber())
  .pipe(sourcemaps.init())
  .pipe(typescript({sortOutput: true, module:'amd'}));
  return tsresult.js
  .pipe(concat('app.un.js'))
  .pipe(sourcemaps.write())
  .pipe(gulp.dest('static/scripts'))
  .pipe(rename('app.min.js'))
  .pipe(uglify())
  .pipe(gulp.dest(staticPath+'static/scripts'))
  .pipe(plumber.stop())
  .pipe(connect.reload())
  .on('error', function (err) {
    console.error('Error', err.message);
  });
});

gulp.task('nunjucks', function () {
  return gulp.src('src/templates/*.nunj')
  .pipe(plumber())
  .pipe(nunjucks())
  .pipe(gulp.dest('static/work-views'))
  .pipe(concat('templates.un.js'))
  .pipe(sourcemaps.write())
  .pipe(gulp.dest('static/views'))
  .pipe(rename('templates.min.js'))
  .pipe(uglify())
  .pipe(gulp.dest(staticPath+'static/views'))
  .pipe(plumber.stop())
  .pipe(connect.reload())
  .on('error', function (err) {
    console.error('Error', err.message);
  });
});

gulp.task('watch', function() {
  gulp.watch('src/templates/*.nunj', ['nunjucks']);
  gulp.watch('src/htm/**/*.htm');
  gulp.watch(tsPath, ['typescript']);
  //gulp.watch(jsPath, ['js']);
  gulp.watch(cssCompilePath, ['css']);
  gulp.watch(sassPath, ['sass']);
  gulp.watch(requirePath, ['require']);
});
/*
gulp.task('webserver', function() {
  connect.server({
    livereload: true,
    port: 8080
  });
});
*/
//gulp.task('default', ['traceur', 'babel', 'watch']);
//gulp.task('default', ['watch', 'typescript', 'js', 'sass', 'require']);
//gulp.task('default', ['watch', 'typescript', 'sass', 'require','webserver', 'nunjucks']);
gulp.task('default', ['watch', 'move', 'typescript', 'sass', 'require','nunjucks', 'cssmin']);