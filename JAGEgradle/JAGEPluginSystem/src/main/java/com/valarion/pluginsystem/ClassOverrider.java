package com.valarion.pluginsystem;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation that defines that a method is intended to select between two classes
 * the one that should be loaded.
 * @author Rubén Tomás Gracia
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ClassOverrider {

}
