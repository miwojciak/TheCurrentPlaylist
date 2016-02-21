package com.mwo.spotify.app;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class RadioApplication {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:applicationContext.xml");


		System.out.println("Press any key to stop the service...");

		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ctx.close();
	}
}
