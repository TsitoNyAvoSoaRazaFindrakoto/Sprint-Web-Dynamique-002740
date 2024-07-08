package mg.itu.prom16.embed;

import jakarta.servlet.http.HttpSession;

public class EmbedSession {
	HttpSession session;

	public EmbedSession(){}

	public EmbedSession(HttpSession session){
		setSession(session);
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public Object get(String key) {
		return getSession().getAttribute(key);
	}

	public void add(String key, Object value){
		getSession().setAttribute(key, value);
	}

	public void remove(String key) {
		getSession().removeAttribute(key);
	}

	public void reset(){
		getSession().invalidate();
	}
}
