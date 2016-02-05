package com.inaka.lewis;

import com.android.annotations.NonNull;
import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.client.api.LintClient;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Project;
import com.inaka.lewis.issues.LauncherActivityDetector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.android.SdkConstants.FN_ANDROID_MANIFEST_XML;


public class LauncherActivityTest extends LintDetectorTest {

    final private Set<Issue> mEnabled = new HashSet<Issue>();

    @Override
    protected Detector getDetector() {
        return new LauncherActivityDetector();
    }

    @Override
    protected List<Issue> getIssues() {
        List<Issue> issues = new ArrayList<Issue>();
        issues.add(LauncherActivityDetector.ISSUE_MISSING_LAUNCHER);
        issues.add(LauncherActivityDetector.ISSUE_MORE_THAN_ONE_LAUNCHER);
        issues.add(LauncherActivityDetector.ISSUE_LAUNCHER_ACTIVITY_IN_LIBRARY);
        return issues;
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

    /**
     * Test that a manifest with an activity with a launcher intent has no warnings.
     */
    public void testHasMainActivity() throws Exception {
        mEnabled.add(LauncherActivityDetector.ISSUE_MORE_THAN_ONE_LAUNCHER);
        mEnabled.add(LauncherActivityDetector.ISSUE_MISSING_LAUNCHER);
        mEnabled.add(LauncherActivityDetector.ISSUE_LAUNCHER_ACTIVITY_IN_LIBRARY);

        String expected = "No warnings.";

        String result = lintProject(xml(FN_ANDROID_MANIFEST_XML, "" +
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<manifest package=\"com.example.android.custom-lint-rules\"\n" +
                "          xmlns:android=\"http://schemas.android.com/apk/res/android\">\n" +
                "    <application>\n" +
                "        <activity android:name=\"com.example.android.custom-lint-rules" +
                ".OtherActivity\">\n" +
                "        </activity>\n" +
                "\n" +
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

    /**
     * Test that a manifest without an activity with a launcher intent reports an error.
     */
    public void testMissingMainActivity() throws Exception {
        mEnabled.add(LauncherActivityDetector.ISSUE_MORE_THAN_ONE_LAUNCHER);
        mEnabled.add(LauncherActivityDetector.ISSUE_MISSING_LAUNCHER);
        mEnabled.add(LauncherActivityDetector.ISSUE_LAUNCHER_ACTIVITY_IN_LIBRARY);

        String expected = "AndroidManifest.xml:4: Warning: Expecting AndroidManifest.xml to have an " +
                "activity with a launcher intent. [MissingLauncher]\n" +
                "    <application>\n" +
                "    ^\n" +
                "0 errors, 1 warnings\n";

        String result = lintProject(xml(FN_ANDROID_MANIFEST_XML, "" +
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<manifest package=\"com.example.android.custom-lint-rules\"\n" +
                "          xmlns:android=\"http://schemas.android.com/apk/res/android\">\n" +
                "    <application>\n" +
                "        <activity android:name=\"com.example.android.custom-lint-rules" +
                ".Activity1\">\n" +
                "            <intent-filter>\n" +
                "                <action android:name=\"android.intent.action.VIEW\" />\n" +
                "\n" +
                "                <category android:name=\"android.intent.category.HOME\" />\n" +
                "                <category android:name=\"android.intent.category.LAUNCHER\" />\n" +
                "                <category android:name=\"android.intent.category.DEFAULT\" />\n" +
                "                <category android:name=\"android.intent.category.BROWSABLE\" " +
                "/>\n" +
                "            </intent-filter>\n" +
                "        </activity>\n" +
                "\n" +
                "        <activity android:name=\"com.example.android.custom-lint-rules" +
                ".Activity2\">\n" +
                "        </activity>\n" +
                "\n" +
                "        <activity android:name=\"com.example.android.custom-lint-rules" +
                ".Activity3\">\n" +
                "            <intent-filter>\n" +
                "                <action android:name=\"android.intent.action.SEND\"/>\n" +
                "                <category android:name=\"android.intent.category.DEFAULT\"/>\n" +
                "                <data android:mimeType=\"text/plain\"/>\n" +
                "            </intent-filter>\n" +
                "        </activity>\n" +
                "    </application>\n" +
                "</manifest>"));

        assertEquals(expected, result);
    }

    /**
     * Test that a manifest without an application tag has no warnings.
     */
    public void testMissingApplication() throws Exception {
        mEnabled.add(LauncherActivityDetector.ISSUE_MORE_THAN_ONE_LAUNCHER);
        mEnabled.add(LauncherActivityDetector.ISSUE_MISSING_LAUNCHER);
        mEnabled.add(LauncherActivityDetector.ISSUE_LAUNCHER_ACTIVITY_IN_LIBRARY);

        String expected = "No warnings.";

        String result = lintProject(xml(FN_ANDROID_MANIFEST_XML, "" +
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<manifest package=\"com.example.android.custom-lint-rules\"\n" +
                "          >\n" +
                "</manifest>"));

        assertEquals(expected, result);
    }

    public void testMissingActivities() throws Exception {
        mEnabled.add(LauncherActivityDetector.ISSUE_MORE_THAN_ONE_LAUNCHER);
        mEnabled.add(LauncherActivityDetector.ISSUE_MISSING_LAUNCHER);
        mEnabled.add(LauncherActivityDetector.ISSUE_LAUNCHER_ACTIVITY_IN_LIBRARY);

        String expected = "AndroidManifest.xml:4: Warning: Expecting AndroidManifest.xml to have an " +
                "<activity> tag. [MissingLauncher]\n" +
                "    <application>\n" +
                "    ^\n" +
                "0 errors, 1 warnings\n";

        String result = lintProject(xml(FN_ANDROID_MANIFEST_XML, "" +
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<manifest package=\"com.example.android.custom-lint-rules\"\n" +
                "          >\n" +
                "    <application>\n" +
                "    </application>\n" +
                "</manifest>"));

        assertEquals(expected, result);
    }

    /**
     * Test that a manifest with two launcher activities reports an error.
     */
    public void testMultipleLauncherActivities() throws Exception {
        mEnabled.add(LauncherActivityDetector.ISSUE_MORE_THAN_ONE_LAUNCHER);
        mEnabled.add(LauncherActivityDetector.ISSUE_MISSING_LAUNCHER);
        mEnabled.add(LauncherActivityDetector.ISSUE_LAUNCHER_ACTIVITY_IN_LIBRARY);

        String expected = "AndroidManifest.xml:12: Warning: Expecting AndroidManifest.xml to have only one " +
                "activity with a launcher intent. [MoreThanOneLauncher]\n" +
                "        <activity android:name=\"com.example.android.custom-lint-rules.MainActivity\">\n" +
                "        ^\n" +
                "0 errors, 1 warnings\n";

        String result = lintProject(xml(FN_ANDROID_MANIFEST_XML, "" +
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<manifest package=\"com.example.android.custom-lint-rules\"\n" +
                "          xmlns:android=\"http://schemas.android.com/apk/res/android\">\n" +
                "    <application>\n" +
                "        <activity android:name=\"com.example.android.custom-lint-rules" +
                ".OtherActivity\">\n" +
                "            <intent-filter>\n" +
                "                <action android:name=\"android.intent.action.MAIN\"/>\n" +
                "                <category android:name=\"android.intent.category.LAUNCHER\"/>\n" +
                "            </intent-filter>\n" +
                "        </activity>\n" +
                "\n" +
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
