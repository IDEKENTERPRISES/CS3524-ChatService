package shared.responses;

import shared.User;

public class OKResponse extends MessageResponse {
	public OKResponse(User source, String messageBody) {
		super(source, messageBody);
	}

	// @Override
	// public void execute() {
	// 	System.out.println("Success: " + this.messageBody);
	// }
}
