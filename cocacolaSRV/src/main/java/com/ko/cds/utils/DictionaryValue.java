package com.ko.cds.utils;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.sf.oval.configuration.annotation.Constraint;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Constraint(checkWith = DictionaryValueCheck.class)
/***
 * 
 * @author IBM
 * 
 * an annotation that would check a field against some values in a dictionary.
 * If field's value was in the given set, than the validation would succeed
 *
 */
public @interface DictionaryValue {
	/**
     * message to be used for the ConstraintsViolatedException
     *
     * @see ConstraintsViolatedException
     */
    String message() default "constraint.DictionaryValue.violated";
 
    String file();

}
