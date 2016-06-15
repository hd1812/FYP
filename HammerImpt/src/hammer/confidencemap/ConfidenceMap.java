package hammer.confidencemap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.rits.cloning.Cloner;

import hammer.dictionary.impl.Dictionary;

/**
 * @author hao Dictionary-like class to store confidences of inverse-forward
 *         models.
 */
public class ConfidenceMap
{

	/**
	 * Public implementation of confidence map
	 */
	public Impl pimpl_;

	public Cloner cloner = new Cloner();

	/**
	 * Constructor
	 */
	public ConfidenceMap()
	{
		pimpl_ = new Impl();
	}

	public ConfidenceMap(Impl pimpl)
	{
		pimpl_ = pimpl;
	}

	/**
	 * Copy constructor
	 * 
	 * @param obj
	 */
	public ConfidenceMap(ConfidenceMap obj)
	{
		this(obj.pimpl_);
	}

	/**
	 * @author hao Definition of implementation of confidencemap
	 */
	public class Impl
	{
		public Dictionary<Double> dictionary_;
		public Impl()
		{
			dictionary_ = new Dictionary<Double>();
		}
	}

	/**
	 * It creates a new instance of confidence map
	 * 
	 * @return
	 */
	public static ConfidenceMap make()
	{
		return new ConfidenceMap();
	}

	/**
	 * It creates a copy of confidence map
	 * 
	 * @param obj
	 * @return
	 * @throws CloneNotSupportedException
	 */
	public ConfidenceMap clone() throws CloneNotSupportedException
	{
		return cloner.deepClone(this);
	}

	/**
	 * Factory method to create a new ConfidenceMap containing a subset of the
	 * inverse-forward pairs in another instance
	 * 
	 * @param obj
	 * @param keys
	 * @return
	 */
	public static ConfidenceMap subset(final ConfidenceMap obj, final ArrayList<String> keys)
	{
		ConfidenceMap newState = ConfidenceMap.make();
		obj.pimpl_.dictionary_.subset(keys, newState.pimpl_.dictionary_);
		return newState;
	}

	/**
	 * Insert an element into ConfidenceMap
	 * 
	 * @param key
	 * @param value
	 */
	public void put(final String key, Double value)
	{
		pimpl_.dictionary_.put(key, value);
	}

	/**
	 * Get a confidence value
	 * 
	 * @param key
	 * @return
	 */
	public Double get(final String key)
	{
		return pimpl_.dictionary_.get(key);
	}

	/**
	 * Function to check whether ConfidenceMap contains the confidence of an
	 * inverse-forward pair or not
	 * 
	 * @param key
	 * @return
	 */
	public boolean contains(final String key)
	{
		return pimpl_.dictionary_.contains(key);
	}

	/**
	 * Function to determine whether ConfidenceMap is empty.
	 * 
	 * @return
	 */
	public boolean empty()
	{
		return pimpl_.dictionary_.empty();
	}

	/**
	 * Get KIterator for start of iteration.
	 * 
	 * @return
	 */
	public Map.Entry<String, Double> begin()
	{
		return pimpl_.dictionary_.data.entrySet().iterator().next();
	}

	/**
	 * Get the start of the given map
	 * 
	 * @param map
	 * @return
	 */
	public Map.Entry<String, Double> range_begin(final ConfidenceMap map)
	{
		return map.begin();
	}

	/**
	 * Get the last entry
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map.Entry<String, Double> end()
	{
		Iterator<Entry<String, Double>> itr = pimpl_.dictionary_.data.entrySet().iterator();
		while (itr.hasNext())
		{
			itr.next();
		}
		return (Entry<String, Double>) itr;
	}

	/**
	 * Get the last entry of a given map
	 * 
	 * @param map
	 * @return
	 */
	public Map.Entry<String, Double> range_end(final ConfidenceMap map)
	{
		return map.end();
	}

}
