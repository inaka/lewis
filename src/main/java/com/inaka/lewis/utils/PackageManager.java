package com.inaka.lewis.utils;

import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Location;

import lombok.ast.Node;

public class PackageManager {

    public static String getPackage(JavaContext context, Node node) {
        String packageName = context.getMainProject().getPackage();
        Location nodeLocation = getNodeLocation(context, node);

        String classLocationString = nodeLocation.getFile().toString().replaceAll("/", ".");

        int findPackage = classLocationString.lastIndexOf(packageName);
        return classLocationString.substring(findPackage);
    }

    public static Location getNodeLocation(JavaContext context, Node node) {
        return context.getLocation(node);
    }

    public static boolean isGenerated(JavaContext context, Node node) {
        String packageName = context.getMainProject().getPackage();
        Location nodeLocation = getNodeLocation(context, node);

        String classLocationString = nodeLocation.getFile().toString().replaceAll("/", ".");

        int findPackage = classLocationString.lastIndexOf(packageName);

        return classLocationString.substring(0, findPackage).contains("generated");
    }
}
