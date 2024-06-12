package mg.itu.prom16.types;

import java.lang.reflect.Method;

public class Mapping {
    String caller;
    Method urlmethod;

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

    public Mapping(String caller, Method urlmethod) {
        caller(caller);
        urlmethod(urlmethod);
    }

    Mapping(){}

}
