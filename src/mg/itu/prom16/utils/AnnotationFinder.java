package mg.itu.prom16.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;

import mg.itu.prom16.Annotations.Get;

public class AnnotationFinder {
    public static HashMap<String, Mapping> allGetMethods(Class<?> location){
        HashMap<String,Mapping> map = new HashMap<String,Mapping>();
        Method[] allMethods = location.getDeclaredMethods();

        for (Method method : allMethods) {
            if(method.isAnnotationPresent(mg.itu.prom16.Annotations.Get.class)){
                String urlValue = method.getAnnotation(mg.itu.prom16.Annotations.Get.class).url();
                map.put(urlValue,new Mapping(location.getName(),method.getName()));
            }
        }

        return map;
        
    }

}
