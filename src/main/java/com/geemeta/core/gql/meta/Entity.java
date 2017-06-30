package com.geemeta.core.gql.meta;

import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by hongxq on 2016/5/5.
 * 如果注解的name为空，则取类名(clazz.getSimpleName())
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
@Component
public @interface Entity {
    /**
     * (Optional) The name of the entity.
     * <p/>
     * Defaults to the class name.
     */
    String name() default "";


    /**
     * (Optional) The name of the table.
     * <p/>
     * Defaults to the entity name.
     */
    String table() default "";

    /**
     * (Optional) The catalog of the table.
     * <p/>
     * Defaults to the default catalog.
     */
    String catalog() default "";

    /**
     * (Optional) The schema of the table.
     * <p/>
     * Defaults to the default schema for user.
     */
    String schema() default "";


//    /**
//     * (Optional) Unique constraints that are to be placed on
//     * the table. These are only used if table generation is in
//     * effect. These constraints apply in addition to any constraints
//     * specified by the <code>Column</code> and <code>JoinColumn</code>
//     * annotations and constraints entailed by primary key mappings.
//     * <p/>
//     * Defaults to no additional constraints.
//     */
//    UniqueConstraint[] uniqueConstraints() default { };
//
//    /**
//     * (Optional) Indexes for the table. These are only used if table generation is in effect.  Defaults to no
//     * additional indexes.
//     *
//     * @return The indexes
//     */
//    Index[] indexes() default {};
}