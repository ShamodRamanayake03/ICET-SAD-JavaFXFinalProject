package util;

import entity.EmployeeEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {
    private static SessionFactory sessionFactory = createSession();

    private static SessionFactory createSession() {
        StandardServiceRegistry registry = null;
        try {
            // Create the registry
            registry = new StandardServiceRegistryBuilder()
                    .configure("hibernate.cfg.xml")
                    .build();

            // Create MetadataSources
            Metadata metadata = new MetadataSources(registry)
                    .addAnnotatedClass(EmployeeEntity.class)
                    .getMetadataBuilder()
                    .applyImplicitNamingStrategy(ImplicitNamingStrategyJpaCompliantImpl.INSTANCE)
                    .build();

            // Create SessionFactory
            return metadata.getSessionFactoryBuilder().build();
        } catch (Exception e) {
            if (registry != null) {
                StandardServiceRegistryBuilder.destroy(registry);
            }
            e.printStackTrace(); // Log the exception for debugging
            throw new ExceptionInInitializerError("Initial SessionFactory creation failed: " + e);
        }
    }

    public static Session getSession() {
        if (sessionFactory == null) {
            throw new IllegalStateException("SessionFactory has not been created.");
        }
        return sessionFactory.openSession();
    }
}
