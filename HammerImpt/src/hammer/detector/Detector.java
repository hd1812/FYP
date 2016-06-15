package hammer.detector;

import java.util.Map.Entry;

import hammer.confidencemap.ConfidenceMap;
import hammer.exception.HammerException;

/**
 * @author hao Class to detect behaviours from a series of confidence maps.
 */
public class Detector
{

	/**
	 * Public implementation of detector
	 */
	public Impl pimpl_;

	/**
	 * Constructor
	 * 
	 * @param threshold
	 * @param period,
	 *            number of cycles to wait before make predictions
	 */
	public Detector(Double threshold, int period)
	{
		this.pimpl_ = new Impl(threshold, period);
	}

	/**
	 * @author hao Implementation of detector
	 */
	private class Impl
	{

		Double			threshold;
		int				period_;
		int				t_;
		String			detectedBehaviour_;
		ConfidenceMap	lastConfidenceMap_;

		private Impl(Double threshold, int period)
		{
			this.threshold = threshold;
			this.period_ = period;
			this.t_ = 0;
			this.detectedBehaviour_ = null;
			lastConfidenceMap_ = ConfidenceMap.make();
		}
	}

	/**
	 * It creates an instance of detector
	 * 
	 * @param threshold
	 * @param period
	 * @return
	 */
	public static Detector make(Double threshold, int period)
	{
		return new Detector(threshold, period);
	}

	/**
	 * Main function. Feed confidences in, and if a behaviour is detected (see
	 * class definition) return true. Find the confidence behaviour with the
	 * highest increase in performance
	 * 
	 * @param confidences
	 * @return
	 */
	public boolean detect(final ConfidenceMap confidences)
	{
		if (pimpl_.t_ == pimpl_.period_)
		{
			Double maxConfidenceDelta = (double) 0;
			String highestConfidenceBehaviour = new String();

			for (Entry<String, Double> entry : confidences.pimpl_.dictionary_.data.entrySet())
			{
				final String name = entry.getKey();
				final Double currentConfidence = entry.getValue();
				final Double oldConfidence = (pimpl_.lastConfidenceMap_.contains(name))
						? pimpl_.lastConfidenceMap_.get(name)
						: 0;
				Double delta = currentConfidence - oldConfidence;
				if (delta > maxConfidenceDelta)
				{
					maxConfidenceDelta = delta;
					highestConfidenceBehaviour = name;
				}
			}

			if (pimpl_ != null)
			{
				pimpl_.lastConfidenceMap_ = confidences;
			}

			pimpl_.t_ = 0;

			if (maxConfidenceDelta > pimpl_.threshold)
			{
				pimpl_.detectedBehaviour_ = highestConfidenceBehaviour;
				return true;
			}
		} else
		{
			++pimpl_.t_;
		}
		return false;
	}

	/**
	 * reset detected behaviour
	 */
	public void reset()
	{
		pimpl_.detectedBehaviour_ = null;
	}

	/**
	 * Get the name of detected behaviour
	 * 
	 * @return
	 * @throws HammerException
	 */
	public final String getDetected() throws HammerException
	{
		if (pimpl_.detectedBehaviour_ == null)
			throw new HammerException("No behaviour was detected");
		return pimpl_.detectedBehaviour_;
	}

}
