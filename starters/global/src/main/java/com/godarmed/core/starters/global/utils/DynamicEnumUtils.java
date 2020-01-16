package com.godarmed.core.starters.global.utils;

import com.alibaba.fastjson.JSON;
import sun.reflect.ConstructorAccessor;
import sun.reflect.FieldAccessor;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;



/**
 * 枚举类操作工具类
 */
public class DynamicEnumUtils {
    private static ReflectionFactory reflectionFactory = ReflectionFactory.getReflectionFactory();

    private static void setFailsafeFieldValue(Field field, Object target, Object value) throws NoSuchFieldException,
            IllegalAccessException {

        // 反射访问私有变量
        field.setAccessible(true);

        /**
         * 接下来，我们将字段实例中的修饰符更改为不再是final，
         * 从而使反射允许我们修改静态final字段。
         */
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        int modifiers = modifiersField.getInt(field);

        // 去掉修饰符int中的最后一位
        modifiers &= ~Modifier.FINAL;
        modifiersField.setInt(field, modifiers);

        FieldAccessor fa = reflectionFactory.newFieldAccessor(field, false);
        fa.set(target, value);
    }

    private static void blankField(Class<?> enumClass, String fieldName) throws NoSuchFieldException,
            IllegalAccessException {
        for (Field field : Class.class.getDeclaredFields()) {
            if (field.getName().contains(fieldName)) {
                AccessibleObject.setAccessible(new Field[]{field}, true);
                setFailsafeFieldValue(field, enumClass, null);
                break;
            }
        }
    }

    private static void cleanEnumCache(Class<?> enumClass) throws NoSuchFieldException, IllegalAccessException {
        blankField(enumClass, "enumConstantDirectory"); // Sun (Oracle?!?) JDK 1.5/6
        blankField(enumClass, "enumConstants"); // IBM JDK
    }

    private static ConstructorAccessor getConstructorAccessor(Class<?> enumClass, Class<?>[] additionalParameterTypes)
            throws NoSuchMethodException {
        Class<?>[] parameterTypes = new Class[additionalParameterTypes.length + 2];
        parameterTypes[0] = String.class;
        parameterTypes[1] = int.class;
        System.arraycopy(additionalParameterTypes, 0, parameterTypes, 2, additionalParameterTypes.length);
        return reflectionFactory.newConstructorAccessor(enumClass.getDeclaredConstructor(parameterTypes));
    }

    private static Object makeEnum(Class<?> enumClass, String value, int ordinal, Class<?>[] additionalTypes,
                                   Object[] additionalValues) throws Exception {
        Object[] parms = new Object[additionalValues.length + 2];
        parms[0] = value;
        parms[1] = Integer.valueOf(ordinal);
        System.arraycopy(additionalValues, 0, parms, 2, additionalValues.length);
        return enumClass.cast(getConstructorAccessor(enumClass, additionalTypes).newInstance(parms));
    }


    /**
     * 获取原有枚举实例
     * @param enumType
     * @return
     */
    public static <T extends Enum<?>> Field getEnumInstance(Class<T> enumType){
        // 0. 检查类型
        if (!Enum.class.isAssignableFrom(enumType)) {
            throw new RuntimeException("class " + enumType + " is not an instance of Enum");
        }

        // 1. 在枚举类中查找“$values”持有者并获取以前的枚举实例
        Field valuesField = null;
        Field[] fields = enumType.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().contains("$VALUES")) {
                valuesField = field;
                break;
            }
        }
        AccessibleObject.setAccessible(new Field[]{valuesField}, true);

        return valuesField;
    }

    /**
     * 判断枚举是否已存在
     * @param values
     * @param enumName
     * @param <T>
     * @return
     */
    public static <T extends Enum<?>> boolean contains(List<T> values, String enumName){
        for (T value : values) {
            if (value.name().equals(enumName)) {
                return true;
            }
        }
        return false;
    }





    /**
     * 将枚举实例添加到作为参数提供的枚举类中
     *
     * @param enumType         要修改的枚举类型
     * @param enumName         添加的枚举类型名字
     * @param additionalTypes  枚举类型参数类型列表
     * @param additionalValues 枚举类型参数值列表
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    public static <T extends Enum<?>> void addEnum(Class<T> enumType, String enumName, Class<?>[] additionalTypes, Object[] additionalValues) {


        // 1. 在枚举类中查找“$values”持有者并获取以前的枚举实例
        Field valuesField = getEnumInstance(enumType);

        try {
            // 2. 将他拷贝到数组
            T[] previousValues = (T[]) valuesField.get(enumType);
            List<T> values = new ArrayList<T>(Arrays.asList(previousValues));


            if(!contains(values,enumName)){
                // 3. 创建新的枚举项
                T newValue = (T) makeEnum(enumType, enumName, values.size(), additionalTypes, additionalValues);
                // 4. 添加新的枚举项
                values.add(newValue);
            }


            // 5. 设定拷贝的数组，到枚举类型
            setFailsafeFieldValue(valuesField, null, values.toArray((T[]) Array.newInstance(enumType, 0)));

            // 6. 清除枚举的缓存
            cleanEnumCache(enumType);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 将枚举实例添加到作为参数提供的枚举类中
     *
     * @param enumType         要修改的枚举类型
     * @param enumNames         添加的枚举类型名称列表
     * @param additionalTypes  枚举类型参数类型列表
     * @param additionalValueList 枚举类型参数值列表
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    public static <T extends Enum<?>> void addEnum(Class<T> enumType, String[] enumNames, Class<?>[] additionalTypes, Object[][] additionalValueList) {

        // 1. 在枚举类中查找“$values”持有者并获取以前的枚举实例
        Field valuesField = getEnumInstance(enumType);

        try {
            // 2. 将他拷贝到数组
            T[] previousValues = (T[]) valuesField.get(enumType);
            List<T> values = new ArrayList<T>(Arrays.asList(previousValues));


            for (int i = 0; i < enumNames.length; i++) {
                if(!contains(values,enumNames[i])){
                    // 3. 创建新的枚举项
                    T newValue = (T) makeEnum(enumType, enumNames[i], values.size(), additionalTypes, additionalValueList[i]);
                    // 4. 添加新的枚举项
                    values.add(newValue);
                }
            }

            // 5. 设定拷贝的数组，到枚举类型
            setFailsafeFieldValue(valuesField, null, values.toArray((T[]) Array.newInstance(enumType, 0)));

            // 6. 清除枚举的缓存
            cleanEnumCache(enumType);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 从作为参数提供的枚举类中将某个枚举实例删除
     *
     * @param enumType         要修改的枚举类型
     * @param enumName         删除的枚举类型名字
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    public static <T extends Enum<?>> void delEnum(Class<T> enumType, String enumName) {

        // 1. 在枚举类中查找“$values”持有者并获取以前的枚举实例
        Field valuesField = getEnumInstance(enumType);

        try {
            // 2. 将他拷贝到数组
            T[] previousValues = (T[]) valuesField.get(enumType);
            List<T> values = new ArrayList<T>(Arrays.asList(previousValues));

            // 3. 删除枚举项
            values = values.stream().filter(item->!item.name().equals(enumName)).collect(Collectors.toList());

            // 4. 设定拷贝的数组，到枚举类型
            setFailsafeFieldValue(valuesField, null, values.toArray((T[]) Array.newInstance(enumType, 0)));

            // 5. 清除枚举的缓存
            cleanEnumCache(enumType);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 将作为参数提供的枚举类中的所有枚举实例删除
     *
     * @param enumType         要清空的枚举类型
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    public static <T extends Enum<?>> void clearEnum(Class<T> enumType) {

        // 1. 在枚举类中查找“$values”持有者并获取以前的枚举实例
        Field valuesField = getEnumInstance(enumType);

        try {
            // 2. 将他拷贝到数组
            T[] previousValues = (T[]) valuesField.get(enumType);
            List<T> values = new ArrayList<>();

            // 3. 设定拷贝的数组，到枚举类型
            setFailsafeFieldValue(valuesField, null, values.toArray((T[]) Array.newInstance(enumType, 0)));

            // 4. 清除枚举的缓存
            cleanEnumCache(enumType);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 将作为参数提供的枚举类中的枚举实例重置
     *
     * @param enumType         要重置的枚举类型
     * @param enumNames         重置后的枚举类型名称列表
     * @param additionalTypes  重置后的枚举类型参数类型列表
     * @param additionalValueList 重置后的枚举类型参数值列表
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    public static <T extends Enum<?>> void resetEnum(Class<T> enumType, String[] enumNames, Class<?>[] additionalTypes, Object[][] additionalValueList) {

        // 1. 在枚举类中查找“$values”持有者并获取以前的枚举实例
        Field valuesField = getEnumInstance(enumType);

        try {
            // 2. 将他拷贝到数组
            List<T> values = new ArrayList<T>();

            for (int i = 0; i < enumNames.length; i++) {
                if(!contains(values,enumNames[i])){
                    // 3. 创建新的枚举项
                    T newValue = (T) makeEnum(enumType, enumNames[i], values.size(), additionalTypes, additionalValueList[i]);
                    // 4. 添加新的枚举项
                    values.add(newValue);
                }
            }

            // 5. 设定拷贝的数组，到枚举类型
            setFailsafeFieldValue(valuesField, null, values.toArray((T[]) Array.newInstance(enumType, 0)));

            // 6. 清除枚举的缓存
            cleanEnumCache(enumType);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 枚举类修改测试
     * @param args
     */
    public static void main(String[] args) {
        //枚举修改测试
        //1.添加
        DynamicEnumUtils.addEnum(TestEnum.class,"NUM100",new Class[]{Integer.class},new Object[]{100});
        System.out.println("增加后:"+ JSON.toJSONString(TestEnum.values()));
        //2.删除
        DynamicEnumUtils.delEnum(TestEnum.class,"NUM100");
        System.out.println("删除后:"+ JSON.toJSONString(TestEnum.values()));
        //3.重置
        DynamicEnumUtils.resetEnum(TestEnum.class,new String[]{"NUM_888","NUM_999"},new Class[]{Integer.class},new Object[][]{
                new Object[]{888},new Object[]{999}
        });
        System.out.println("重置后:"+ JSON.toJSONString(TestEnum.values()));
        //3.清空
        DynamicEnumUtils.clearEnum(TestEnum.class);
        System.out.println("清空后:"+ JSON.toJSONString(TestEnum.values()));
    }

    static enum TestEnum {
        /**
         * 默认枚举值
         */
        NUM1(1),
        NUM2(2),
        NUM3(3),
        NUM4(4),
        NUM5(5);

        private Integer num;

        TestEnum(Integer num){
            this.num = num;
        }

        public Integer getNum() {
            return num;
        }

        public static boolean isInEnum(Integer currentNum) {
            for (TestEnum num : TestEnum.values()) {
                if (num.getNum().equals(currentNum)) {
                    return true;
                }
            }
            return false;
        }
    }

}