package com.inaka.lewis.issues;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.XmlContext;

import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.android.SdkConstants.ANDROID_MANIFEST_XML;
import static com.android.SdkConstants.TAG_USES_PERMISSION;

public class PermissionsInLibraryDetector extends ResourceXmlDetector implements Detector.XmlScanner {

    public static final Issue ISSUE_PERMISSION_USAGE_IN_LIBRARY = Issue.create(
            "PermissionUsageInLibraryDetector",
            "Permission usage in library",
            "This library should not have a permission invocation.",
            Category.CORRECTNESS,
            8,
            Severity.ERROR,
            new Implementation(PermissionsInLibraryDetector.class, Scope.MANIFEST_SCOPE));

    private List<Location> mPermissionTagsLocations;

    @Override
    public Collection<String> getApplicableElements() {
        return Collections.singletonList(TAG_USES_PERMISSION);
    }

    @Override
    public void beforeCheckProject(@NonNull Context context) {
        mPermissionTagsLocations = new ArrayList<Location>();
    }

    @Override
    public void visitElement(XmlContext context, Element element) {
        mPermissionTagsLocations.add(context.getLocation(element));
    }

    @Override
    public void afterCheckProject(@NonNull Context context) {

        // if it's not a library, it's an application
        if (context.getProject() == context.getMainProject() && context.getMainProject().isLibrary()) {
            for (Location location : mPermissionTagsLocations) {
                context.report(ISSUE_PERMISSION_USAGE_IN_LIBRARY, location,
                        "Expecting " + ANDROID_MANIFEST_XML + " not to have a <" + TAG_USES_PERMISSION + "> tag invocation");
            }
        }
    }
}
