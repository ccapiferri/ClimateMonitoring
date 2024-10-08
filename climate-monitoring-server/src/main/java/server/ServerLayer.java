/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import climatemonitoring.core.Application;
import climatemonitoring.core.Layer;
import climatemonitoring.core.headless.Console;

/**
 * The actual server application
 * 
 * @author ccapiferri
 * @version 1.0-SNAPSHOT
 */
public class ServerLayer extends Layer {

	/**
	 * Code to be executed as the layer gets created
	 */
	public void onAttach() {

		m_url = Console.read("Database URL > ");
		m_username = Console.read("Username > ");
		m_password = Console.read("Password > ");


		try {

			Connection dummy = DriverManager.getConnection(m_url, m_username, m_password);

			if (dummy != null)
				Console.info("Server connection estabilished");
			else
				Application.close();

			m_server = new ServerSocket(25565);
		}

		catch (IOException e) {

			Console.error("Server starting failed");
			Application.close();
		}

		catch (SQLException ex) {

			Console.error("Database connection failed");
			Application.close();
		}
	}

	/**
	 * Code that runs on every iteration of the main loop
	 */
	public void onUpdate() {

		try {

			Socket client = m_server.accept();
			Console.info("New client connected: " + client.getInetAddress());
			new Skeleton(client, new ServerDatabaseImpl(m_url, m_username, m_password));
		}

		catch (IOException ex) {

			Console.error("Client binding failed");
		}
	}

	/**
	 * Code that runs in headless configuration only
	 */
	public void onHeadlessRender() {


	}

	/**
	 * Code that runs in GUI configuration only
	 */
	public void onGUIRender() {


	}

	/**
	 * Code to be executed as the layer gets deleted
	 */
	public void onDetach() {


	}

	private ServerSocket m_server;

	private String m_url;
	private String m_username;
	private String m_password;
}
