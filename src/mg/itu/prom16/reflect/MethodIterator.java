package mg.itu.prom16.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MethodIterator {
	public static Object executeMethod(Object b, String name, Object[] params) throws Exception {
		Method m = b.getClass().getMethod(name, ClassIterator.getClasses(params));
		Object result = m.invoke(b, params);
		return result;
	}

	public static Method[] getters(Class<?> c) throws Exception {
		String[] names = FieldIterator.methods_names(c, "get");
		Method[] ms = new Method[names.length];
		for (int i = 0; i < ms.length; i++) {
			ms[i] = c.getMethod(names[i]);
		}
		return ms;
	}

	public static Method setter(Class<?> c, Field field) throws NoSuchMethodException {
		return c.getDeclaredMethod(FieldIterator.method_name(field, "set"), field.getType());
	}

	public static Method[] setters(Class<?> c) throws Exception {
		Field[] fields = c.getDeclaredFields();
		String[] names = FieldIterator.methods_names(c, "set");
		Method[] ms = new Method[names.length];
		for (int i = 0; i < ms.length; i++) {
			ms[i] = c.getDeclaredMethod(names[i], fields[i].getType());
		}
		return ms;
	}

	public static Method setter_string(Class<?> c, Field field) throws NoSuchMethodException {
		return c.getDeclaredMethod(FieldIterator.method_name(field, "set"), String.class);
	}

	public static Method setter_type(Class<?> c, Field field , Class<?> param) throws NoSuchMethodException{
		return c.getDeclaredMethod(FieldIterator.method_name(field, "set"), param);

	}

}