package com.telran.annotation.engine;

import com.telran.annotation.engine.annotations.ProductServiceAnnotation;
import com.telran.annotation.engine.annotations.Run;
import com.telran.annotation.engine.annotations.Value;
import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.Set;

public class Main {

    @SneakyThrows
    public static void main(String[] args) {
//        new ProductService().run();
        Reflections reflections =
            new Reflections("com.telran.annotation.product");

        Set<Class<?>> annotatedWith = reflections.getTypesAnnotatedWith(ProductServiceAnnotation.class);

        for (Class<?> clazz : annotatedWith) {
            //ProductService instance = new ProductService()
            Object instance = clazz.newInstance();

            prepareFields(instance);
            runMethod(instance);
        }
    }

    @SneakyThrows
    private static void prepareFields(Object instance) {
        Field [] fields = instance.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(Value.class)) {
                Properties properties = new Properties();
                properties.load(
                    Main
                    .class
                    .getClassLoader()
                    .getResourceAsStream("application.properties")
                );

                Value annotation = field.getAnnotation(Value.class);

                String filename = properties.getProperty(annotation.valueFromPropertyFile());
                field.setAccessible(true);
                field.set(instance, filename);
            }
        }
    }

    @SneakyThrows
    private static void runMethod(Object instance) {
        Method [] methods = instance.getClass().getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(Run.class)) {
                //ProductService instance = new ProductService()
                //instance.run()
                method.invoke(instance);
            }
        }
    }
}
