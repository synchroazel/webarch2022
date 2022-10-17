package it.unitn.padalino.assignment2;

import javax.servlet.ServletContextEvent;

public class ServletContextListener
        implements javax.servlet.ServletContextListener {


    // ON SHUTDOWN // Run this before web application is shutdown
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

        ReaderWriter rw = new ReaderWriter();
        rw.serialWriter(servletContextEvent, "users-db.txt");
    }

    // ON STARTUP // Run this before web application is started
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        ReaderWriter rw = new ReaderWriter();
        rw.serialReader(servletContextEvent, "users-db.txt");

    }
}
