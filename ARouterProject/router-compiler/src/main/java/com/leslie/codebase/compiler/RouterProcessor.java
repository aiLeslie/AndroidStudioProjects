package com.leslie.codebase.compiler;

import com.google.auto.service.AutoService;
import com.leslie.codebase.router.annotation.Router;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
import javax.lang.model.element.QualifiedNameable;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;


@SupportedAnnotationTypes("com.leslie.codebase.router.annotation.Router")
@AutoService(Processor.class)
public class RouterProcessor extends AbstractProcessor {
    public static final String AROUTER_SUFFIX = "$$Router";

    private Messager msg;
    private Filer filer;
    private Types typeUtils;
    private Elements elementUtils;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        msg = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();
        typeUtils = processingEnvironment.getTypeUtils();
        elementUtils = processingEnvironment.getElementUtils();
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

                processRouter(router);
            }


            // 完成注释
            return true;
        }
        return false;
    }

    private void processRouter(Element router) {
        createJavaSourceFile((TypeElement) router);

    }

    private void createJavaSourceFile(TypeElement router) {


        String qualifiedName = router.getQualifiedName().toString();
        String packageName = getPackageName(qualifiedName);
        String simpleName = getClassName(qualifiedName);

        // 获得Activity属性节点
        TypeElement activity = elementUtils.getTypeElement("android.app.Activity");
        // HashMap<String, Class<? extends Activity>> V的类型 = Class<? extends Activity>
        ParameterizedTypeName activityClass = ParameterizedTypeName.get(
                ClassName.get(Class.class),
                WildcardTypeName.subtypeOf(ClassName.get(activity))

        );
        ParameterizedTypeName pathClazz = ParameterizedTypeName.get(
                ClassName.get(HashMap.class),
                ClassName.get(String.class),
                activityClass
        );

        ParameterSpec map = ParameterSpec.builder(pathClazz, "map").build();


        MethodSpec load = MethodSpec.methodBuilder("load")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(map)
                .addStatement("map.put($S, $T.class)", router.getAnnotation(Router.class).value(), router)
                .build();

        TypeElement connectable = elementUtils.getTypeElement("com.leslie.codebase.router_api.Connectable");
        TypeSpec routerType = TypeSpec.classBuilder(simpleName + AROUTER_SUFFIX)
                .addSuperinterface(ClassName.get(connectable))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(load)
                .build();

        JavaFile javaFile = JavaFile.builder(packageName, routerType)
                .build();

        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }

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

}
