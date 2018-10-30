package com.aol.spring.demo.models;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import java.util.List;

/**
 * Created by ram on 02/03/16.
 */
@Repository
@Transactional
public class UserDao {

    @Autowired
    private SessionFactory _sessionFactory;

    private Session getSession() {
        return _sessionFactory.getCurrentSession();
    }

    public void save(User user) {
        getSession().save(user);
    }

    public void delete(User user) {
        getSession().delete(user);
    }

    public List getAll() {
        return getSession().createQuery("from User").list();
    }

    public User getByEmail(String email) {
        return (User) getSession().createQuery(
                "from User where email = :email")
                .setParameter("email", email)
                .uniqueResult();
    }

    public User getById(Long id) {
        return (User) getSession().load(User.class, id);
    }

    public void update(User user) {
        getSession().update(user);
    }
}
