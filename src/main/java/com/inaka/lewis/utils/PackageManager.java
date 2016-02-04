package com.inaka.lewis.utils;

import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Location;

import lombok.ast.Node;

public class PackageManager {

    /**
     * Get the package where a file is located.
     *
     * @param context is the context of the Java code.
     * @param node    represents the file.
     * @return the package.
     */
    public static String getPackage(JavaContext context, Node node) {
        String packageName = context.getMainProject().getPackage();
        Location nodeLocation = getNodeLocation(context, node);

        String classLocationString = nodeLocation.getFile().toString().replaceAll("/", ".");

        int findPackage = classLocationString.lastIndexOf(packageName);
        return classLocationString.substring(findPackage);
    }

    /**
     * Return the location of a node.
     *
     * @param context is the context of the Java code.
     * @param node    is the node to evaluate.
     * @return the location of the node.
     */
    public static Location getNodeLocation(JavaContext context, Node node) {
        return context.getLocation(node);
    }

    /**
     * Return if the node (file) is auto generated or not.
     *
     * @param context is the context of the Java code.
     * @param node    represents the file.
     * @return true if is auto generated, false if not.
     */
    public static boolean isGenerated(JavaContext context, Node node) {
        String packageName = context.getMainProject().getPackage();
        Location nodeLocation = getNodeLocation(context, node);

        String classLocationString = nodeLocation.getFile().toString().replaceAll("/", ".");

        int findPackage = classLocationString.lastIndexOf(packageName);

        return classLocationString.substring(0, findPackage).contains("generated");
    }
}
