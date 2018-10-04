package com.rhythm7.lib_compiler;

import com.rhythm7.lib_annotations.RandomInt;

import java.util.Random;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

class AnnotatedRandomInt extends AnnotatedRandomElement {
    // 数值最小值
    private int minValue;
    // 数值最大值
    private int maxValue;

    /**
     * 构造函数
     * @param element
     */
    AnnotatedRandomInt(Element element) {
        // 传入父类构造
        super(element);
        // 获取注释中的属性

        minValue = element.getAnnotation(RandomInt.class).minValue(); // 数值最小值
        maxValue = element.getAnnotation(RandomInt.class).maxValue(); // 数值最大值
    }

    /**
     * 类型是否有效
     * @param elements
     * @param types
     * @return 传入的元素的类型和参数类型是否一致
     */
    @Override
    public boolean isTypeValid(Elements elements, Types types) {
        return element.asType().getKind().equals(TypeKind.INT);
    }

    /**
     * @return 数值范围中的随机数值字符串
     */
    @Override
    public String getRandomValue() {
        Random random = new Random();
        return "" + (minValue + random.nextInt(maxValue - minValue + 1));
    }

}
