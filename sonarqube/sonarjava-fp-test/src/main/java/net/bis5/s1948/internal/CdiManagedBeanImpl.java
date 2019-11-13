package net.bis5.s1948.internal;

import net.bis5.s1948.CdiManagedBean;
import javax.enterprise.context.RequestScoped;

@RequestScoped
public class CdiManagedBeanImpl implements CdiManagedBean {

    @Override
    public void sayHello() {
        System.out.println("Hello World!");
    }
}