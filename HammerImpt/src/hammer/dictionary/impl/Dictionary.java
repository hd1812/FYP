package hammer.dictionary.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.rits.cloning.Cloner;

/**
 * @author hao Create a generic class of dictionary
 * @param <T>
 */
public class Dictionary<T>
{

	/**
	 * Main storage place
	 */
	public Map<String, T> data;

	/**
	 * Deep cloner
	 */
	public Cloner cloner = new Cloner();

	public Dictionary()
	{
		this.data = new HashMap<String, T>();
	}

	/**
	 * It creates a instance of dictionary
	 * 
	 * @return
	 */
	public static <T> Dictionary<T> make()
	{

		return new Dictionary<T>();
	}

	/**
	 * It inserts a pair of data into dictionary
	 * 
	 * @param key
	 * @param value
	 */
	public void put(final String key, T value)
	{
		data.put(key, value);
	}

	public Dictionary<T> clone(){
		return cloner.deepClone(this);
	}
	
	public Map<String, T> CloneData(){
		return cloner.deepClone(data);
	}
	
	/**
	 * Get corresponding value use a valid key
	 * 
	 * @param key
	 * @return
	 */
	public T get(final String key)
	{
		Map<String, T> newData = new HashMap<String, T>(data);
		return newData.get(key);
	}

	/**
	 * Checks whether dictionary contains the key
	 * 
	 * @param key
	 * @return
	 */
	public boolean contains(final String key)
	{
		return data.containsKey(key);
	}

	/**
	 * Checks whether dictionary is empty;
	 * 
	 * @return
	 */
	public boolean empty()
	{
		return data.isEmpty();
	}

	/**
	 * It creates a subset of dictionary to output
	 * 
	 * @param keys
	 * @param output
	 */
	public void subset(final ArrayList<String> keys, Dictionary<T> output)
	{
		ArrayList<String> newKey = new ArrayList<String>(keys);
		Map<String, T> newData = new HashMap<String, T>(data);
		for (String key : newKey)
		{
			output.put(key, newData.get(key));
		}
	}
}
