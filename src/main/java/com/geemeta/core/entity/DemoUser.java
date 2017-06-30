package com.geemeta.core.entity;

import com.geemeta.core.gql.meta.Col;
import com.geemeta.core.gql.meta.Entity;

/**
 * @author itechgee@126.com
 * @date 2017/5/27.
 */
@Entity(name="demo_user",table = "demo_user")
public class DemoUser extends BaseEntity {

    private String name;
    private int age;

    @Col(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Col(name = "age")
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
