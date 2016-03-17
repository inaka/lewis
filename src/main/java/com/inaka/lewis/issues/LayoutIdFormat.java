package com.inaka.lewis.issues;

import com.android.annotations.NonNull;
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.Speed;
import com.android.tools.lint.detector.api.XmlContext;

import org.w3c.dom.Attr;

import java.util.Collection;
import java.util.Collections;

import static com.android.SdkConstants.ANDROID_URI;
import static com.android.SdkConstants.ATTR_ID;

public class LayoutIdFormat extends LayoutDetector {

    public static final Issue ISSUE = Issue.create(
            "LayoutIDFormat",
            "Layout id format",
            "The id of a layout must be written using lowerCamelCase.",
            Category.TYPOGRAPHY,
            8,
            Severity.ERROR,
            new Implementation(LayoutIdFormat.class, Scope.RESOURCE_FILE_SCOPE));

    @NonNull
    @Override
    public Speed getSpeed() {
        return Speed.FAST;
    }

    @Override
    public Collection<String> getApplicableAttributes() {
        return Collections.singletonList(ATTR_ID);
    }

    @Override
    public boolean appliesTo(@NonNull ResourceFolderType folderType) {
        return folderType == ResourceFolderType.LAYOUT;
    }

    @Override
    public void visitAttribute(@NonNull XmlContext context, @NonNull Attr attribute) {
        String value = attribute.getValue();

        if (!value.isEmpty()) {

            // Make sure this is really one of the android: attributes
            if (!ANDROID_URI.equals(attribute.getNamespaceURI())) {
                return;
            }

            value = value.substring(5); // ignore "@+id/"
            
            if(!idCorrectFormat(value)){
                context.report(ISSUE, attribute, context.getLocation(attribute),
                        String.format("The id \"%1$s\", should be written using lowerCamelCase.", value));
            }

        }
    }

    private boolean idCorrectFormat(String value) {
        return Character.isLowerCase(value.charAt(0)) && !value.contains("_") && !value.contains("-");
    }

}
