package mg.itu.prom16.embed;

import java.util.Enumeration;
import java.util.HashMap;

import jakarta.servlet.http.HttpSession;

public class EmbedSession {
	HashMap<String,Object> sessionHashMap = new HashMap<String,Object>();

	public Object add(String key , Object value) {
		return sessionHashMap.put(key, value);
	}

	public boolean remove(String key , Object value) {
		return sessionHashMap.remove(key,value);
	}

	public Object get(String key){
		return sessionHashMap.get(key);
	}

	public Object remove(String key){
		return sessionHashMap.remove(key);
	}

	public EmbedSession(HttpSession session){
		for (Enumeration<String> e = session.getAttributeNames(); e.hasMoreElements();){
			String key = e.nextElement();
			sessionHashMap.put(key, session.getAttribute(key));
		}
	}

	public void toHttpSession(HttpSession session){
		for (String sessionHashMapKeys : sessionHashMap.keySet()) {
			session.setAttribute(sessionHashMapKeys, get(sessionHashMapKeys));	
		}
	}
}
