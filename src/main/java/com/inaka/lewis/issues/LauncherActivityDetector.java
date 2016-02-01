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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.android.SdkConstants.ANDROID_MANIFEST_XML;
import static com.android.SdkConstants.ANDROID_URI;
import static com.android.SdkConstants.ATTR_NAME;
import static com.android.SdkConstants.TAG_ACTIVITY;
import static com.android.SdkConstants.TAG_APPLICATION;
import static com.android.SdkConstants.TAG_INTENT_FILTER;
import static com.android.xml.AndroidManifest.NODE_ACTION;
import static com.android.xml.AndroidManifest.NODE_CATEGORY;


public class LauncherActivityDetector extends ResourceXmlDetector implements Detector.XmlScanner {

    public static final Issue ISSUE_MISSING_LAUNCHER = Issue.create(
            "MissingLauncher",
            "Missing Launcher Activity",
            "This app should have an activity with a launcher intent.",
            Category.CORRECTNESS,
            5,
            Severity.WARNING,
            new Implementation(LauncherActivityDetector.class, Scope.MANIFEST_SCOPE));

    public static final Issue ISSUE_MORE_THAN_ONE_LAUNCHER = Issue.create(
            "MoreThanOneLauncher",
            "More than one Launcher Activity",
            "This app should have only one activity with a launcher intent.",
            Category.CORRECTNESS,
            5,
            Severity.WARNING,
            new Implementation(LauncherActivityDetector.class, Scope.MANIFEST_SCOPE));

    public static final Issue ISSUE_LAUNCHER_ACTIVITY_IN_LIBRARY = Issue.create(
            "LauncherActivityInLibrary",
            "Launcher Activity in library",
            "This library should not have activities with a launcher intent.",
            Category.CORRECTNESS,
            8,
            Severity.ERROR,
            new Implementation(LauncherActivityDetector.class, Scope.MANIFEST_SCOPE));

    /**
     * This will be true if the current file we're checking has at least one activity.
     */
    private boolean mHasActivity;

    /**
     * This will be true if there is a launcher activity
     */
    private boolean mHasLauncherActivity;

    /**
     * The lacation of the <application> tag
     */
    private Location mApplicationTagLocation;

    @Override
    public Collection<String> getApplicableElements() {
        List<String> elements = new ArrayList<String>();
        elements.add(TAG_ACTIVITY);
        elements.add(TAG_APPLICATION);
        return elements;
    }

    @Override
    public void beforeCheckProject(@NonNull Context context) {
        mHasActivity = false;
        mHasLauncherActivity = false;
        mApplicationTagLocation = null;
    }

    @Override
    public void afterCheckProject(@NonNull Context context) {

        // if it's not a library, it's an application
        if (context.getProject() == context.getMainProject() && !context.getMainProject().isLibrary() && mApplicationTagLocation != null) {

            if (!mHasActivity) {
                context.report(ISSUE_MISSING_LAUNCHER, mApplicationTagLocation,
                        "Expecting " + ANDROID_MANIFEST_XML + " to have an <" + TAG_ACTIVITY + "> tag.");
            } else if (!mHasLauncherActivity) {
                context.report(ISSUE_MISSING_LAUNCHER, mApplicationTagLocation,
                        "Expecting " + ANDROID_MANIFEST_XML + " to have an activity with a launcher intent.");
            }

        }
    }

    @Override
    public void visitElement(XmlContext context, Element element) {
        if (isMainActivity(context, element)) {
            mHasLauncherActivity = true;
        }
    }

    /**
     * Returns true if the XML node is an activity with a launcher intent.
     *
     * @param node The node to check.
     * @return true if the node is an activity with a launcher intent.
     */
    private boolean isMainActivity(XmlContext context, Node node) {

        if (TAG_APPLICATION.equals(node.getNodeName())) {
            mApplicationTagLocation = context.getLocation(node);
        }

        if (TAG_ACTIVITY.equals(node.getNodeName())) {

            mHasActivity = true;

            for (Element activityChild : LintUtils.getChildren(node)) {
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
                        if (mHasLauncherActivity) {
                            context.report(ISSUE_MORE_THAN_ONE_LAUNCHER, context.getLocation(node),
                                    "Expecting " + ANDROID_MANIFEST_XML + " to have only one activity with a launcher intent.");
                        }

                        // if it is a library
                        if (context.getProject() == context.getMainProject() && context.getMainProject().isLibrary()) {
                            context.report(ISSUE_LAUNCHER_ACTIVITY_IN_LIBRARY, context.getLocation(node),
                                    "Expecting " + ANDROID_MANIFEST_XML + " not to have an activity with a launcher intent.");
                        }

                        return true;
                    }
                }
            }
        }
        return false;
    }
}
