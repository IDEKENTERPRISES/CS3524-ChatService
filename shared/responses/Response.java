package shared.responses;

import java.io.Serializable;

import client.Client;

public abstract class Response implements Serializable {
	/**
	 * Execute the response.
	 * @param client The client to execute the response on
	 */
	public abstract void execute(Client client);
}
