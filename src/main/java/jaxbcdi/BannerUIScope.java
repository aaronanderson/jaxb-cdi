package jaxbcdi;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Scope;

@Scope
@Retention(RUNTIME)
@Target(TYPE)
public @interface BannerUIScope {
	boolean singleton() default false;
}
