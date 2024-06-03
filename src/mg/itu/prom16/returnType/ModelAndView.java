package mg.itu.prom16.returnType;

public class ModelAndView {
    String key;
    Object value;

    public String key(String k) {
        if (k == null) {
            return key;
        }
        this.key = k;
        return null;
    }

    public Object value(Object out){
        if (out == null) {
            return value;
        }
        this.value = out;
        return null;
    }

    public ModelAndView(String k , Object out){
        key(k);
        value(out);
    }

    public ModelAndView(){}
}
