package kcauldron

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.tasks.*

class InstallBundle extends DefaultTask {
    @InputFile
    def File serverJar

    @InputFiles
    def ConfigurableFileCollection bootstrapClasspath

    @Input
    def String bootstrapMain

    InstallBundle() {
        bootstrapClasspath = project.files()
    }

    def bootstrapClasspath(Object... args) {
        bootstrapClasspath.from args
    }

    @OutputDirectory
    def File getInstallLocation() {
        new File(project.buildDir, 'bundle')
    }

    @TaskAction
    def install() {
        installLocation.deleteDir()
        installLocation.mkdirs()
        new File(installLocation, "README.txt").withWriter {
            def String jarPath = 'bin/' << (project.group as String).replace('.', File.separator) << File.separator << project.name << File.separator << project.version << File.separator << project.name << '-' << project.version << '.jar'

            it << '''KCauldron installation guide

# Understanding this bundle
You're reading this guide because you're using deprecated installation method
If you want use easier & safer method please read about KBootstrap at https://prok.pw/KBootstrap

# Installation and usage
1. Unpack this zip into server directory
2. Use following line to start the server:
  java -jar '''
            it << jarPath
            it << '''
3. That's end, enjoy

# Why I should use KBootstrap?
1. Easiest server installation
2. Built-in libraries management
3. Update & run server in one line
4. Ability to not read this boring guide
5. What else?
If you are not yet convinced and want to use bundles instead KBootstrap... Meh, this is your choice.
'''
        }
        def cp = bootstrapClasspath
        for (int i = 0; i < 3; i++) {
            def result = project.javaexec { it ->
                workingDir installLocation
                classpath cp
                main bootstrapMain
                args '--serverDir', installLocation.canonicalPath,
                        '--installServer', serverJar.canonicalFile
            }
            if (result.exitValue == 0) return
        }
        throw new GradleException("Failed to install bundle into ${installLocation}")
    }

    private static final class NopOutputStream extends OutputStream {
        @Override
        void write(byte[] b, int off, int len) throws IOException {
        }

        @Override
        void write(byte[] b) throws IOException {
        }

        @Override
        void write(int b) throws IOException {
        }
    }
}
