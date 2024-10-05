package mg.itu.prom16.annotations.models;

import java.lang.reflect.Field;
import java.util.ArrayList;

import mg.itu.prom16.annotations.request.FieldAlternate;

public class FieldAnnotationManager {
	public static Field[] getFieldsWithAlternateName(Class<?> c){
		ArrayList<Field> fields = new ArrayList<Field>();
		for (Field f : c.getDeclaredFields()) {
			if (f.isAnnotationPresent(FieldAlternate.class)  && f.getDeclaredAnnotation(FieldAlternate.class).exclude()) {
				continue;
			} 
			fields.add(f);
		}

		return fields.toArray(new Field[0]);
	}

	public static String[] getAlternateNames(Field[] fields){
		String[] paramId = new String[fields.length];
		for (int i = 0 ; i < fields.length ; i++) {
			String name =fields[i].isAnnotationPresent(FieldAlternate.class) ? fields[i].getAnnotation(FieldAlternate.class).name() : "" ;
			paramId[i] = name.isEmpty() ? fields[i].getName() : name;
		}
		return paramId;
	}

	public static String[] getAlternateFieldName(Class<?> c){
		return getAlternateNames(getFieldsWithAlternateName(c));
	}

}
