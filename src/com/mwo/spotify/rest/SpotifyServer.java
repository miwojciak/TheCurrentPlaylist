package com.mwo.spotify.rest;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.print.attribute.standard.Severity;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.springframework.beans.factory.annotation.Autowired;

import com.mwo.spotify.utils.Storage;

public class SpotifyServer {

	@Autowired
	Storage storage;

	Server server;

	@PostConstruct
	public void init() throws Exception {
		System.out.println("Starting local server");
		server = new Server(8080);

		ServletContextHandler context = new ServletContextHandler();
		context.setContextPath("/");
		context.setAttribute("storage", storage);
		server.setHandler(context);

		context.addServlet(SpotifyServlet.class, "/spotify/*");

		server.start();

		System.out.println("Local server started");
		// server.join();
	}

	@PreDestroy
	public void destroy() throws Exception {
		if (server != null)
			System.out.println("Stopping server");
			server.stop();
	}

}
