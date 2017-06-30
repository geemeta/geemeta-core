package com.geemeta.core.gql.meta;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by hongxueqian on 14-3-23.
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface Col {
    /**
     * (Optional) The name of the column. Defaults to
     * the property or field name.
     */
    String name();

    /**
     * (Optional) Whether the column is a unique key.  This is a
     * shortcut for the <code>UniqueConstraint</code> annotation at the table
     * level and is useful for when the unique key constraint
     * corresponds to only a single column. This constraint applies
     * in addition to any constraint entailed by primary key mapping and
     * to constraints specified at the table level.
     */
    boolean unique() default false;

    /**
     * (Optional) Whether the database column is nullable.
     */
    boolean nullable() default true;

    String dataType() default "";

    int charMaxlength() default 64;

    int numericPrecision() default 20;

    int numericScale() default 0;

    int datetimePrecision() default 0;

}
