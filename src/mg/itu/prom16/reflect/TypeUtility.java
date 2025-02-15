package mg.itu.prom16.reflect;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
				c == String.class ||
				c == LocalDate.class ||
				c == LocalDateTime.class;
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
			throw new IllegalArgumentException("Argument is null or empty");
		}
		boolean isPrimitive = targetType.isPrimitive();
		if (isPrimitive) {
			targetType = getWrapperType(targetType);
		}
		if (targetType == LocalDate.class) {
			return LocalDate.parse(value);
		} else if (targetType == LocalDateTime.class) {
			return LocalDateTime.parse(value);
		} else {
			Method valueOfMethod;
			try {
				valueOfMethod = targetType.getMethod("valueOf", String.class);
			} catch (NoSuchMethodException e) {
				throw new IllegalArgumentException("No valueOf(String) method for " + targetType.getName());
			}
			Object result = valueOfMethod.invoke(null, value);
			if (isPrimitive) {
				return convertToPrimitive(result, targetType);
			}
			return result;
		}
	}

	// Helper method to convert a wrapper result back to its primitive if needed
	private static Object convertToPrimitive(Object result, Class<?> wrapperType) {
		if (wrapperType == Integer.class) return ((Integer) result).intValue();
		if (wrapperType == Boolean.class) return ((Boolean) result).booleanValue();
		if (wrapperType == Byte.class) return ((Byte) result).byteValue();
		if (wrapperType == Character.class) return ((Character) result).charValue();
		if (wrapperType == Double.class) return ((Double) result).doubleValue();
		if (wrapperType == Float.class) return ((Float) result).floatValue();
		if (wrapperType == Long.class) return ((Long) result).longValue();
		if (wrapperType == Short.class) return ((Short) result).shortValue();
		return result;
	}

	public static boolean isCommonJavaType(Class<?> clazz) {
		return clazz.isPrimitive() ||
				TypeUtility.primitiveToWrapperMap.containsValue(clazz) ||
				clazz == String.class ||
				Number.class.isAssignableFrom(clazz) ||
				clazz == Boolean.class ||
				clazz == Character.class ||
				clazz == LocalDate.class ||
				clazz == LocalDateTime.class ||
				TypeUtility.canCast(clazz);
	}

	public static void main(String[] args) throws Exception {
		Object o = String.valueOf("hello");
		System.out.println(o.getClass());
	}

}
