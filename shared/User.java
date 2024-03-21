package shared;

import java.io.Serializable;
import java.util.UUID;

import server.ConnectionPool;
import shared.responses.Response;

public class User implements Serializable{
	private String username;
	private UUID id;

	public User(String username) {
		this.username = username;
		this.id = UUID.randomUUID();
	}

	public String getUserName() {
		return this.username;
	}

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

	public void sendResponse(ConnectionPool pool, Response command) {
		pool.getHandler(this).sendResponse(command);
	}
}
