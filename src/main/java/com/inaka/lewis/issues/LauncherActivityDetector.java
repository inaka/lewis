package com.inaka.lewis.issues;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LintUtils;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.XmlContext;
import com.inaka.lewis.utils.Constants;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Collection;
import java.util.Collections;

import static com.android.SdkConstants.ANDROID_MANIFEST_XML;
import static com.android.SdkConstants.ANDROID_URI;
import static com.android.SdkConstants.ATTR_NAME;
import static com.android.SdkConstants.TAG_ACTIVITY;
import static com.android.SdkConstants.TAG_INTENT_FILTER;
import static com.android.xml.AndroidManifest.NODE_ACTION;
import static com.android.xml.AndroidManifest.NODE_CATEGORY;


public class LauncherActivityDetector extends ResourceXmlDetector implements Detector.XmlScanner {

    public static final Issue ISSUE_MISSING_LAUNCHER = Issue.create(
            "MissingLauncherDetector",
            "Missing Launcher Activity",
            "This app should have an activity with a launcher intent.",
            Category.CORRECTNESS,
            3,
            Severity.WARNING,
            new Implementation(LauncherActivityDetector.class, Scope.MANIFEST_SCOPE));

    public static final Issue ISSUE_MORE_THAN_ONE_LAUNCHER = Issue.create(
            "MoreThanOneLauncherDetector",
            "More than one Launcher Activity",
            "This app should have only one activity with a launcher intent.",
            Category.CORRECTNESS,
            3,
            Severity.WARNING,
            new Implementation(LauncherActivityDetector.class, Scope.MANIFEST_SCOPE));

    /**
     * This will be true if the current file we're checking has at least one activity.
     */
    private boolean mHasActivity;

    /**
     * The manifest file location for the main project, null if there is no manifest.
     */
    private Location mManifestLocation;

    /**
     * This is the counter of launcher activities
     */
    private int mLauncherActivitiesCounter;

    @Override
    public Collection<String> getApplicableElements() {
        return Collections.singleton(TAG_ACTIVITY);
    }

    @Override
    public void beforeCheckProject(@NonNull Context context) {
        mHasActivity = false;
        mManifestLocation = null;
        mLauncherActivitiesCounter = 0;
    }

    @Override
    public void afterCheckProject(@NonNull Context context) {

        // Don't report issues on libraries
        if (context.getProject() == context.getMainProject() && !context.getMainProject().isLibrary() && mManifestLocation != null) {

            if (!mHasActivity) {
                context.report(ISSUE_MISSING_LAUNCHER, mManifestLocation,
                        "Expecting " + ANDROID_MANIFEST_XML + " to have an <" + TAG_ACTIVITY + "> tag.");
            } else if (mLauncherActivitiesCounter == 0) {
                context.report(ISSUE_MISSING_LAUNCHER, mManifestLocation,
                        "Expecting " + ANDROID_MANIFEST_XML + " to have an activity with a launcher intent.");
            } else if (mLauncherActivitiesCounter > 1) {
                context.report(ISSUE_MORE_THAN_ONE_LAUNCHER, mManifestLocation,
                        "Expecting " + ANDROID_MANIFEST_XML + " to have only one activity with a launcher intent.");
            }
        }
    }

    @Override
    public void afterCheckFile(@NonNull Context context) {
        // Store a reference to the manifest file in case we need to report it's location.
        if (context.getProject() == context.getMainProject()) {
            mManifestLocation = Location.create(context.file);
        }
    }

    @Override
    public void visitElement(XmlContext context, Element activityElement) {
        // Checks every activity and reports an error if there is no activity with a launcher intent.
        mHasActivity = true;
        if (isMainActivity(activityElement)) {
            mLauncherActivitiesCounter++;
        }
    }

    /**
     * Returns true if the XML node is an activity with a launcher intent.
     *
     * @param activityNode The node to check.
     * @return true if the node is an activity with a launcher intent.
     */
    private boolean isMainActivity(Node activityNode) {
        if (TAG_ACTIVITY.equals(activityNode.getNodeName())) {

            for (Element activityChild : LintUtils.getChildren(activityNode)) {
                if (TAG_INTENT_FILTER.equals(activityChild.getNodeName())) {

                    boolean hasLauncherCategory = false;
                    boolean hasMainAction = false;

                    for (Element intentFilterChild : LintUtils.getChildren(activityChild)) {
                        // Check for category tag)
                        if (NODE_CATEGORY.equals(intentFilterChild.getNodeName())
                                && Constants.CATEGORY_NAME_LAUNCHER.equals(
                                intentFilterChild.getAttributeNS(ANDROID_URI, ATTR_NAME))) {
                            hasLauncherCategory = true;
                        }
                        // Check for action tag
                        if (NODE_ACTION.equals(intentFilterChild.getNodeName())
                                && Constants.ACTION_NAME_MAIN.equals(
                                intentFilterChild.getAttributeNS(ANDROID_URI, ATTR_NAME))) {
                            hasMainAction = true;
                        }
                    }

                    if (hasLauncherCategory && hasMainAction) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
