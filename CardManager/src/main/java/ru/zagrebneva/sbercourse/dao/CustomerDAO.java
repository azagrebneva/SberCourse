package ru.zagrebneva.sbercourse.dao;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.zagrebneva.sbercourse.models.Card;
import ru.zagrebneva.sbercourse.models.Customer;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class CustomerDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public CustomerDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    public List<Customer> index() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select c from Customer c", Customer.class)
                .getResultList();
    }

    @Transactional
    public void save(Customer customer) {
        Session session = sessionFactory.getCurrentSession();
        session.save(customer);
    }

    @Transactional(readOnly = true)
    public Customer show(int id) {
        Session session = sessionFactory.getCurrentSession();
        Customer customer = session.get(Customer.class, id);
        List<Card> cards = customer.getCards();
        Hibernate.initialize(cards);
        return customer;
    }

    @Transactional
    public void update(int id, Customer updatedCustomer) {
        Session session = sessionFactory.getCurrentSession();
        Customer customerToBeUpdated = session.get(Customer.class, id);

        customerToBeUpdated.setName(updatedCustomer.getName());
        customerToBeUpdated.setSurname(updatedCustomer.getSurname());
        customerToBeUpdated.setMiddleName(updatedCustomer.getMiddleName());
        customerToBeUpdated.setBirthday(updatedCustomer.getBirthday());
        customerToBeUpdated.setEmail(updatedCustomer.getEmail());
    }

    @Transactional
    public void delete(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(session.get(Customer.class, id));
    }

    @Transactional
    public Customer getCustomer(Customer customer) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "select customer " +
                "from Customer customer " +
                "where customer.name = :name " +
                "and customer.middleName = :middleName " +
                "and customer.surname = :surname ";

        return session.createQuery(hql, Customer.class)
                .setParameter("name", customer.getName())
                .setParameter("middleName", customer.getMiddleName())
                .setParameter("surname", customer.getSurname())
                .getResultList()
                .stream().findFirst().orElse(null);
    }

    // Работа с картами

    @Transactional
    public void saveCard(Card card) {
        Session session = sessionFactory.getCurrentSession();
        session.save(card);
    }

    @Transactional(readOnly = true)
    public Card showCard(int id) {
        Session session = sessionFactory.getCurrentSession();
        Card card = session.get(Card.class, id);
        return card;
    }

    @Transactional
    public void deleteCard(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(session.get(Card.class, id));
    }

    // Выбор всех карт
    @Transactional
    public List<List<?>> getExpiredСards(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        Session session = sessionFactory.getCurrentSession();
        String hql = "select new list(card, customer) " +
                "from Card card " +
                    "inner join card.customer as customer " +
                "where month(card.cardExpiryDate) = :month " +
                    "and year(card.cardExpiryDate) = :year";

        return session.createQuery(hql)
                .setParameter("month", month)
                .setParameter("year", year)
                .list();
    }


}
