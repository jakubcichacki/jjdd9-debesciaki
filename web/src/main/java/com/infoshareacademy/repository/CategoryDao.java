package com.infoshareacademy.repository;

import com.infoshareacademy.domain.entity.Category;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Stateless
public class CategoryDao {
    @PersistenceContext
    EntityManager entityManager;

    public void persistEntityList(List<Category> list) throws IOException {
        for (Category o : list) {
            entityManager.persist(o);
        }
    }

    public Optional<Category> findById(long id) {
        try {
            return Optional.ofNullable(entityManager.find(Category.class, id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Category> findByApiId(long id) {
        Query query = entityManager.createQuery("SELECT c FROM Category c WHERE c.apiId=:apiID");
        query.setParameter("apiID", id);
        try {
            return Optional.ofNullable((Category) query.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}