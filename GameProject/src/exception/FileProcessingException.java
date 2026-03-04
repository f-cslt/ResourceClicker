package exception;


public class FileProcessingException extends RuntimeException{
	
	public FileProcessingException(String message, Throwable exception) {
		super(message, exception);
	}
}
