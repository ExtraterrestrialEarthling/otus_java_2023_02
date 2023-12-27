package ru.otus.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.cache.HwCache;
import ru.otus.crm.cache.HwListener;
import ru.otus.crm.cache.MyCache;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DbServiceClientCached;

import java.util.List;

public class DbServiceCacheDemo {
    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";


    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);

        var clientTemplate = new DataTemplateHibernate<>(Client.class);

        HwCache<Long, Client> cache = new MyCache<>(10);
        cache.addListener(new HwListener<Long, Client>() {
            @Override
            public void notify(Long key, Client value, String action) {
                log.info("key:{}, value:{}, action: {}", key, value, action);
            }
        });
        var dbServiceClient = new DbServiceClientCached(transactionManager, clientTemplate, cache);

        Client newClient = new Client("John", new Address("Street1"), List.of(new Phone("+7913111111")));
        Client savedClient = dbServiceClient.saveClient(newClient);

        for (int i = 1; i <= 5; i++) {
            Client cachedClient = dbServiceClient.getClient(savedClient.getId()).orElse(null);
            System.out.println("Iteration " + i + ": " + cachedClient);
        }

        savedClient.setName("John Smith");
        dbServiceClient.saveClient(savedClient);

        for (int i = 1; i <= 5; i++) {
            Client updatedCachedClient = dbServiceClient.getClient(savedClient.getId()).orElse(null);
            System.out.println("Updated Iteration " + i + ": " + updatedCachedClient);
        }
    }
}
