package mg.itu.prom16.types;

import java.lang.reflect.Method;

import mg.itu.prom16.annotations.request.method.Get;
import mg.itu.prom16.annotations.request.method.Post;

public class Mapping {
	String caller;
	Method urlmethod;
	String verb;

	public String caller(String c) {
		if (c != null && !c.isBlank()) {
			caller = c;
			return "";
		}
		return caller;
	}

	public Method urlmethod(Method method) {
		if (method != null) {
			urlmethod = method;
			return null;
		}
		return urlmethod;
	}

	public Mapping(String caller, Method urlmethod, String verb) {
		caller(caller);
		urlmethod(urlmethod);
		setVerb(verb);
	}

	public Mapping(String caller, Method urlmethod) {
		caller(caller);
		urlmethod(urlmethod);
		setVerb();
	}

	public Mapping() {
	}

	public String getCaller() {
		return caller;
	}

	public void setCaller(String caller) {
		this.caller = caller;
	}

	public Method getUrlmethod() {
		return urlmethod;
	}

	public void setUrlmethod(Method urlmethod) {
		this.urlmethod = urlmethod;
	}

	public String getVerb() {
		return verb;
	}

	public void setVerb() {
		if (getUrlmethod().isAnnotationPresent(Post.class)) {
			setVerb("POST");
			return;
		}
		setVerb("GET");
	}

	public void setVerb(String verb) {
		this.verb = verb;
	}

}
