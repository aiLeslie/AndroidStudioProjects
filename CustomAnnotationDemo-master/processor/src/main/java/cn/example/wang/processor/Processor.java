package cn.example.wang.processor;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * 消息处理器为注释处理器提供了报告错误消息、警告和其他通知的方式。可以传递元素、注释和注释值，以提供消息的位置提示。然而，这样的位置提示可能是不可用的或仅仅是近似的。
 * <p>
 * 打印带有错误类型的消息将引发错误。
 * 注意，该接口中的方法“打印”的消息可能会或可能不会作为文本输出出现在Syr.OUT或Stult.Err这样的位置。实现可以选择以不同的方式呈现这些信息，例如窗口中的消息。
 *
 * @ProcessingEnvironment 注释处理工具框架将提供一个注释处理器，该对象具有实现该接口的对象，因此处理器可以使用框架提供的设施来编写新文件、报告错误消息和查找其他实用程序。
 */
public class Processor extends AbstractProcessor {
    Elements elementUtils;
    Messager messager;
    Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        messager = processingEnvironment.getMessager();
        //返回用于操作元素的一些实用方法的实现
        elementUtils = processingEnvironment.getElementUtils();
        Types typeUtils = processingEnvironment.getTypeUtils();
        filer = processingEnvironment.getFiler();


        SourceVersion sourceVersion = processingEnvironment.getSourceVersion();
        Locale locale = processingEnvironment.getLocale();
        //返回了这个注解处理器的特权配置
        Map<String, String> options = processingEnvironment.getOptions();
    }

    /**
     * process()函数中不能直接进行异常抛出,否则的话,运行Annotation Processor的进程会异常崩溃,然后弹出一大堆让人捉摸不清的堆栈调用日志显示.
     *
     * @param set
     * @param roundEnvironment
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        for (Element element : roundEnvironment.getElementsAnnotatedWith(MyAnnotion.class)) {
            messager.printMessage(Diagnostic.Kind.NOTE, "记录一些日志------------------");
            // 显示转换元素类型
            //VariableElement  表示FIELD   TypeElement 表示 type
/*
*
* - Element
  所说的元素就是指类,方法, 包,属性等. 对于Element的理解可以想成XML的Element结构.下面来个例子看一下,这里呢只是大概的解释一下,还是不理解的同学建议去看一下java的JDK.
      package com.example;    // PackageElement 包元素
      public class Foo {        // TypeElement  类型元素
          private int a;      // VariableElement  属性元素
          private Foo other;  // VariableElement
          public Foo () {}    // ExecuteableElement  方法元素
          public void setA (  // ExecuteableElement   int newA // TypeElement ) {
          }
      }
      //这些元素涉及到内容比较多,这里先不一一概述.
      ```

-

    }
}
*
* */

            messager.printMessage(Diagnostic.Kind.NOTE, "Element :    " + element);
            List<? extends AnnotationMirror> allAnnotationMirrors = elementUtils.getAllAnnotationMirrors(element);
            for (int i = 0; i <allAnnotationMirrors.size(); i++) {
                AnnotationMirror annotationMirror = allAnnotationMirrors.get(i);
                Map<? extends ExecutableElement, ? extends AnnotationValue> elementValuesWithDefaults = elementUtils.getElementValuesWithDefaults(annotationMirror);

            }

            if(element instanceof TypeElement){
                TypeElement typeElement = (TypeElement) element;
                Name binaryName = elementUtils.getBinaryName(typeElement);
            }

            TypeElement mainActivity = elementUtils.getTypeElement("cn.example.wang.routerdemo.MainActivity");

            messager.printMessage(Diagnostic.Kind.NOTE, "getTypeElement------"+mainActivity);




            messager.printMessage(Diagnostic.Kind.NOTE, "日志结束------------------");
        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotataions = new LinkedHashSet<>();
        annotataions.add(MyAnnotion.class.getCanonicalName());
        return annotataions;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
