package hammer.common;

/**
 * @author hao Function wrapper with one input parameter and one returned object
 * @param <T>
 * @param <O>
 */
public interface T1Functor<T, O>
{

	/**
	 * Function template
	 * 
	 * @param obj
	 * @return
	 */
	public T Function(O obj);
}
