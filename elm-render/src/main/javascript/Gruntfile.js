module.exports = function(grunt) {
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-contrib-compress');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-elm');
    grunt.loadNpmTasks('grunt-exec');

    grunt.initConfig({
        'pkg': grunt.file.readJSON('package.json'),

        'exec': {
            'elm-github-install': {
                command: 'elm-github-install'
            }
        },

        'elm': {
            compile: {
                files: {
                    'app/elm-code.js': ['src/elm/**/*.elm']
                }
            }
        },

        'copy': {
            'dist': {
                files: [],
            }
        },

        'concat': {
            options: {
                separator: ';\n'
            },
            'sources': {
                'src': [
                    'src/js/**/*.js',
                    'app/elm-code.js'
                ],
                'dest': 'app/<%= pkg.name %>.js'
            }
        },

        'compress': {
            dist: {
                options: {
                    archive: 'dist/<%= pkg.name %>-<%= pkg.version %>.zip'
                },
                files: [{
                    src: ['app/**', 'server.js'],
                    dest: '/'
                }]
            }
        },

        'watch': {
            'dev': {
                files: ['Gruntfile.js', 'elm-package.json', 'src/**'],
                tasks: ['build'],
                options: {
                    atBegin: true
                }
            },
            'min': {
                files: ['Gruntfile.js', 'src/**'],
                tasks: ['package'],
                options: {
                    atBegin: true
                }
            }
        },

        'clean': {
            temp: {
                src: ['tmp', 'app', 'dist', "elm-stuff"]
            }
        },
    });

    grunt.registerTask('dev', ['watch:dev']);
    grunt.registerTask('minified', ['watch:min']);
    grunt.registerTask('build', ['copy', 'exec:elm-github-install', 'elm', 'concat']);
    grunt.registerTask('package', ['build', 'compress']);
};