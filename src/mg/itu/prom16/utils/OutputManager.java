package mg.itu.prom16.utils;

import java.lang.reflect.InvocationTargetException;

import mg.itu.prom16.returnType.ModelAndView;

public class OutputManager {
    private static Object getOneInstance(Class<?> toinstance){
        try {
            return toinstance.getConstructor(new Class<?>[0]).newInstance(new Object[0]);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return e;
        }
    }

    private static Object callMethod(Class<?> location , String method){
        Object callerObject = OutputManager.getOneInstance(location);
        if (callerObject instanceof Exception) {
            return callerObject;
        }
        try {
            return location.getDeclaredMethod(method, new Class<?>[0]).invoke(callerObject, new Object[0]);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            // TODO Auto-generated catch block
            return new Exception(e.getMessage());
        }
    }
    
    public static Object output(Mapping map){
        Class<?> methodsource;
        try {
            methodsource = Class.forName(map.caller(null));
        } catch (ClassNotFoundException e) {
            return e;
        }
        Object result = OutputManager.callMethod(methodsource, map.urlmethod(null));
        return result;
    }
    
    public static ModelAndView getOuput(Mapping map){
        Object result = OutputManager.output(map);
        ModelAndView v = new ModelAndView("", result);
        if (result instanceof Exception) {
           v.key("error");
           return v; 
        } else if (result instanceof String) {
            v.key("output");
            return v;
        } else{
            return ((ModelAndView)result) ;
        }
    }
}
