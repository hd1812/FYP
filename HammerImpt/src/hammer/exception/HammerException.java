package hammer.exception;

/**
 * @author hao Simple exception class which inherits from Exception library
 */
@SuppressWarnings("serial")
public class HammerException extends Exception
{

	/**
	 * Error message thrown by exception
	 */
	private String message_;

	/**
	 * Constructor
	 * 
	 * @param message
	 */
	public HammerException(String message)
	{
		this.message_ = message;
	}

	/**
	 * Get a message describing what went wrong
	 * 
	 * @return
	 */
	final String what()
	{
		return message_;
	}
}
