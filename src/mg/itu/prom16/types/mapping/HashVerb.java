package mg.itu.prom16.types.mapping;

import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Method;

public class HashVerb extends HashMap<String, Method> {
	@Override
	public Method put(String key, Method value) throws IllegalArgumentException {
		expectError(key, value);
		return super.put(key, value);
	}

	public boolean isValableKey(String key) {
		switch (key) {
			case "GET":
			case "POST":
				return true;
			default:
				return false;
		}
	}

	public Class<?> getDeclaringClass(String key) {
		return get(key).getDeclaringClass();
	}

	public void expectError(String key, Method value) throws IllegalArgumentException {
		if (!isValableKey(key)) {
			throw new IllegalArgumentException("Verb '" + key + "'' not allowed . only 'GET','POST' for now");
		} else if (containsKey(key)) {
			throw new IllegalArgumentException("Verb '" + key + " already exists for path" + value);
		}
	}

	@Override
	public void putAll(Map<? extends String, ? extends Method> m) {
		for (String key : m.keySet()) {
			put(key, m.get(key));
		}
	}
}
