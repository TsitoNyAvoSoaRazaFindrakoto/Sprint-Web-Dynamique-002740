package mg.itu.prom16.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ClassIterator {
	public static Class<?>[] getClasses(Object[] o) {
		Class<?>[] c = new Class<?>[o.length];
		for (int i = 0; i < c.length; i++) {
			c[i] = o[i].getClass();
		}
		return c;
	}

	public static Class<?> getClass(Object o) {
		return o.getClass();
	}

	public static String[] getNameClasses(Object[] o) {
		o = ClassIterator.getClasses(o);
		String[] names = new String[o.length];
		for (int i = 0; i < names.length; i++) {
			names[i] = ((Class<?>) o[i]).getName();
		}
		return names;
	}

	public static void create(Object o, Object[] field) throws Exception {
		Method[] m = MethodIterator.setters(o.getClass());
		for (int i = 0; i < m.length; i++) {
			m[i].invoke(o, field[i]);
		}
	}

	public static Object instance(Class<?> toinstance) throws Exception {
		return toinstance.getConstructor(new Class<?>[0]).newInstance(new Object[0]);
	}

	// public static void create_string(Object c, Object[] args) throws Exception {
	// Method[] m = MethodIterator.setters_string(c.getClass());
	// for (int i = 0; i < m.length ; i++) {
	// m[i].invoke(c,args[i]);
	// }
	// }

	// public static Object createString(Class<?><?> c , Object[] args) throws
	// Exception{
	// Object instance = ClassIterator.instance(c);
	// ClassIterator.create_string(instance, args);
	// return instance;
	// }

	public static Object cast_and_create(Class<?> c, Field[] fields, Object[] args) throws Exception {
		Object instance = ClassIterator.instance(c);
		for (int i = 0; i < fields.length; i++) {
			if (args[i] == null) {
				continue;
			}

			Object fieldvalue = args[i];
			if (fields[i].getType() != String.class && TypeUtility.canCast(fields[i].getType())) {
				fieldvalue = TypeUtility.castStringToType(((String) fieldvalue), fields[i].getType());
				// throw new Exception(" casted : " + fieldvalue.getClass() + " -- " +
				// fields[i].getType());
			}

			Method m;
			if (TypeUtility.isSameType(fieldvalue.getClass(), fields[i].getType())) {
				m = MethodIterator.setter(c, fields[i]);
			} else if (fieldvalue instanceof String) {
				m = MethodIterator.setter_string(c, fields[i]);
			} else {
				m = MethodIterator.setter_type(c, fields[i], fieldvalue.getClass());
			}
			m.invoke(instance, fieldvalue);
		}
		return instance;
	}

}
