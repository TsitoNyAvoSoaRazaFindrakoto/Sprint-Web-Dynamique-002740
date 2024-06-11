package mg.itu.prom16.types;

public class Mapping {
    String caller;
    String urlmethod;

    public String caller(String c) {
        if (c != null && !c.isBlank()) {
            caller = c;
            return "";
        }
        return caller;
    }

    public String urlmethod(String method) {
        if (method != null && !method.isBlank()) {
            urlmethod = method;
            return "";
        }
        return urlmethod;
    }

    public Mapping(String caller, String urlmethod) {
        caller(caller);
        urlmethod(urlmethod);
    }

    Mapping(){}

}
