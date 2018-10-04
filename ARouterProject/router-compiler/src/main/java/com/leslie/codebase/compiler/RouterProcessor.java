package com.leslie.codebase.compiler;

import com.google.auto.service.AutoService;
import com.leslie.codebase.router.annotation.Router;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes("com.leslie.codebase.router.annotation.Router")
@AutoService(Processor.class)
public class RouterProcessor extends AbstractProcessor {
    public static final String PACKAGE_NAME = "com.routerbase.";
    public static final String AROUTER_SUFFIX = "$$Router";

    private Messager msg;
    private Filer filer;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        msg = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        // 遍历注释类型集合

        for (TypeElement typeElement : set) {
            msg.printMessage(Diagnostic.Kind.NOTE, typeElement.getQualifiedName());
        }
        // 采集注解中的值与对应的java类型
        if (!set.isEmpty()) {
            Set<? extends Element> routers = roundEnvironment.getElementsAnnotatedWith(Router.class);
            for (Element router : routers) {
                msg.printMessage(Diagnostic.Kind.NOTE, router.getSimpleName());

            }
            processRouter(routers);

            // 完成注释
            return true;
        }
        return false;
    }

    private void processRouter(Set<? extends Element> routers) {
        if (routers.isEmpty()) return;

        for (Element router : routers) {
            createJavaSourceFile(router);
        }

    }

    private void createJavaSourceFile(Element router) {


        String qualifiedName = getQualifiedName(router);
        String packageName = getPackageName(qualifiedName);
        String className = getClassName(qualifiedName);


        StringBuilder content = new StringBuilder();
        content.append("import ");
        content.append(packageName);
        content.append(";\n");
        content.append("import com.leslie.codebase.router_api.Connectable;\n");
        content.append("import java.util.HashMap;\n");
        content.append("public class");
        content.append(className + AROUTER_SUFFIX);
        content.append("implements Connectable {\n");
        content.append("@Override\n");
        content.append("public void load(HashMap map) {\n");
        content.append("map.put(");
        content.append("\"");
        content.append(router.getAnnotation(Router.class).value());
        content.append("\", ");
        content.append(qualifiedName);
        content.append(".class);\n");
        content.append("}\n");
        content.append( "}");

        try {
            JavaFileObject sourceFile = filer.createSourceFile(qualifiedName + AROUTER_SUFFIX, router);
            OutputStream out = sourceFile.openOutputStream();
            out.write(content.toString().getBytes());
            createHelloWorld(System.out);

            out.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

//        content.append();
    }

    /**
     * @param element
     * @return 根据元素获得全限定名
     */
    private String getQualifiedName(Element element) {
        return ((TypeElement) element.getEnclosingElement()).getQualifiedName().toString();
    }

    /**
     * @param qualifier
     * @return 截取类名中的包名
     */
    private String getPackageName(String qualifier) {
        return qualifier.substring(0, qualifier.lastIndexOf("."));
    }

    /**
     * @param qualifier
     * @return 截取全限定类名中的类名
     */
    private String getClassName(String qualifier) {
        return qualifier.substring(qualifier.lastIndexOf(".") + 1);
    }


    private void createHelloWorld(PrintStream out) {
        MethodSpec main = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
                .build();

        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(main)
                .build();

        JavaFile javaFile = JavaFile.builder("com.example.helloworld", helloWorld)
                .build();

        try {
            javaFile.writeTo(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
