package shared;

import java.io.Serializable;
import java.util.UUID;

import server.ConnectionPool;
import shared.responses.Response;

public class User implements Serializable{
	private String username;
	private UUID id;

	/**
	 * Constructs a new User object with the specified username.
	 *
	 * @param username the username of the user
	 */
	public User(String username) {
		this.username = username;
		this.id = UUID.randomUUID();
	}

	/**
	 * Returns the username of the user.
	 *
	 * @return the username of the user as a String
	 */
	public String getUserName() {
		return this.username;
	}

	/**
	 * Returns the ID of the user.
	 *
	 * @return the ID of the user as a UUID
	 */
	public UUID getId() {
		return this.id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (obj.getClass() != this.getClass()) {
			return false;
		}

		final User other = (User) obj;
		return this.getId() == other.getId();
	}

	@Override
	public int hashCode() {
		return this.id.hashCode();
	}

	@Override
	public String toString() {
		return this.username;
	}

	/**
	 * Sends a response to the client.
	 *
	 * @param pool    the connection pool
	 * @param command the response to send
	 */
	public void sendResponse(ConnectionPool pool, Response command) {
		pool.getHandler(this).sendResponse(command);
	}
}
