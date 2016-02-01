package com.inaka.lewis.issues;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;

import java.lang.reflect.Type;
import java.util.List;

import lombok.ast.AstVisitor;
import lombok.ast.ClassDeclaration;
import lombok.ast.EnumDeclaration;
import lombok.ast.ForwardingAstVisitor;
import lombok.ast.InterfaceDeclaration;
import lombok.ast.Node;
import lombok.ast.StrictListAccessor;
import lombok.ast.TypeVariable;

public class JavaVariablesDetector extends Detector implements Detector.JavaScanner {

    public static final Issue ISSUE_PRIVATE_VARIABLE_NAME = Issue.create(
            "PrivateVariableName",
            "A private variable should be named beginning with 'm'",
            "Every private variable should be named beginning with 'm', for example: 'mCounter'.",
            Category.CORRECTNESS,
            4,
            Severity.WARNING,
            new Implementation(JavaVariablesDetector.class, Scope.JAVA_FILE_SCOPE));

    @Override
    public AstVisitor createJavaVisitor(@NonNull final JavaContext context) {
        return new ForwardingAstVisitor() {
            @Override
            public boolean visitClassDeclaration(ClassDeclaration node) {

                List<Node> elements = node.getChildren();
                StrictListAccessor<TypeVariable, ClassDeclaration> variables = node.astTypeVariables();
                
                for(TypeVariable typeVariable : variables){
                    typeVariable.getChildren().
                }

                return super.visitClassDeclaration(node);
            }
        };
    }
}
