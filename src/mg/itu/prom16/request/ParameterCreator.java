package mg.itu.prom16.request;

import java.lang.reflect.Parameter;

import mg.itu.prom16.reflect.ClassIterator;
import mg.itu.prom16.reflect.ClassicCaster;

public class ParameterCreator {
	public static Object[] createParameters(String[][] values , Parameter[] params) throws Exception{
		Object[] result = new Object[values.length];
		for (int i = 0; i < values.length; i++) {
			if (ClassicCaster.iscastable(params[i].getType()) && values[i][0]!=null) {
				result[i] = ClassicCaster.convert(values[i][0], params[i].getType());
			} else {
				result[i] = ClassIterator.createString(params[i].getType(),values[i]);
			}
		}

		return result;
	}
}
