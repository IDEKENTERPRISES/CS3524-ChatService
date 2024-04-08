package shared.requests;
import java.io.Serializable;
import java.util.regex.Pattern;

import server.ChatServerHandler;
import server.ConnectionPool;
import shared.responses.ErrorResponse;
import shared.responses.OKResponse;

public abstract class Request implements Serializable {

	/**
	 * Helper function to check if the user making the request is logged in.
	 * @param handler The handler making the request
	 * @return true if the user is logged in, false otherwise
	 */
	public boolean isAuthorized(ChatServerHandler handler) {
		return handler.getUser() != null;
	}

	/**
	 * Helper function to check if the user making the request is logged in.
	 * If the user is not logged in, an error response is sent to the client.
	 * @param handler The handler making the request
	 * @param pool The connection pool
	 * @return true if the user is logged in, false otherwise
	 */
	public boolean checkAuthorizationAndSendError(ChatServerHandler handler, ConnectionPool pool) {
		if (!this.isAuthorized(handler)) {
			this.sendUnauthorizedResponse(handler, pool);
			return false;
		}
		return true;
	}

	/**
	 * Sends an error response to the client indicating that the user is not logged in.
	 * @param handler The handler making the request
	 * @param pool The connection pool
	 */
	public void sendUnauthorizedResponse(ChatServerHandler handler, ConnectionPool pool) {
		this.sendErrorResponse(handler, pool, "You are not registered, use the REGISTER command.");
	}

	/**
	 * Sends an error response to the client.
	 * @param handler The handler making the request
	 * @param pool The connection pool
	 * @param message The error message to send
	 */
	public void sendErrorResponse(ChatServerHandler handler, ConnectionPool pool, String message) {
		handler.sendResponse(new ErrorResponse(pool.SERVER, message));
	}

	/**
	 * Sends an OK response to the client.
	 * @param handler The handler making the request
	 * @param pool The connection pool
	 * @param message The message to send
	 */
	public void sendOKResponse(ChatServerHandler handler, ConnectionPool pool, String message) {
		handler.sendResponse(new OKResponse(pool.SERVER, message));
	}

	/**
	 * Executes the request, processing the request and likely sending a response to the client.
	 * @param handler The handler making the request
	 * @param pool The connection pool
	 */
	public abstract void execute(ChatServerHandler handler, ConnectionPool pool);

	/**
	 * Returns the pattern that user input must match to be parsed as this request type.
	 * @return The Regex pattern
	 */
	public abstract Pattern getPattern();
}
