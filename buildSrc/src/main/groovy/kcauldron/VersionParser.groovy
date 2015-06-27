package kcauldron

import java.util.regex.Matcher

class VersionParser {
    public static String parseForgeRevision(File forgeFile, File propsFile) {
        def forgeVersion = forgeFile.text
        def int majorVersion = v(forgeVersion =~ /.+int majorVersion\s+=\s+(\d+);/)
        def int minorVersion = v(forgeVersion =~ /.+int minorVersion\s+=\s+(\d+);/)
        def int revisionVersion = v(forgeVersion =~ /.+int revisionVersion\s+=\s+(\d+);/)
        def props = new Properties();
        propsFile.withInputStream { props.load(it) }
        def int buildVersion = props['fmlbuild.build.number'] as int
        return "${majorVersion}.${minorVersion}.${revisionVersion}.${buildVersion}"
    }

    static int v(Matcher matcher) {
        matcher.find()
        matcher.group(1) as int
    }
}
