package mg.itu.prom16.types.returnType;

import java.util.HashMap;
import java.util.Set;

public class ModelAndView {
	String method = "GET", view;
	public boolean redirect = false ;
	HashMap<String, Object> attributes = new HashMap<String, Object>();

	public void setView(String page) {
		this.view = page;
	}

	public String getView() {
		return view;
	}

	public ModelAndView(String method, String view) {
		this.method = method;
		this.view = view;
	}

	public void setAttributes(HashMap<String, Object> attributes) {
		this.attributes = attributes;
	}

	public void setAttribute(String key, Object value) {
		getAttributes().put(key, value);
	}

	public void addAttributes(HashMap<String, Object> newatt) {
		getAttributes().putAll(newatt);
	}

	public void deleteAttribute(String key) {
		getAttributes().remove(key);
	}

	public HashMap<String, Object> getAttributes() {
		return attributes;
	}

	public Object getAttribute(String name) {
		return getAttributes().get(name);
	}

	public Set<String> getAttributeNames() {
		return getAttributes().keySet();
	}

	// end of set and get

	// Constructors

	public ModelAndView(String page) {
		setView(page);
	}

	public ModelAndView() {
	}

	// method for emptiness model and view

	public boolean isEmpty() {
		boolean invalidPage = getView() == null || getView().isEmpty();
		boolean invalidAttribute = (getAttributes() == null || getAttributes().isEmpty());
		return invalidPage && invalidAttribute;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

}
