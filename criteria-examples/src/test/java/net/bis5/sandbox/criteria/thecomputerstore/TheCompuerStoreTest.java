package net.bis5.sandbox.criteria.thecomputerstore;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import net.bis5.sandbox.criteria.thecomputerstore.entity.Manufacturer;
import net.bis5.sandbox.criteria.thecomputerstore.entity.Manufacturer_;
import net.bis5.sandbox.criteria.thecomputerstore.entity.Product;
import net.bis5.sandbox.criteria.thecomputerstore.entity.Product_;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TheComputerStoreTest {

    private TheComputerStore sut;
    private EntityManager entityManager;

    @BeforeAll
    void initialize() {
        sut = new TheComputerStore();
    }

    @BeforeEach
    void setup() {
        entityManager = sut.entityManagerFactory.createEntityManager();
    }

    @AfterEach
    void tearDown() {
        entityManager.close();
    }

    @AfterAll
    void destroy() {
        sut.entityManagerFactory.close();
    }

    @Test
    @DisplayName("Select the names of all the products in the store")
    void selectTheNamesOfAllTheProductsInTheStore() {
        // SELECT name FROM products;

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<String> query = builder.createQuery(String.class);
        Root<Product> root = query.from(Product.class);
        query.select(root.get(Product_.name));

        List<String> names = entityManager.createQuery(query).getResultList();

        assertThat(names, containsInAnyOrder("Hard drive", "Memory", "ZIP drive", "Floppy disk", "Monitor", "DVD drive",
                "CD drive", "Printer", "Toner cartridge", "DVD burner"));
    }

    @DisplayName("Select all the products with a price between $60 and $120. With AND")
    @Test
    void selectAllTheProductsWithAPriceBetween60And120_WithAnd() {
        // SELECT * FROM Products WHERE Price >= 60 AND Price <= 120;

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Product> query = builder.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);
        query.select(root);
        query.where(builder.and(builder.greaterThanOrEqualTo(root.get(Product_.price), BigDecimal.valueOf(60)),
                builder.lessThanOrEqualTo(root.get(Product_.price), BigDecimal.valueOf(120))));

        List<Product> products = entityManager.createQuery(query).getResultList();
        assertThat(products.stream().map(Product::getName).toList(),
                containsInAnyOrder("Memory", "CD drive", "Toner cartridge"));
    }

    @DisplayName("Select all the products with a price between $60 and $120. With BETWEEN")
    @Test
    void selectAllTheProductsWithAPriceBetween60And120_WithBetween() {
        // SELECT * FROM Products WHERE Price BETWEEN 60 AND 120;

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Product> query = builder.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);
        query.select(root);
        query.where(builder.between(root.get(Product_.price), BigDecimal.valueOf(60), BigDecimal.valueOf(120)));

        List<Product> products = entityManager.createQuery(query).getResultList();
        assertThat(products.stream().map(Product::getName).toList(),
                containsInAnyOrder("Memory", "CD drive", "Toner cartridge"));
    }

    @DisplayName("Compute the number of products with a price larger than or equal to $180. ")
    @Test
    void computeTheNumberOfProductsWithAPriceLargerThanOrEqualTo180() {
        // SELECT COUNT(*) FROM Products WHERE Price >= 180;

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Product> root = query.from(Product.class);
        query.select(builder.count(root));
        query.where(builder.greaterThanOrEqualTo(root.get(Product_.price), BigDecimal.valueOf(180)));

        Long count = entityManager.createQuery(query).getSingleResult();

        assertEquals(5L, count);
    }

    @DisplayName("Select the average price of each manufacturer's products, showing the manufacturer's name. ")
    @Test
    void selectTheAveragePriceOfEachManufacturersProductsShowingTheManufacturersName_ObjectArray() {
        // SELECT AVG(Price) Manufacturers.Name FROM Products INNER JOIN Manufacturers
        // ON Products.manufacturer = Manufacturers.code GROUP BY Manufacturers.Name;

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
        Root<Product> root = query.from(Product.class);
        Join<Product, Manufacturer> manufacturerJoin = root.join(Product_.manufacturer);

        query.multiselect(builder.avg(root.get(Product_.price)), manufacturerJoin.get(Manufacturer_.name));
        query.groupBy(manufacturerJoin.get(Manufacturer_.name));

        List<Object[]> results = entityManager.createQuery(query).getResultList();

        assertEquals(6, results.size());
        assertEquals(6, results.stream().map(row -> row[/* Name */1]).distinct().count());
    }

    @DisplayName("Select the average price of each manufacturer's products, showing the manufacturer's name. ")
    @Test
    void selectTheAveragePriceOfEachManufacturersProductsShowingTheManufacturersName_Tuple() {
        // SELECT AVG(Price) Manufacturers.Name FROM Products INNER JOIN Manufacturers
        // ON Products.manufacturer = Manufacturers.code GROUP BY Manufacturers.Name;

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
        Root<Product> root = query.from(Product.class);
        Join<Product, Manufacturer> manufacturerJoin = root.join(Product_.manufacturer);

        Expression<Double> priceExpr = builder.avg(root.get(Product_.price));
        Path<String> manufacturerPath = manufacturerJoin.get(Manufacturer_.name);

        query.multiselect(priceExpr, manufacturerPath);
        query.groupBy(manufacturerJoin.get(Manufacturer_.name));

        List<Tuple> results = entityManager.createQuery(query).getResultList();

        assertEquals(6, results.size());
        assertEquals(6, results.stream().map(row -> row.<String>get(manufacturerPath)).distinct().count());
    }

    @DisplayName("Select the average price of each manufacturer's products, showing the manufacturer's name. ")
    @Test
    void selectTheAveragePriceOfEachManufacturersProductsShowingTheManufacturersName_Record() {
        // SELECT AVG(Price) Manufacturers.Name FROM Products INNER JOIN Manufacturers
        // ON Products.manufacturer = Manufacturers.code GROUP BY Manufacturers.Name;

        record AveragePrice(Double price, String manufacturerName) {
        }
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<AveragePrice> query = builder.createQuery(AveragePrice.class);
        Root<Product> root = query.from(Product.class);
        Join<Product, Manufacturer> manufacturerJoin = root.join(Product_.manufacturer);

        query.multiselect(builder.avg(root.get(Product_.price)), manufacturerJoin.get(Manufacturer_.name));
        query.groupBy(root.get(Product_.manufacturer));

        List<AveragePrice> results = entityManager.createQuery(query).getResultList();

        assertEquals(6, results.size());
        assertEquals(6, results.stream().map(AveragePrice::manufacturerName).distinct().count());
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

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        record ManufacturerProduct(String manufacturerName, String productName, BigDecimal price) {
        }
        CriteriaQuery<ManufacturerProduct> query = builder.createQuery(ManufacturerProduct.class);
        Root<Product> root = query.from(Product.class);
        Join<Product, Manufacturer> manufacturerJoin = root.join(Product_.manufacturer);

        Subquery<BigDecimal> subQuery = query.subquery(BigDecimal.class);
        Root<Product> subRoot = subQuery.from(Product.class);
        subQuery.select(builder.max(subRoot.get(Product_.price)));
        subQuery.where(builder.equal(subRoot.get(Product_.manufacturer),
                manufacturerJoin));
        manufacturerJoin.on(builder.equal(root.get(Product_.price), subQuery));

        query.multiselect(manufacturerJoin.get(Manufacturer_.name), root.get(Product_.name), root.get(Product_.price));
        List<ManufacturerProduct> results = entityManager.createQuery(query).getResultList();

        assertEquals(7, results.size());
    }
}
