package com.github.terma.gigaspacewebconsole.server;

import com.github.terma.gigaspacewebconsole.core.config.ConfigFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class Runner {

    private Server server;

    public void start() {
        // set config
        System.setProperty(ConfigFactory.CONFIG_PATH_SYSTEM_PROPERTY, "classpath:/config.json");

        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setContextPath("/driver-web-console");
        webAppContext.setWar("target/gigaspace-web-console-web-0.0.28-SNAPSHOT.war");

        server = new Server(8080);
        server.setHandler(webAppContext);
        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
