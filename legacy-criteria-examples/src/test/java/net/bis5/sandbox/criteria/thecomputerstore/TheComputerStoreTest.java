package net.bis5.sandbox.criteria.thecomputerstore;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import net.bis5.sandbox.criteria.thecomputerstore.entity.Manufacturer_;
import net.bis5.sandbox.criteria.thecomputerstore.entity.Product;
import net.bis5.sandbox.criteria.thecomputerstore.entity.Product_;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TheComputerStoreTest {

    private TheComputerStore sut;
    private Session session;

    @BeforeAll
    void initialize() {
        sut = new TheComputerStore();
    }

    @BeforeEach
    void setup() {
        session = sut.entityManagerFactory.createEntityManager().unwrap(Session.class);
    }

    @AfterEach
    void tearDown() {
        session.close();
    }

    @AfterAll
    void destroy() {
        sut.entityManagerFactory.close();
    }

    @Test
    @DisplayName("Select the names of all the products in the store")
    void selectTheNamesOfAllTheProductsInTheStore() {
        // SELECT name FROM products;

        Criteria criteria = session.createCriteria(Product.class);

        criteria.setProjection(Projections.property(Product_.NAME));

        @SuppressWarnings("unchecked")
        List<String> names = criteria.list();

        assertThat(names, containsInAnyOrder("Hard drive", "Memory", "ZIP drive", "Floppy disk", "Monitor", "DVD drive",
                "CD drive", "Printer", "Toner cartridge", "DVD burner"));
    }

    @DisplayName("Select all the products with a price between $60 and $120. With AND")
    @Test
    void selectAllTheProductsWithAPriceBetween60And120_WithAnd() {
        // SELECT * FROM Products WHERE Price >= 60 AND Price <= 120;

        Criteria criteria = session.createCriteria(Product.class);

        criteria.add(Restrictions.and(Restrictions.ge(Product_.PRICE, BigDecimal.valueOf(60)),
                Restrictions.le(Product_.PRICE, BigDecimal.valueOf(120))));

        @SuppressWarnings("unchecked")
        List<Product> products = criteria.list();

        assertThat(products.stream().map(Product::getName).toList(),
                containsInAnyOrder("Memory", "CD drive", "Toner cartridge"));
    }

    @DisplayName("Select all the products with a price between $60 and $120. With BETWEEN")
    @Test
    void selectAllTheProductsWithAPriceBetween60And120_WithBetween() {
        // SELECT * FROM Products WHERE Price BETWEEN 60 AND 120;

        Criteria criteria = session.createCriteria(Product.class);

        criteria.add(Restrictions.between(Product_.PRICE, BigDecimal.valueOf(60), BigDecimal.valueOf(120)));

        @SuppressWarnings("unchecked")
        List<Product> products = criteria.list();

        assertThat(products.stream().map(Product::getName).toList(),
                containsInAnyOrder("Memory", "CD drive", "Toner cartridge"));
    }

    @DisplayName("Compute the number of products with a price larger than or equal to $180. ")
    @Test
    void computeTheNumberOfProductsWithAPriceLargerThanOrEqualTo180() {
        // SELECT COUNT(*) FROM Products WHERE Price >= 180;

        Criteria criteria = session.createCriteria(Product.class);

        criteria.setProjection(Projections.count(Product_.CODE));
        criteria.add(Restrictions.ge(Product_.PRICE, BigDecimal.valueOf(180)));

        Long count = (Long) criteria.uniqueResult();

        assertEquals(5L, count);
    }

    @DisplayName("Select the average price of each manufacturer's products, showing the manufacturer's name. ")
    @Test
    void selectTheAveragePriceOfEachManufacturersProductsShowingTheManufacturersName_ObjectArray() {
        // SELECT AVG(Price) Manufacturers.Name FROM Products INNER JOIN Manufacturers
        // ON Products.manufacturer = Manufacturers.code GROUP BY Manufacturers.Name;

        Criteria criteria = session.createCriteria(Product.class);
        criteria.createAlias(Product_.MANUFACTURER, "manufacturer");

        criteria.setProjection(Projections.projectionList().add(Projections.avg(Product_.PRICE))
                .add(Projections.groupProperty("manufacturer." + Manufacturer_.NAME)));

        @SuppressWarnings("unchecked")
        List<Object[]> results = criteria.list();

        assertEquals(6, results.size());
        assertEquals(6, results.stream().map(row -> row[/* Name */1]).distinct().count());
    }

    @DisplayName("Select the average price of each manufacturer's products, showing the manufacturer's name. ")
    @Test
    @Disabled
    void selectTheAveragePriceOfEachManufacturersProductsShowingTheManufacturersName_Tuple() {
        // SELECT AVG(Price) Manufacturers.Name FROM Products INNER JOIN Manufacturers
        // ON Products.manufacturer = Manufacturers.code GROUP BY Manufacturers.Name;

        // Tupleに相当する機能はない
    }

    @DisplayName("Select the average price of each manufacturer's products, showing the manufacturer's name. ")
    @Test
    void selectTheAveragePriceOfEachManufacturersProductsShowingTheManufacturersName_Record() {
        // SELECT AVG(Price) Manufacturers.Name FROM Products INNER JOIN Manufacturers
        // ON Products.manufacturer = Manufacturers.code GROUP BY Manufacturers.Name;

        // recordだと"no appropriate constructor in class"でこけた

        Criteria criteria = session.createCriteria(Product.class);
        criteria.createAlias(Product_.MANUFACTURER, "manufacturer");

        criteria.setProjection(Projections.projectionList().add(Projections.avg(Product_.PRICE).as("price"))
                .add(Projections.groupProperty("manufacturer." + Manufacturer_.NAME).as("manufacturerName")));
        criteria.setResultTransformer(Transformers.aliasToBean(AveragePrice.class));

        @SuppressWarnings("unchecked")
        List<AveragePrice> results = criteria.list();

        assertEquals(6, results.size());
        assertEquals(6, results.stream().map(AveragePrice::getManufacturerName).distinct().count());
    }

    @DisplayName("Select the name of each manufacturer along with the name and price of its most expensive product.")
    @Test
    void selectTheNameOfEachManufacturerAlongWithTheNameAndPriceOfItsMostExpensiveProduct() {
        /*
         * SELECT A.Name, A.Price, F.Name
         * FROM Products A INNER JOIN Manufacturers F
         * ON A.Manufacturer = F.Code
         * AND A.Price =
         * (
         * SELECT MAX(A.Price)
         * FROM Products A
         * WHERE A.Manufacturer = F.Code
         * );
         */

        Criteria criteria = session.createCriteria(Product.class);

        DetachedCriteria subQuery = DetachedCriteria.forClass(Product.class, "sub");
        subQuery.setProjection(Projections.max("sub." + Product_.PRICE));
        subQuery.add(Restrictions.eqProperty("sub." + Product_.MANUFACTURER, "this." + Product_.MANUFACTURER));

        criteria.createAlias(Product_.MANUFACTURER, "manufacturer", JoinType.INNER_JOIN,
                Subqueries.propertyEq("this." + Product_.PRICE, subQuery));

        criteria.setProjection(
                Projections.projectionList()
                        .add(Projections.property("manufacturer." + Manufacturer_.NAME).as("manufacturerName"))
                        .add(Projections.property(Product_.NAME).as("productName"))
                        .add(Projections.property(Product_.PRICE).as("price")));
        criteria.setResultTransformer(Transformers.aliasToBean(ManufacturerProduct.class));

        @SuppressWarnings("unchecked")
        List<ManufacturerProduct> results = criteria.list();

        assertEquals(7, results.size());
    }
}
