package com.haltwinizki.repository.impl;

import com.haltwinizki.products.Product;
import com.haltwinizki.repository.ProductRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class HibernateProductRepository implements ProductRepository {
    private final SessionFactory sessionFactory;

    public HibernateProductRepository() {
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    @Override
    public List<Product> getAllProducts() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Product", Product.class).list();
        }
    }

    @Override
    public Product delete(long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Product product = session.get(Product.class, id);
            session.delete(product);
            transaction.commit();
            return product;
        }
    }

    @Override
    public Product update(Product productToUpdate) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.update(productToUpdate);
            transaction.commit();
            return productToUpdate;
        }
    }

    @Override
    public Product create(Product product) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(product);
            transaction.commit();
            return product;
        }
    }

    @Override
    public Product read(long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Product.class, id);
        }
    }
    @Override
    public List<Product> getDiscardedProducts() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Discarded_Product ", Product.class).list();
        }
    }
}
