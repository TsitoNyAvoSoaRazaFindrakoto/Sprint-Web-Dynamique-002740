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
			names[i] =((Class)o[i]).getName();
		}
		return names;
	}

	public static Object construct(Class c) throws Exception{
		return c.getConstructor(new Class[0]).newInstance(new Object[0]);
	}

	public static Object create(Class c , Object[] field) throws Exception {
		Object o = ClassIterator.construct(c);
		Method[] m = MethodIterator.setters(c);
		for (int i = 0; i < m.length; i++) {
			m[i].invoke(o, field[i]);
		}
		return o;
	}


	/**
	 * 
	 * @param Object o
	 * 
	 * <p> customized getClass
	 * 	<ul>
	 * 		<li><b>0<b/> for int and such
	 * 		<li><b>1<b/> for double and such
	 * 		<li><b>2<b/> for String my boi
	 * 		<li><b>3<b/> for sql dates 
	 * 		<li><b>4<b/> for locatldate 
	 * 		<li><b>5<b/> fro boolean
	 * @return the value of the class
	 */
	public static int classValue(Object o ){
		if (o instanceof Integer || o instanceof BigInteger) return 0;
		if (o instanceof Double || o instanceof BigDecimal) return 1;
		if (o instanceof String) return 2;
		if (o instanceof java.sql.Date ) return 3;
		if (o instanceof java.time.LocalDate ) return 4;
		if (o instanceof Boolean ) return 5 ;
		else return -1;
	}
}
