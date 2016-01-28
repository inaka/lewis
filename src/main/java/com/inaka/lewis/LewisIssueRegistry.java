package com.inaka.lewis;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Issue;

import java.util.Collections;
import java.util.List;


@SuppressWarnings("unused")
public class LewisIssueRegistry extends IssueRegistry {
    @Override
    public List<Issue> getIssues() {
        return Collections.singletonList(RootPathDetector.ISSUE);
    }
}
