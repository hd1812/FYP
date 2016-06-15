package hammer.common;

public interface T3Functor<T, O1, O2, O3>
{

	/**
	 * Function template
	 * 
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	public T Function(O1 obj1, O2 obj2, O3 obj3);
}
