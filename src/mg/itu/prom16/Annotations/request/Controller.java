package mg.itu.prom16.Annotations.request;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Controller {
    boolean isController() default true;
}
