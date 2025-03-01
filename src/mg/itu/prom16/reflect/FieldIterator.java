package mg.itu.prom16.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

public class FieldIterator {
	public static int getFieldsNumber(Class<?> c){
        return c.getDeclaredFields().length;
    }

    public static Field[] getFields(Class<?> o){
        Field[] fs = o.getDeclaredFields();
        return fs;
    }

    public static Class<?>[] getFieldTypes(Class<?> o){
        Field[] fs = o.getDeclaredFields();
        Class<?>[] result = new Class<?>[fs.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = fs[i].getType();
        }
        return result;
    }

	public static String method_name(Field field , String begin) {
		return begin + (field.getName().charAt(0)+"").toUpperCase() + field.getName().substring(1);
	}

    public static String[] methods_names(Class<?> o,String begin) throws Exception {
        Field[] fs = o.getDeclaredFields();
        String[] names = new String[fs.length];
        for (int i = 0; i < names.length; i++) {
            names[i] =FieldIterator.method_name(fs[i], begin);
        }
        return names;
    }

    public static HashMap<Field,Object> fieldValues(Object o) throws Exception {
        HashMap<Field,Object> fieldHashMap = new HashMap<>();
        
        Field[] fieldsArray = FieldIterator.getFields(o.getClass());
        Method[] gettersArray = MethodIterator.getters(o.getClass());

        for (int i = 0; i < fieldsArray.length; i++) {
            fieldHashMap.put(fieldsArray[i], gettersArray[i].invoke(o));
        }

        return fieldHashMap;
    }

		public static String describe(Field f){
			return "field " + f.getName() + " of type " + f.getType().getSimpleName() + " in " + f.getDeclaringClass().getSimpleName();
		}
}
