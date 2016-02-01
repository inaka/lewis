package com.inaka.lewis;

import com.android.annotations.NonNull;
import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.client.api.LintClient;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Project;
import com.inaka.lewis.issues.IconInLibraryDetector;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.android.SdkConstants.FN_ANDROID_MANIFEST_XML;

public class IconInLibraryTest extends LintDetectorTest {

    final private Set<Issue> mEnabled = new HashSet<Issue>();

    @Override
    protected Detector getDetector() {
        return new IconInLibraryDetector();
    }

    @Override
    protected List<Issue> getIssues() {
        return Collections.singletonList(IconInLibraryDetector.ISSUE_ICON_IN_LIBRARY);
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

    public void testNoIcon() throws Exception {
        mEnabled.add(IconInLibraryDetector.ISSUE_ICON_IN_LIBRARY);

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
                "    </application>\n" +
                "</manifest>"));

        assertEquals(expected, result);
    }

    public void testIconButIsApp() throws Exception {
        mEnabled.add(IconInLibraryDetector.ISSUE_ICON_IN_LIBRARY);

        String expected = "No warnings.";

        String result = lintProject(xml(FN_ANDROID_MANIFEST_XML, "" +
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<manifest package=\"com.example.android.custom-lint-rules\"\n" +
                "          xmlns:android=\"http://schemas.android.com/apk/res/android\">\n" +
                "        <application\n" +
                "        android:name=\".App\"\n" +
                "        android:allowBackup=\"true\"\n" +
                "        android:icon=\"@mipmap/ic_launcher\"\n" +
                "        android:label=\"@string/app_name\"\n" +
                "        android:theme=\"@style/AppTheme\" >\n" +
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
