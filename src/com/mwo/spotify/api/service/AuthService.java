package com.mwo.spotify.api.service;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.mwo.spotify.utils.Storage;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.models.AuthorizationCodeCredentials;
import com.wrapper.spotify.models.RefreshAccessTokenCredentials;
import com.wrapper.spotify.models.User;

public class AuthService {

	@Autowired
	Storage storage;

	Api api;
	User user;

	boolean tokensReceived = false;

	public Api getApi() {
		return api;
	}

	@PostConstruct
	public void init() {
		api = Api.builder().clientId(storage.getClientId())
				.clientSecret(storage.getClientSecret())
				.redirectURI(storage.getRedirectUri()).build();
	}

	public void getCode() {
		if (storage.getCode() == null) {
			final List<String> scopes = Arrays.asList("user-read-private",
					"user-read-email", "playlist-modify-private",
					"playlist-modify-public", "playlist-read-private");

			String authorizeURL = api.createAuthorizeURL(scopes,
					storage.getState());

			System.out.println(authorizeURL);

			if (Desktop.isDesktopSupported()) {
				try {
					Desktop.getDesktop().browse(new URI(authorizeURL));
				} catch (IOException e) {
					e.printStackTrace();
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void getToken(){
		if (storage.getCode() != null && !tokensReceived) {
			try {
				final AuthorizationCodeCredentials authorizationCodeCredentials = api
						.authorizationCodeGrant(storage.getCode()).build().get();

				System.out.println("Code used! Token expires in:"
						+ authorizationCodeCredentials.getExpiresIn());

				api.setAccessToken(authorizationCodeCredentials.getAccessToken());
				api.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

				final User currentUser = api.getMe().build().get();

				/* Use the information about the user */
				System.out.println("URI to currently logged in user is: "
						+ currentUser.getUri());
				System.out.println("The currently logged in user comes from: "
						+ currentUser.getCountry());
				System.out.println("You can reach this user at: "
						+ currentUser.getEmail());

				user = currentUser;

				this.tokensReceived = true;
			} catch (IOException | WebApiException e) {
				e.printStackTrace();
			}
		}
	}

	public void refreshToken() {
		if (tokensReceived) {
			try {
				RefreshAccessTokenCredentials refreshAccessTokenCredentials = api
						.refreshAccessToken().build().get();
				System.out.println("Token expires in:"
						+ refreshAccessTokenCredentials.getExpiresIn());

				api.setAccessToken(refreshAccessTokenCredentials.getAccessToken());
			} catch (IOException | WebApiException e) {
				e.printStackTrace();
			}
		}
	}

}
