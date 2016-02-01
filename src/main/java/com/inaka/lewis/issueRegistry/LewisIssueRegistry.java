package com.inaka.lewis.issueRegistry;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Issue;
import com.inaka.lewis.issues.IconInLibraryDetector;
import com.inaka.lewis.issues.LauncherActivityDetector;
import com.inaka.lewis.issues.RootPackageDetector;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("unused")
public class LewisIssueRegistry extends IssueRegistry {

    @Override
    public List<Issue> getIssues() {
        List<Issue> issues = new ArrayList<Issue>();
        issues.add(RootPackageDetector.ISSUE_CLASS_IN_ROOT_PACKAGE);
        issues.add(LauncherActivityDetector.ISSUE_MISSING_LAUNCHER);
        issues.add(LauncherActivityDetector.ISSUE_MORE_THAN_ONE_LAUNCHER);
        issues.add(LauncherActivityDetector.ISSUE_LAUNCHER_ACTIVITY_IN_LIBRARY);
        issues.add(IconInLibraryDetector.ISSUE_ICON_IN_LIBRARY);
        return issues;
    }

}
