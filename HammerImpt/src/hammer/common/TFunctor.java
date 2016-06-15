package hammer.common;

/**
 * @author hao Function wrapper. Use functor to pass function as an object
 * @param <T>
 */
public interface TFunctor<T>
{
	/**
	 * Function template
	 */
	public T Function();
}