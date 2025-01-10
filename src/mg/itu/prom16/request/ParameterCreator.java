package mg.itu.prom16.request;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;

import mg.itu.prom16.annotations.models.FieldAnnotationManager;
import mg.itu.prom16.annotations.validation.Validation;
import mg.itu.prom16.reflect.ClassIterator;
import mg.itu.prom16.reflect.TypeUtility;

public class ParameterCreator {
	public static Object[] createParameters(Object[][] values, Parameter[] params, HashMap<String, List<String>> errors)
			throws Exception {
		Boolean hasErrors = false;
		Object[] result = new Object[values.length];
		for (int i = 0; i < values.length; i++) {
			Class<?> classTobuild = params[i].getType();
			if (values[i] == null)
				continue;
			else if (classTobuild.equals(String.class)) {
				result[i] = values[i][0];
			} else if (TypeUtility.canCast(classTobuild) && values[i][0] != null) {
				result[i] = TypeUtility.castStringToType(((String) values[i][0]), classTobuild);
			} else {
				Field[] corresponding_fields = FieldAnnotationManager.getFieldsWithAlternateName(classTobuild);

				HashMap<String, List<String>> error = Validation.assertObject(classTobuild, corresponding_fields, values[i]);

				if (error==null || error.isEmpty()) {
					result[i] = ClassIterator.cast_and_create(classTobuild, corresponding_fields, values[i]);
				} else {
					if (hasErrors) {
						errors.putAll(error);
					} else {
						hasErrors = true;
						errors.putAll(error);
					}
				}
			}
		}
		return result;
	}
}
