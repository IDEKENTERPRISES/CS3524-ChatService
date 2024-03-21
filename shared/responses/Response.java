package shared.responses;

import java.io.Serializable;

import client.Client;

public abstract class Response implements Serializable{
	public abstract void execute(Client client);
}
