package hammer.common;

/**
 * @author hao Function wrapper with two input parameters and one return object
 * @param <T>
 * @param <O>
 */
public interface T2Functor<T, O1, O2>
{

	/**
	 * Function template
	 * 
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	public T Function(O1 obj1, O2 obj2);
}
