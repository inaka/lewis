package com.inaka.lewis.issueRegistry;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Issue;
import com.inaka.lewis.issues.HardcodedTextDetectorModified;
import com.inaka.lewis.issues.IconInLibraryDetector;
import com.inaka.lewis.issues.JavaVariablesDetector;
import com.inaka.lewis.issues.LauncherActivityDetector;
import com.inaka.lewis.issues.LayoutIdFormat;
import com.inaka.lewis.issues.PermissionsInLibraryDetector;
import com.inaka.lewis.issues.RootPackageDetector;

import java.util.Arrays;
import java.util.List;


@SuppressWarnings("unused")
public class LewisIssueRegistry extends IssueRegistry {

    /**
     * Get issues
     *
     * @return the list of new issues to add.
     */
    @Override
    public List<Issue> getIssues() {
        return Arrays.asList(
                RootPackageDetector.ISSUE_CLASS_IN_ROOT_PACKAGE,
                LauncherActivityDetector.ISSUE_MISSING_LAUNCHER,
                LauncherActivityDetector.ISSUE_MORE_THAN_ONE_LAUNCHER,
                LauncherActivityDetector.ISSUE_LAUNCHER_ACTIVITY_IN_LIBRARY,
                IconInLibraryDetector.ISSUE_ICON_IN_LIBRARY,
                PermissionsInLibraryDetector.ISSUE_PERMISSION_USAGE_IN_LIBRARY,
                JavaVariablesDetector.ISSUE_INSTANCE_VARIABLE_NAME,
                JavaVariablesDetector.ISSUE_CLASS_CONSTANT_NAME,
                HardcodedTextDetectorModified.ISSUE,
                LayoutIdFormat.ISSUE);
    }

}
