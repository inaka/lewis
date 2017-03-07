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
import lombok.ast.Block;
import lombok.ast.ConstructorDeclaration;
import lombok.ast.ForwardingAstVisitor;
import lombok.ast.KeywordModifier;
import lombok.ast.MethodDeclaration;
import lombok.ast.Node;
import lombok.ast.VariableDeclaration;
import lombok.ast.VariableDefinitionEntry;

public class JavaVariablesDetector extends Detector implements Detector.JavaScanner {

    public static final Issue ISSUE_CLASS_CONSTANT_NAME = Issue.create(
            "ClassConstantName",
            "A class constant should be named using UPPER_SNAKE_CASE.",
            "Every class constant (static and final) should be named using UPPER_SNAKE_CASE.",
            Category.TYPOGRAPHY,
            4,
            Severity.WARNING,
            new Implementation(JavaVariablesDetector.class, Scope.JAVA_FILE_SCOPE));

    @Override
    public AstVisitor createJavaVisitor(@NonNull final JavaContext context) {
        return new ForwardingAstVisitor() {

            @Override
            public boolean visitVariableDeclaration(VariableDeclaration node) {

                if (hasClassParent(node) && !PackageManager.isGenerated(context, node)) {

                    Node classDeclaration = node.getParent();

                    String nodeString = node.toString();

                    VariableDefinitionEntry variableDefinition = node.astDefinition().astVariables().first();
                    String name = variableDefinition.astName().astValue();

                    if (isStaticAndFinal(node)) {
                        if (!staticFinalCorrectFormat(name)) {
                            context.report(ISSUE_CLASS_CONSTANT_NAME, context.getLocation(node),
                                    "Expecting " + name + " to be named using UPPER_SNAKE_CASE.");
                        }
                    }
                }

                return super.visitVariableDeclaration(node);
            }

        };
    }

    /**
     * Return if the node is a widget (dependencies injection with ButterKnife).
     *
     * @param nodeString string representing a VariableDeclaration.
     * @return true if it is a widget, false if not.
     */
    private boolean isWidget(String nodeString) {
        return nodeString.contains("@InjectView") || nodeString.contains("@Bind");
    }

    /**
     * Return if the variable is static or final.
     *
     * @param variableDeclaration the variable to evaluate.
     * @return true if it is static or final, false if not.
     */
    private boolean isStaticOrFinal(VariableDeclaration variableDeclaration) {
        boolean isStaticOrFinal = false;
        for (KeywordModifier keywordModifier : variableDeclaration.astDefinition().astModifiers().astKeywords()) {
            if (keywordModifier.astName().equals("static") || keywordModifier.astName().equals("final")) {
                isStaticOrFinal = true;
            }
        }
        return isStaticOrFinal;
    }

    /**
     * Return if the variable is static and final.
     *
     * @param variableDeclaration the variable to evaluate.
     * @return true if it is static and final, false if not.
     */
    private boolean isStaticAndFinal(VariableDeclaration variableDeclaration) {
        boolean isStatic = false;
        boolean isFinal = false;

        for (KeywordModifier keywordModifier : variableDeclaration.astDefinition().astModifiers().astKeywords()) {
            if (keywordModifier.astName().equals("static")) {
                isStatic = true;
            }
            if (keywordModifier.astName().equals("final")) {
                isFinal = true;
            }
        }

        return isStatic && isFinal;
    }

    /**
     * Check if a class constant has the correct format (UPPER_SNAKE_CASE).
     *
     * @param name is the name of the constant.
     * @return true if has the correct format, false if not.
     */
    private boolean staticFinalCorrectFormat(String name) {
        return name.equals(name.toUpperCase());
    }

    /**
     * Check if a class is a Model (is inside a package called 'models').
     *
     * @param context          is the context of the Java code.
     * @param classDeclaration represents the class.
     * @return true if it is a model, false if not.
     */
    private boolean isModel(JavaContext context, Node classDeclaration) {
        String classFilePackage = PackageManager.getPackage(context, classDeclaration);
        return classFilePackage.contains(".models.");
    }

    /**
     * Check if a variable declaration if inside a class scope (skip other local variables).
     *
     * @param variableDeclaration represents the variable.
     * @return true if it is inside a class, false if not.
     */
    private boolean hasClassParent(VariableDeclaration variableDeclaration) {
        MethodDeclaration methodDeclaration = variableDeclaration.astDefinition().upIfParameterToMethodDeclaration();
        ConstructorDeclaration constructorDeclaration = variableDeclaration.astDefinition().upIfParameterToConstructorDeclaration();
        Block block = variableDeclaration.astDefinition().upUpIfLocalVariableToBlock();
        return methodDeclaration == null && constructorDeclaration == null && block == null;
    }

}
