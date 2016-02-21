package com.mwo.spotify.rest;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import com.mwo.spotify.utils.Storage;
import org.springframework.beans.factory.annotation.Value;

@SuppressWarnings("serial")
@Path("spotify")
public class SpotifyServlet extends HttpServlet {

	@Value( "${state}" )
	String expectedState;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Storage storage = (Storage) getServletContext().getAttribute("storage");
		response.setContentType("text/html");

		String code = request.getParameter("code");
		String state = request.getParameter("state");

		if (state != null && state.equals(expectedState)) {
			if (storage != null) {
				storage.setCode(code);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().println("Code received.");
				return;
			}
		}
		response.getWriter().println("False state received");

	}

}
