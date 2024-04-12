package net.bis5.sandbox.criteria.thecomputerstore;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Hello world!
 *
 */
public class TheComputerStore {

    final EntityManagerFactory entityManagerFactory //
            = Persistence.createEntityManagerFactory("TheComputerStore");

}
