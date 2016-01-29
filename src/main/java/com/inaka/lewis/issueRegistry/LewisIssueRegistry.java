package com.inaka.lewis.issueRegistry;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Issue;
import com.inaka.lewis.issues.LauncherActivityDetector;
import com.inaka.lewis.issues.RootPackageDetector;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("unused")
public class LewisIssueRegistry extends IssueRegistry {

    @Override
    public List<Issue> getIssues() {
        List<Issue> issues = new ArrayList<Issue>();
        issues.add(RootPackageDetector.ISSUE);
        issues.add(LauncherActivityDetector.ISSUE_MISSING_LAUNCHER);
        issues.add(LauncherActivityDetector.ISSUE_MORE_THAN_ONE_LAUNCHER);
        return issues;
    }

}
