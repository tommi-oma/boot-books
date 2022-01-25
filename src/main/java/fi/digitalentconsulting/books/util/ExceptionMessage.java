package fi.digitalentconsulting.books.util;

public class ExceptionMessage {
	private String message;
	private String description;
	
	public ExceptionMessage() {
	}

	public ExceptionMessage(String message, String description) {
		this.message = message;
		this.description = description;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
