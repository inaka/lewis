package com.inaka.lewis.issues;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.inaka.lewis.utils.PackageManager;

import lombok.ast.AstVisitor;
import lombok.ast.ClassDeclaration;
import lombok.ast.EnumDeclaration;
import lombok.ast.ForwardingAstVisitor;
import lombok.ast.InterfaceDeclaration;
import lombok.ast.Node;


public class RootPackageDetector extends Detector implements Detector.JavaScanner {

    public static final Issue ISSUE_CLASS_IN_ROOT_PACKAGE = Issue.create(
            "RootPackage",
            "Java file should not be inside root package",
            "Every .java file should be inside a custom package inside the root package.",
            Category.CORRECTNESS,
            8,
            Severity.ERROR,
            new Implementation(RootPackageDetector.class, Scope.JAVA_FILE_SCOPE));

    @Override
    public AstVisitor createJavaVisitor(@NonNull final JavaContext context) {
        return new ForwardingAstVisitor() {
            @Override
            public boolean visitClassDeclaration(ClassDeclaration node) {
                String fileName = node.astName().astValue();
                shouldNotBeInRootPackage(context, node, fileName);
                return super.visitClassDeclaration(node);
            }

            @Override
            public boolean visitInterfaceDeclaration(InterfaceDeclaration node) {
                String fileName = node.astName().astValue();
                shouldNotBeInRootPackage(context, node, fileName);
                return super.visitInterfaceDeclaration(node);
            }

            @Override
            public boolean visitEnumDeclaration(EnumDeclaration node) {
                String fileName = node.astName().astValue();
                shouldNotBeInRootPackage(context, node, fileName);
                return super.visitEnumDeclaration(node);
            }
        };
    }

    /**
     * Check if the node is inside the root package and report an issue in that case.
     *
     * @param context  is the context of the Java code.
     * @param node     represents the element to evaluate.
     * @param fileName is the name of the file.
     */
    private void shouldNotBeInRootPackage(JavaContext context, Node node, String fileName) {

        String packageName = context.getMainProject().getPackage();

        String filePackageString = PackageManager.getPackage(context, node);

        if (filePackageString.equals(packageName + "." + fileName + ".java")
                && !PackageManager.isGenerated(context, node)) {
            context.report(ISSUE_CLASS_IN_ROOT_PACKAGE, PackageManager.getNodeLocation(context, node),
                    " Expecting " + fileName + " not to be in root package " + packageName);
        }

    }

}
