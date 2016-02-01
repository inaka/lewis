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
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.android.SdkConstants.ANDROID_MANIFEST_XML;
import static com.android.SdkConstants.TAG_APPLICATION;

public class IconDetector extends ResourceXmlDetector implements Detector.XmlScanner {

    public static final Issue ISSUE_ICON_IN_LIBRARY = Issue.create(
            "IconInLibraryDetector",
            "Icon in library",
            "This library should not have an icon.",
            Category.CORRECTNESS,
            8,
            Severity.ERROR,
            new Implementation(IconDetector.class, Scope.MANIFEST_SCOPE));

    private List<Node> mApplicationTagsWithIcon;
    private List<Location> mIconAttributesLocations;

    @Override
    public Collection<String> getApplicableElements() {
        return Collections.singletonList(TAG_APPLICATION);
    }

    @Override
    public void beforeCheckProject(@NonNull Context context) {
        mApplicationTagsWithIcon = new ArrayList<Node>();
        mIconAttributesLocations = new ArrayList<Location>();
    }

    @Override
    public void visitElement(XmlContext context, Element element) {

        if (!element.getAttribute("icon").equals("")) {
            mApplicationTagsWithIcon.add(element);
            mIconAttributesLocations.add(context.getLocation(element.getAttributes().getNamedItem("icon")));
        }

    }

    @Override
    public void afterCheckProject(@NonNull Context context) {

        // if it's not a library, it's an application
        if (context.getProject() == context.getMainProject() && context.getMainProject().isLibrary()) {
            for (int i = 0; i < mIconAttributesLocations.size(); i++) {
                context.report(ISSUE_ICON_IN_LIBRARY, mIconAttributesLocations.get(i),
                        "Expecting " + ANDROID_MANIFEST_XML + " not to have an icon inside <" + TAG_APPLICATION + "> tag");
            }
        }
    }
}
