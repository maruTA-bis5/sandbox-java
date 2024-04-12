package net.bis5.sandbox.criteria.thecomputerstore;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Hello world!
 *
 */
public class TheComputerStore {

    final EntityManagerFactory entityManagerFactory //
            = Persistence.createEntityManagerFactory("TheComputerStore");

}
