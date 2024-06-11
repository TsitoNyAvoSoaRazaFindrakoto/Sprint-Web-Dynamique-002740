package mg.itu.prom16.Annotations;

import java.lang.annotation.*;
import java.util.HashMap;

import mg.itu.prom16.types.Mapping;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Get {
    public String url();

}
