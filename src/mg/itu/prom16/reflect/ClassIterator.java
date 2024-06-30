package mg.itu.prom16.reflect;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;

public class ClassIterator {
	public static Class[] getClasses(Object[] o) {
		Class[] c = new Class[o.length];
		for (int i = 0; i < c.length; i++) {
			c[i]=o[i].getClass();
		}
		return c;
	}

	public static Class getClass(Object o) {
		return o.getClass();
	}

	public static String[] getNameClasses(Object[] o){
		o = ClassIterator.getClasses(o);
		String[] names = new String[o.length];
		for (int i = 0; i < names.length; i++) {
			names[i] = ((Class)o[i]).getName();
		}
		return names;
	}

	public static void create(Object o , Object[] field) throws Exception {
		Method[] m = MethodIterator.setters(o.getClass());
		for (int i = 0; i < m.length ; i++) {
			m[i].invoke(o,field[i]);
		}
	}

	public static Object instance(Class<?> toinstance) throws Exception{
		return toinstance.getConstructor(new Class<?>[0]).newInstance(new Object[0]);
	}

	public static void create_string(Object c, Object[] args) throws Exception {
		Method[] m = MethodIterator.setters_string(c.getClass());
		for (int i = 0; i < m.length ; i++) {
			m[i].invoke(c,args[i]);
		}
	}

	public static Object createString(Class<?> c , Object[] args) throws Exception{
		Object instance = ClassIterator.instance(c);
		ClassIterator.create_string(instance, args);
		return instance;
	}

}
