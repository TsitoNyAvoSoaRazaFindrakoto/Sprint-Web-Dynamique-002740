package mg.itu.prom16.reflect;

import java.lang.reflect.Method;

public class MethodIterator {
	public static Object executeMethod(Object b , String name , Object[] params) throws Exception {
        Method m = b.getClass().getMethod(name, ClassIterator.getClasses(params));
        Object result = m.invoke(b, params);
        return result;
    }

    public static Method[] getters(Class<Object> c) throws Exception {
        String[] names = FieldIterator.methods_names(c, "get");
        Method[] ms = new Method[names.length];
        for (int i = 0; i < ms.length; i++) {
            ms[i]= c.getMethod(names[i]);
        }
        return ms;
    }
    
    public static Method[] setters(Class<Object> c) throws Exception {
        Class<Object>[] argtype = FieldIterator.getFieldTypes(c);
        String[] names = FieldIterator.methods_names(c, "set");
        Method[] ms = new Method[names.length];
        for (int i = 0; i < ms.length; i++) {
            ms[i]= c.getMethod(names[i],argtype[i]);
        }
        return ms;
    }
}
