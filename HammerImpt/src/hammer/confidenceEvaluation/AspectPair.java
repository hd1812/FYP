package hammer.confidenceEvaluation;

/**
 * @author hao Small helper class that contains references to the actual value
 *         of an State aspect and its predicted value. May be used in conjuction
 *         with HAMMER::compare() to find the error between the actual value and
 *         the predicted value.
 * @param <T>
 */
public class AspectPair<T>
{

	public final T	actual;
	public final T	predicted;

	/**
	 * Constructor
	 * 
	 * @param Actual
	 * @param Pedicted
	 */
	AspectPair(final Object Actual, final Object Pedicted)
	{
		this.actual = (T) Actual;
		this.predicted = (T) Pedicted;
	}

	/**
	 * Copy constructor
	 * 
	 * @param ap
	 */
	AspectPair(AspectPair<T> ap)
	{
		this(ap.actual, ap.predicted);
	}

	/**
	 * Generate comparison result between current and predicted states Compare
	 * method implementations with different input types
	 * 
	 * @param actual
	 * @param predicted
	 * @return
	 */
	public Double compare(final Double actual, final Double predicted)
	{
		return actual - predicted;
	}

	public boolean compare(final boolean actual, final boolean predicted)
	{
		return !(actual == predicted);
	}

	public int compare(final String actual, final String predicted)
	{
		return actual.compareTo(predicted);
	}

}
