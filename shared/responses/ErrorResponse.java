package shared.responses;
import shared.User;

public class ErrorResponse extends MessageResponse {

	public ErrorResponse(User source, String messageBody) {
		super(source, messageBody);
	}
}
