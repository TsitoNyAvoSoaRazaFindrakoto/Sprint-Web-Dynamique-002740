package mg.itu.prom16.Annotations;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import mg.itu.prom16.types.Mapping;

public class AnnotationFinder {
    
    private static ArrayList<Class<?>> getControllerList(ServletContext sc ,String packagename) throws ServletException {
        String bin_path = "WEB-INF/classes/" + packagename.replace(".", "/");

        bin_path = sc.getRealPath(bin_path);
        
        File b = new File(bin_path);

        if (!b.exists()) {
            throw new ServletException(" package '" + packagename + "' does not exist ");
        }

        ArrayList<Class<?>> controllerArrayList = new ArrayList<Class<?>>();

        for (File onefile : b.listFiles()) {
            if (onefile.isFile() && onefile.getName().endsWith(".class")) {
                Class<?> clazz;
                try {
                    clazz = Class.forName(packagename + "." + onefile.getName().split(".class")[0]);
                    if (clazz.isAnnotationPresent(mg.itu.prom16.Annotations.Controller.class))
                        controllerArrayList.add(clazz);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        // if (controllerArrayList.isEmpty()) {
        //     throw new ServletException("Package vide");
        // }
        return controllerArrayList;
    }

    

    private static HashMap<String, Mapping> allGetMethods(Class<?> location) throws ServletException{
        HashMap<String,Mapping> map = new HashMap<String,Mapping>();
        Method[] allMethods = location.getDeclaredMethods();

        for (Method method : allMethods) {
            if(method.isAnnotationPresent(mg.itu.prom16.Annotations.Get.class)){
                String urlValue = method.getAnnotation(mg.itu.prom16.Annotations.Get.class).url();
                if(map.containsKey(urlValue)){
                    throw new ServletException(urlValue + " already exists");
                }
                map.put(urlValue,new Mapping(location.getName(),method.getName()));
            }
        }

        return map;  
    }

    public static  HashMap<String,Mapping> urlMapping(ServletContext sc ,String initparameter)throws ServletException{
        ArrayList<Class<?>> clazzList = AnnotationFinder.getControllerList(sc,sc.getInitParameter(initparameter));
        HashMap<String,Mapping> urlMap = new HashMap<String,Mapping>();
        for(Class<?> clazz : clazzList){
            urlMap.putAll(AnnotationFinder.allGetMethods(clazz));
        }
        return urlMap;
    }

}
