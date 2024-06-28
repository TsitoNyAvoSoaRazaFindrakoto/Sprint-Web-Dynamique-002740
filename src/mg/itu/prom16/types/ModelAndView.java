package mg.itu.prom16.types;

import java.util.HashMap;
import java.util.Set;

public class ModelAndView {
	String page;
	HashMap<String, Object> attributes = new HashMap<String,Object>();

	// Getters and setters:

	// for page

	public void setPage(String page) {
		this.page = page;
	}

	public String getPage() {
		return page;
	}

	// for attibutes
	public void setAttributes(HashMap<String, Object> attributes) {
		this.attributes = attributes;
	}

	public void setAttribute(String key , Object value) {
		getAttributes().put(key, value);
	}

	public void addAttributes(HashMap<String,Object> newatt){
		getAttributes().putAll(newatt);
	}
	

	public void deleteAttribute( String key ){
		getAttributes().remove(key);
	}
	
	public HashMap<String, Object> getAttributes() {
		return attributes;
	}

	public Object getAttribute(String name) {
		return getAttributes().get(name);
	}

	public Set<String> getAttributeNames(){
		return getAttributes().keySet();
	}


	// end of set and get

	// Constructors

	public ModelAndView(String page) {
		setPage(page);
	}

	public ModelAndView() {
	}


	// method for emptiness model and view

	public boolean isEmpty(){
		boolean invalidPage = getPage()==null || getPage().isEmpty(); 
		boolean invalidAttribute = ( getAttributes()==null || getAttributes().isEmpty());
		return  invalidPage && invalidAttribute;
	}
}
