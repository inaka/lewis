package com.inaka.lewis;

import com.android.annotations.NonNull;
import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.client.api.LintClient;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Project;
import com.inaka.lewis.issues.LauncherActivityDetector;
import com.inaka.lewis.issues.PermissionsInLibraryDetector;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.android.SdkConstants.FN_ANDROID_MANIFEST_XML;
import static com.android.SdkConstants.FN_ANT_PROPERTIES;
import static com.android.SdkConstants.FN_BUILD_GRADLE;
import static com.android.SdkConstants.FN_GRADLE_WRAPPER_PROPERTIES;
import static com.android.SdkConstants.FN_LOCAL_PROPERTIES;
import static com.android.SdkConstants.FN_PROJECT_PROPERTIES;

public class PermissionsInLibraryTest extends LintDetectorTest {

    final private Set<Issue> mEnabled = new HashSet<Issue>();

    @Override
    protected Detector getDetector() {
        return new PermissionsInLibraryDetector();
    }

    @Override
    protected List<Issue> getIssues() {
        return Collections.singletonList(PermissionsInLibraryDetector.ISSUE_PERMISSION_USAGE_IN_LIBRARY);
    }

    @Override
    protected TestConfiguration getConfiguration(LintClient client, Project project) {
        return new TestConfiguration(client, project, null) {
            @Override
            public boolean isEnabled(@NonNull Issue issue) {
                return super.isEnabled(issue) && mEnabled.contains(issue);
            }
        };
    }

    public void testNoPermissions() throws Exception{
        mEnabled.add(PermissionsInLibraryDetector.ISSUE_PERMISSION_USAGE_IN_LIBRARY);

        String expected = "No warnings.";

        String result = lintProject(xml(FN_ANDROID_MANIFEST_XML, "" +
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<manifest package=\"com.example.android.custom-lint-rules\"\n" +
                "          xmlns:android=\"http://schemas.android.com/apk/res/android\">\n" +
                "    <application>\n" +
                "        <activity android:name=\"com.example.android.custom-lint-rules" +
                ".MainActivity\">\n" +
                "            <intent-filter>\n" +
                "                <action android:name=\"android.intent.action.MAIN\"/>\n" +
                "                <category android:name=\"android.intent.category.LAUNCHER\"/>\n" +
                "            </intent-filter>\n" +
                "        </activity>\n" +
                "    </application>\n" +
                "</manifest>"));

        assertEquals(expected, result);
    }

    public void testPermissionsButIsNotALibrary() throws Exception{
        mEnabled.add(PermissionsInLibraryDetector.ISSUE_PERMISSION_USAGE_IN_LIBRARY);

        String expected = "No warnings.";

        String result = lintProject(xml(FN_ANDROID_MANIFEST_XML, "" +
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<manifest package=\"com.example.android.custom-lint-rules\"\n" +
                "          xmlns:android=\"http://schemas.android.com/apk/res/android\">\n" +
                " <uses-permission android:name=\"android.permission.INTERNET\" />\n" +
                " <uses-permission android:name=\"android.permission.INTERNET\" />\n" +
                "    <application>\n" +
                "        <activity android:name=\"com.example.android.custom-lint-rules" +
                ".MainActivity\">\n" +
                "            <intent-filter>\n" +
                "                <action android:name=\"android.intent.action.MAIN\"/>\n" +
                "                <category android:name=\"android.intent.category.LAUNCHER\"/>\n" +
                "            </intent-filter>\n" +
                "        </activity>\n" +
                "    </application>\n" +
                "</manifest>"));

        assertEquals(expected, result);
    }

}
