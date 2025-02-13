package mg.itu.prom16.reflect;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class TypeUtility {

	private static final Map<Class<?>, Class<?>> primitiveToWrapperMap = new HashMap<>();

	static {
		primitiveToWrapperMap.put(int.class, Integer.class);
		primitiveToWrapperMap.put(boolean.class, Boolean.class);
		primitiveToWrapperMap.put(byte.class, Byte.class);
		primitiveToWrapperMap.put(char.class, Character.class);
		primitiveToWrapperMap.put(double.class, Double.class);
		primitiveToWrapperMap.put(float.class, Float.class);
		primitiveToWrapperMap.put(long.class, Long.class);
		primitiveToWrapperMap.put(short.class, Short.class);
		primitiveToWrapperMap.put(void.class, Void.class);
	}

	// Method to get the wrapper class for a given primitive type
	private static Class<?> getWrapperType(Class<?> primitiveType) {
		return primitiveToWrapperMap.get(primitiveType);
	}

	// Method to check if a class can be cast from a String
	public static boolean canCast(Class<?> c) {
		return primitiveToWrapperMap.containsKey(c) ||
				primitiveToWrapperMap.containsValue(c) ||
				c == String.class;
	}

	public static boolean isSameType(Class<?> a, Class<?> b) {
		if (a.isPrimitive() && !b.isPrimitive()) {
			return getWrapperType(a).equals(b);
		}
		if (!a.isPrimitive() && b.isPrimitive()) {
			return a.equals(getWrapperType(b));
		}
		return a.equals(b);
	}

	// Method to cast a string to a desired type
	public static Object castStringToType(String value, Class<?> targetType) throws Exception {
		if (value == null || value.isEmpty()) {
			throw new IllegalArgumentException("argument is null");
		}
		boolean isPrimitive = targetType.isPrimitive();
		if (isPrimitive) {
		targetType = getWrapperType(targetType);
		}
		Method valueOfMethod = targetType.getMethod("valueOf", String.class);
		Object result = valueOfMethod.invoke(null, value);

		// If the original target type was primitive, convert back to primitive type
		
		if (isPrimitive) {
		// System.out.println("primitive");
			if (targetType == Integer.class) return (int) result;
			if (targetType == Boolean.class) return (boolean) result;
			if (targetType == Byte.class) return (byte) result;
			if (targetType == Character.class) return (char) result;
			if (targetType == Double.class) return (double) result;
			if (targetType == Float.class) return (float) result;
			if (targetType == Long.class) return (long) result;
			if (targetType == Short.class) return (short) result;
		}
		

		return result;
	}

	public static boolean isCommonJavaType(Class<?> clazz) {
		return clazz.isPrimitive() ||
				TypeUtility.primitiveToWrapperMap.containsValue(clazz) ||
				clazz == String.class ||
				Number.class.isAssignableFrom(clazz) ||
				clazz == Boolean.class ||
				clazz == Character.class ||
				TypeUtility.canCast(clazz);
	}

	public static void main(String[] args) throws Exception {
		Object o = String.valueOf("hello");
		System.out.println(o.getClass());
	}

}
