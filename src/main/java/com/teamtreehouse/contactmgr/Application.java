package com.teamtreehouse.contactmgr;

import com.teamtreehouse.contactmgr.model.Contact;
import com.teamtreehouse.contactmgr.model.Contact.ContactBuilder;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

/**
 * Created by user on 12.04.2016.
 */
public class Application
{
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory()
    {
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static void main(String[] args)
    {
        Contact contact = new ContactBuilder("Leo", "Cserny").withEmail("somethins@test.com").withPhone(35136518).build();
        int id = save(contact);

//        for (Contact cnt : fetchAllContacts()) {
//            System.out.println(cnt);
//        }
        System.out.printf("%n%nBefore update%n");
        fetchAllContacts().stream().forEach(System.out::println);

        Contact newContact = findContactById(id);
        contact.setLastName("Something");
        updateContact(contact);

        System.out.printf("%n%nAfter update%n");
        fetchAllContacts().stream().forEach(System.out::println);

        Contact delContact = findContactById(4);
        deleteContact(delContact);

        System.out.printf("%n%nAfter delete%n");
        fetchAllContacts().stream().forEach(System.out::println);
    }

    private static Contact findContactById(int id)
    {
        Session session = sessionFactory.openSession();
        Contact contact = session.get(Contact.class, id);
        session.close();

        return contact;
    }

    private static void deleteContact(Contact contact)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(contact);
        session.getTransaction().commit();
        session.close();
    }

    private static void updateContact(Contact contact)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(contact);
        session.getTransaction().commit();
        session.close();
    }

    private static int save(Contact contact)
    {
        // Open a session
        Session session = sessionFactory.openSession();

        // Begin a transaction
        session.beginTransaction();

        // Use session to save Contact
        int id = (int) session.save(contact);

        // Commit transaction
        session.getTransaction().commit();

        // Close session
        session.close();

        return id;
    }

    @SuppressWarnings("unchecked")
    private static List<Contact> fetchAllContacts()
    {
        Session session = sessionFactory.openSession();

        Criteria criteria = session.createCriteria(Contact.class);
        List<Contact> contacts = criteria.list();

        session.close();

        return contacts;
    }
}
