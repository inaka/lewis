package com.inaka.lewis.issues;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.XmlContext;

import org.w3c.dom.Element;

import java.util.Collection;
import java.util.Collections;

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

    @Override
    public Collection<String> getApplicableElements() {
        return Collections.singletonList(TAG_APPLICATION);
    }

    @Override
    public void visitElement(XmlContext context, Element element) {

        if (context.getMainProject().isLibrary()) {

//            if (!element.getAttribute("icon").equals("")) {
                context.report(ISSUE_ICON_IN_LIBRARY, context.getLocation(element),
                        "Expecting " + ANDROID_MANIFEST_XML + " not to have an icon inside <" + TAG_APPLICATION + "> tag");
//            }
        }

    }
}
