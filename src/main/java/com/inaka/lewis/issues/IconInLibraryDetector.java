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
import static com.android.SdkConstants.ANDROID_URI;
import static com.android.SdkConstants.ATTR_ICON;
import static com.android.SdkConstants.TAG_APPLICATION;

public class IconInLibraryDetector extends ResourceXmlDetector implements Detector.XmlScanner {

    public static final Issue ISSUE_ICON_IN_LIBRARY = Issue.create(
            "IconInLibrary",
            "Icon in library",
            "This library should not have an icon.",
            Category.CORRECTNESS,
            8,
            Severity.ERROR,
            new Implementation(IconInLibraryDetector.class, Scope.MANIFEST_SCOPE));

    private List<Location> mIconAttributesLocations;

    @Override
    public Collection<String> getApplicableElements() {
        return Collections.singletonList(TAG_APPLICATION);
    }

    @Override
    public void beforeCheckProject(@NonNull Context context) {
        mIconAttributesLocations = new ArrayList<Location>();
    }

    @Override
    public void visitElement(XmlContext context, Element element) {

        if (!element.getAttributeNS(ANDROID_URI, ATTR_ICON).equals("")) {
            mIconAttributesLocations.add(context.getLocation(element.getAttributeNodeNS(ANDROID_URI, ATTR_ICON)));
        }

    }

    @Override
    public void afterCheckProject(@NonNull Context context) {

        // if it's a library
        if (context.getProject() == context.getMainProject() && context.getMainProject().isLibrary()) {
            for (int i = 0; i < mIconAttributesLocations.size(); i++) {
                context.report(ISSUE_ICON_IN_LIBRARY, mIconAttributesLocations.get(i),
                        "Expecting " + ANDROID_MANIFEST_XML + " not to have an icon inside <" + TAG_APPLICATION + "> tag");
            }
        }
    }
}
