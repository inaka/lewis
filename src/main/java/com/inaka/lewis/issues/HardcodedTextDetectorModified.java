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

import java.util.Arrays;
import java.util.Collection;

import static com.android.SdkConstants.ANDROID_URI;
import static com.android.SdkConstants.ATTR_CONTENT_DESCRIPTION;
import static com.android.SdkConstants.ATTR_HINT;
import static com.android.SdkConstants.ATTR_LABEL;
import static com.android.SdkConstants.ATTR_PROMPT;
import static com.android.SdkConstants.ATTR_TEXT;
import static com.android.SdkConstants.ATTR_TITLE;

public class HardcodedTextDetectorModified extends LayoutDetector {

    public static final Issue ISSUE = Issue.create(
            "HardcodedText",
            "Hardcoded text",
            "This string cannot be hardcoded.\n" +
            "Hardcoding text attributes directly in layout files is bad for several reasons:\n" +
            "\n" +
            "* When creating configuration variations (for example for landscape or portrait)" +
            "you have to repeat the actual text (and keep it up to date when making changes)\n" +
            "\n" +
            "* The application cannot be translated to other languages by just adding new " +
            "translations for existing string resources.\n" +
            "\n" +
            "In Android Studio and Eclipse there are quickfixes to automatically extract this " +
            "hardcoded string into a resource lookup.",
            Category.I18N,
            8,
            Severity.ERROR,
            new Implementation(HardcodedTextDetectorModified.class, Scope.RESOURCE_FILE_SCOPE));

    @NonNull
    @Override
    public Speed getSpeed() {
        return Speed.FAST;
    }

    @Override
    public Collection<String> getApplicableAttributes() {
        return Arrays.asList(
                // Layouts
                ATTR_TEXT,
                ATTR_CONTENT_DESCRIPTION,
                ATTR_HINT,
                ATTR_LABEL,
                ATTR_PROMPT,
                // Menus
                ATTR_TITLE
        );
    }

    @Override
    public boolean appliesTo(@NonNull ResourceFolderType folderType) {
        return folderType == ResourceFolderType.LAYOUT || folderType == ResourceFolderType.MENU;
    }

    @Override
    public void visitAttribute(@NonNull XmlContext context, @NonNull Attr attribute) {
        String value = attribute.getValue();
        if (!value.isEmpty() && (value.charAt(0) != '@' && value.charAt(0) != '?')) {
            // Make sure this is really one of the android: attributes
            if (!ANDROID_URI.equals(attribute.getNamespaceURI())) {
                return;
            }
            context.report(ISSUE, attribute, context.getLocation(attribute),
                    String.format("[I18N] Hardcoded string \"%1$s\", should use `@string` resource",
                            value));
        }
    }

}
