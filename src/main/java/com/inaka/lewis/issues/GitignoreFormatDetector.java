package com.inaka.lewis.issues;

import com.android.annotations.NonNull;
import com.android.tools.lint.checks.PrivateKeyDetector;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.Speed;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.inaka.lewis.utils.Constants;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class GitignoreFormatDetector extends Detector implements Detector.OtherFileScanner {

    private boolean mIsGradle = false;
    private boolean mIsLocalProperties = false;
    private boolean mIsIdea = false;
    private boolean mIsDSStore = false;
    private boolean mIsBuild = false;
    private boolean mIsCaptures = false;
    private List<String> mNotFound = new ArrayList<String>();

    public static final Issue ISSUE_GITIGNORE_FORMAT = Issue.create(
            "GitignoreFormat",
            ".gitignore file format",
            ".gitignore file should contain at least: .gradle, /local.properties, /.idea/, .DS_Store, " +
                    "/build, /captures.",
            Category.CORRECTNESS,
            8,
            Severity.ERROR,
            new Implementation(GitignoreFormatDetector.class, Scope.OTHER_SCOPE));

    @NonNull
    @Override
    public EnumSet<Scope> getApplicableFiles() {
        return Scope.OTHER_SCOPE;
    }

    @NonNull
    @Override
    public Speed getSpeed() {
        return Speed.NORMAL;
    }

    @Override
    public void run(@NonNull Context context) {

        if (!context.getProject().getReportIssues()) {
            // If this is a library project not being analyzed, ignore it
            return;
        }

        File file = context.file;

        if (isGitignoreFile(file)) {
            if (!hasCorrectFormat(file)) {
                String notFoundString = getNotFoundString();
                context.report(ISSUE_GITIGNORE_FORMAT, Location.create(file),
                        "Expecting .gitignore to contain " + notFoundString + ".");
            }
        }

    }

    private String getNotFoundString() {
        String notFound = mNotFound.get(0);

        for (int i = 1; i < mNotFound.size(); i++) {
            notFound = notFound.concat(", " + mNotFound.get(i));
        }

        return notFound;
    }

    private boolean hasCorrectFormat(File file) {
        try {
            String content = Files.toString(file, Charsets.UTF_8);

            mIsGradle = content.contains(Constants.GITIGNORE_GRADLE);
            mIsLocalProperties = content.contains(Constants.GITIGNORE_LOCAL_PROPERTIES);
            mIsIdea = content.contains(Constants.GITIGNORE_IDEA);
            mIsDSStore = content.contains(Constants.GITIGNORE_DS_STORE);
            mIsBuild = content.contains(Constants.GITIGNORE_BUILD);
            mIsCaptures = content.contains(Constants.GITIGNORE_CAPTURES);

            boolean result = mIsGradle && mIsLocalProperties && mIsIdea && mIsDSStore && mIsBuild && mIsCaptures;

            completeNotFoundList();


            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void completeNotFoundList() {
        mNotFound.clear();

        if (!mIsGradle) {
            mNotFound.add(Constants.GITIGNORE_GRADLE);
        }

        if (!mIsLocalProperties) {
            mNotFound.add(Constants.GITIGNORE_LOCAL_PROPERTIES);
        }

        if (!mIsIdea) {
            mNotFound.add(Constants.GITIGNORE_IDEA);
        }

        if (!mIsDSStore) {
            mNotFound.add(Constants.GITIGNORE_DS_STORE);
        }

        if (!mIsBuild) {
            mNotFound.add(Constants.GITIGNORE_BUILD);
        }

        if (!mIsCaptures) {
            mNotFound.add(Constants.GITIGNORE_CAPTURES);
        }

    }

    private boolean isGitignoreFile(File file) {
        return file.getPath().equals(".gitignore");
    }

}
