package mg.itu.prom16.reflect;

import java.lang.reflect.Parameter;
import java.time.LocalDate;

public class ClassicCaster {

	public static boolean iscastable(Class c){
		return c.isPrimitive() || c == String.class || c == LocalDate.class;
	}

	public static Object convert(String value, Class<?> goalClass) {
        if(value == null) {
            return null;
        } else if(goalClass == int.class) {
            return Integer.parseInt(value);
        } else if(goalClass == float.class) {
            return Float.parseFloat(value);
        } else if(goalClass == double.class) {
            return Double.parseDouble(value);
        } else if(goalClass == long.class) {
            return Long.parseLong(value);
        } else if(goalClass == short.class) {
            return Short.parseShort(value);
        } else if(goalClass == boolean.class) {
            return Boolean.parseBoolean(value);
        } else if(goalClass == char.class) {
            return value.charAt(0);
        } else if(goalClass == LocalDate.class){
            return LocalDate.parse(value);
        } else return value;
    }
}
