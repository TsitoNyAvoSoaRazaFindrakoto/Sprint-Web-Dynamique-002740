package mg.itu.prom16.request;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;

import mg.itu.prom16.Annotations.models.FieldAnnotationManager;
import mg.itu.prom16.reflect.ClassIterator;
import mg.itu.prom16.reflect.TypeUtility;

public class ParameterCreator {
	public static Object[] createParameters(Object[][] values , Parameter[] params) throws Exception{
		Object[] result = new Object[values.length];
		for (int i = 0; i < values.length; i++) {
			if (values[i]==null){} 
			else if (params[i].getType().equals(String.class)){
				result[i]=values[i][0];
			} else if (TypeUtility.canCast(params[i].getType()) && values[i][0]!=null) {
				result[i] = TypeUtility.castStringToType(((String)values[i][0]), params[i].getType());
			} else {
				Field[] corresponding_fields = FieldAnnotationManager.getFieldsWithAlternateName(params[i].getType());
				result[i] = ClassIterator.cast_and_create(params[i].getType(),corresponding_fields,values[i]);
			}
		}
		return result;
	}
}
