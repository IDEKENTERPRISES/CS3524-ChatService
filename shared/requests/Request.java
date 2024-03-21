package shared.requests;
import java.io.Serializable;
import java.util.regex.Pattern;

import server.ChatServerHandler;
import server.ConnectionPool;
import shared.responses.ErrorResponse;
import shared.responses.OKResponse;

public abstract class Request implements Serializable{
    public boolean isAuthorized(ChatServerHandler handler) {
        return handler.getUser() != null;
    }

    public boolean checkAuthorizationAndSendError(ChatServerHandler handler, ConnectionPool pool) {
        if (!this.isAuthorized(handler)) {
            this.sendUnauthorizedResponse(handler, pool);
            return false;
        }
        return true;
    }

    public void sendUnauthorizedResponse(ChatServerHandler handler, ConnectionPool pool) {
        this.sendErrorResponse(handler, pool, "You are not registered, use the REGISTER command.");
    }

    public void sendErrorResponse(ChatServerHandler handler, ConnectionPool pool, String message) {
        handler.sendResponse(new ErrorResponse(pool.SERVER, message));
    }

    public void sendOKResponse(ChatServerHandler handler, ConnectionPool pool, String message) {
        handler.sendResponse(new OKResponse(pool.SERVER, message));
    }

    public static Request fromString(String string) {
        return null;
    }

    public abstract Pattern getPattern();

    public abstract void execute(ChatServerHandler handler, ConnectionPool pool);
}
