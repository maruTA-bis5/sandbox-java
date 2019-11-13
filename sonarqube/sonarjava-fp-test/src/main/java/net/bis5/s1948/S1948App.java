package net.bis5.s1948;

import javax.inject.Inject;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;

@SessionScoped
public class S1948App {

    /** Field injection */
    @Inject CdiManagedBean field1;

    /** Constructor injection */
    private final CdiManagedBean field2;

    /** Setter injection */
    private CdiManagedBean field3;
    
    @Inject
    public S1948App(CdiManagedBean bean) {
        this.field2 = bean;
    }

    @Inject
    public void setField3(CdiManagedBean bean) {
        this.field3 = bean;
    }
    public CdiManagedBean getField3() {
        return field3;
    }

    @PostConstruct
    public void initialize() {
        field1.sayHello();
        field2.sayHello();
        field3.sayHello();
    }
}
