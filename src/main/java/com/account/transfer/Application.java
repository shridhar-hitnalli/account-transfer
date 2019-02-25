package com.account.transfer;

import com.account.transfer.config.AccountApplicationBinder;
import com.account.transfer.controller.AccountController;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;


/**
 * Created by shridhar on 22/02/19.
 */
public class Application {

    public static void main(String[] args) throws Exception {
        final ResourceConfig resourceConfig = new ResourceConfig()
                .packages("com.account.transfer")
                .register(new AccountApplicationBinder());

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        Server jettyServer = new Server(9999);
        jettyServer.setHandler(context);
        ServletHolder jerseyServlet = new ServletHolder(new ServletContainer(resourceConfig));

        context.addServlet(jerseyServlet, "/api/*");

        jerseyServlet.setInitOrder(0);


        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.classnames",
                AccountController.class.getCanonicalName()
                );

        try {
            jettyServer.start();
            jettyServer.join();
        } finally {
            jettyServer.destroy();
        }
    }



}
