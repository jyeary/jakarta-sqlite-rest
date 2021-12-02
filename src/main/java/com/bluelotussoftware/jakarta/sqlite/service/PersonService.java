/*
 * Copyright 2021 John Yeary <jyeary@bluelotussoftware.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bluelotussoftware.jakarta.sqlite.service;

import com.bluelotussoftware.jakarta.sqlite.model.Person;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import java.util.List;
import lombok.NonNull;

/**
 *
 * @author John Yeary <jyeary@bluelotussoftware.com>
 */
public class PersonService implements EntityService<Person> {

    private EntityManager entityManager;

    public PersonService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private EntityTransaction getTransaction() {
        return entityManager.getTransaction();
    }

    @Override
    public void create(@NonNull Person p) {
        getTransaction().begin();
        entityManager.persist(p);
        getTransaction().commit();
    }

    @Override
    public Person update(@NonNull Person p) {
        getTransaction().begin();
        entityManager.merge(p);
        getTransaction().commit();
        return p;
    }

    @Override
    public void delete(@NonNull Person p) {
        getTransaction().begin();
        entityManager.remove(entityManager.contains(p) ? p : entityManager.merge(p));
        getTransaction().commit();
    }

    @Override
    public Person find(long id) {
        return entityManager.find(Person.class, id);
    }

    @Override
    public List<Person> findAll() {
        Query query = entityManager.createQuery("SELECT p FROM Person p");
        return query.getResultList();
    }

}
