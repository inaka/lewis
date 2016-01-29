package com.inaka.lewis;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Issue;
import com.inaka.lewis.issues.LauncherActivityDetector;
import com.inaka.lewis.issues.RootPathDetector;

import java.util.Collections;
import java.util.List;


@SuppressWarnings("unused")
public class LewisIssueRegistry extends IssueRegistry {
    @Override
    public List<Issue> getIssues() {
        List<Issue> issues = Collections.emptyList();
        issues.add(RootPathDetector.ISSUE);
        issues.add(LauncherActivityDetector.ISSUE_MISSING_LAUNCHER);
        issues.add(LauncherActivityDetector.ISSUE_MORE_THAN_ONE_LAUNCHER);
//        return Collections.singletonList(RootPathDetector.ISSUE);
        return issues;
    }
}
