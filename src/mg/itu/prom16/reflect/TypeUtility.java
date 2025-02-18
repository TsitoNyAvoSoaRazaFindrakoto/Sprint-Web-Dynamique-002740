package mg.itu.prom16.reflect;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

	private static Class<?> getWrapperType(Class<?> primitiveType) {
		return primitiveToWrapperMap.get(primitiveType);
	}

	public static boolean canCast(Class<?> c) {
		return primitiveToWrapperMap.containsKey(c) ||
				primitiveToWrapperMap.containsValue(c) ||
				c == String.class ||
				c == LocalDate.class ||
				c == LocalDateTime.class ||
				c == LocalTime.class;
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
		} else if (targetType == LocalTime.class) {
			return LocalTime.parse(value);
		} else {
			try {
				Method valueOfMethod = targetType.getMethod("valueOf", String.class);
				Object result = valueOfMethod.invoke(null, value);
				return isPrimitive ? convertToPrimitive(result, targetType) : result;
			} catch (NoSuchMethodException e) {
				throw new IllegalArgumentException("No valueOf(String) method for " + targetType.getName());
			}
		}
	}

	private static Object convertToPrimitive(Object result, Class<?> wrapperType) {
		if (wrapperType == Integer.class)
			return ((Integer) result).intValue();
		if (wrapperType == Boolean.class)
			return ((Boolean) result).booleanValue();
		if (wrapperType == Byte.class)
			return ((Byte) result).byteValue();
		if (wrapperType == Character.class)
			return ((Character) result).charValue();
		if (wrapperType == Double.class)
			return ((Double) result).doubleValue();
		if (wrapperType == Float.class)
			return ((Float) result).floatValue();
		if (wrapperType == Long.class)
			return ((Long) result).longValue();
		if (wrapperType == Short.class)
			return ((Short) result).shortValue();
		return result;
	}

	public static boolean isCommonJavaType(Class<?> clazz) {
		return clazz.isPrimitive() ||
				primitiveToWrapperMap.containsValue(clazz) ||
				clazz == String.class ||
				Number.class.isAssignableFrom(clazz) ||
				clazz == Boolean.class ||
				clazz == Character.class ||
				clazz == LocalDate.class ||
				clazz == LocalDateTime.class ||
				clazz == LocalTime.class ||
				canCast(clazz);
	}
}