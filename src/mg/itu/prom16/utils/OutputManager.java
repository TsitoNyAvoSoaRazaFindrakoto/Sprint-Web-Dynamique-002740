package mg.itu.prom16.utils;

import java.lang.reflect.InvocationTargetException;

public class OutputManager {
    private static Object getOneInstance(Class<?> toinstance){
        try {
            return toinstance.getConstructor(new Class<?>[0]).newInstance(new Object[0]);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            // TODO Auto-generated catch block
            return new Exception(e.getMessage());
        }
    }

    private static Object callMethod(Class<?> location , String method){
        Object callerObject = OutputManager.getOneInstance(location);
        if (callerObject instanceof Exception) {
            return callerObject;
        }
        try {
            return location.getDeclaredMethod(method, new Class<?>[0]).invoke(method, new Object[0]);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            // TODO Auto-generated catch block
            return new Exception(e.getMessage());
        }
    }
    
    public static String returnString(Mapping map){
        Class<?> methodsource;
        try {
            methodsource = Class.forName(map.caller(null));
        } catch (ClassNotFoundException e) {
            return e.getMessage();
        }
        Object result = OutputManager.callMethod(methodsource, map.urlmethod(null));
        if (result instanceof Exception) {
            return ((Exception)result).getMessage();
        }
        return String.valueOf(result);
    } 
}
