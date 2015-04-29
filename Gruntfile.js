'use strict';

module.exports = function (grunt) {
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        banner: '/*! <%= pkg.title || pkg.name %> - v<%= pkg.version %> - ' +
        '<%= grunt.template.today("yyyy-mm-dd") %>\n' +
        '<%= pkg.homepage ? "* " + pkg.homepage + "\\n" : "" %>' +
        '* Copyright (c) <%= grunt.template.today("yyyy") %> <%= pkg.author.name %>;' +
        ' Licensed <%= _.pluck(pkg.licenses, "type").join(", ") %> */\n',
        watch: {
            js: {
                files: '<%= jshint.all %>',
                tasks: ['concat', 'rsync:devjs']
            },
            sass: {
                files: ['src/main/web/sass/*.scss'],
                tasks: ['sass', 'rsync:devsass']
            }
        },
        concat: {
            options: {
                banner: '<%= banner %>',
                stripBanners: true
            },
            dist: {
                src: [
                    'bower_components/angular/angular.js',
                    'bower_components/angular-route/angular-route.js',
                    'bower_components/ui-bootstrap/dist/ui-bootstrap-tpls-0.12.1.js',
                    'bower_components/angular-leaflet-directive/dist/angular-leaflet-directive.js',
                    'bower_components/leaflet/dist/leaflet.js',
                    'src/main/web/js/app.js',
                    'src/main/web/js/controllers/*',
                    'src/main/web/js/directives/*',
                    'src/main/web/js/services/*'
                ],
                dest: 'src/main/resources/public/js/<%= pkg.name %>.js'
            }
        },
        jshint: {
            options: {
                jshintrc: '.jshintrc',
                "force": true
            },
            all: [
                'Gruntfile.js',
                'src/main/web/js/controllers/*',
                'src/main/web/js/services/*',
                'src/main/web/js/*.js'
            ]
        },
        uglify: {
            options: {
                banner: '<%= banner %>',
                sourceMap: 'src/main/resources/public/js/<%= pkg.name %>.js.map',
                sourceMappingURL: 'src/main/resources/public/js/<%= pkg.name %>.js.map',
                sourceMapPrefix: 2
            },
            dist: {
                src: '<%= concat.dist.dest %>',
                dest: 'src/main/resources/public/js/<%= pkg.name %>.min.js'
            }
        },
        sass: {
            dist: {
                options: {
                    sourcemap: true,
                    style: 'compressed'
                },
                files: {
                    'src/main/resources/public/style/app.min.css': 'src/main/web/sass/style.scss',
                    'src/main/resources/public/style/leaflet.min.css': 'bower_components/leaflet/dist/leaflet.css'
                }
            }
        },
        rsync: {
            options: {
                args: ["--verbose"],
                recursive: true,
                syncDestIgnoreExcl: false
            },
            devjs: {
                options: {
                    src: "src/main/resources/public/js/",
                    dest: "target/classes/public/js/"
                }
            },
            devsass: {
                options: {
                    src: "src/main/resources/public/style/",
                    dest: "target/classes/public/style"
                }
            }
        }

    });

    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-sass');
    grunt.loadNpmTasks('grunt-rsync');

    grunt.registerTask('combine', ['concat:dist', 'uglify:dist', 'sass']);

};