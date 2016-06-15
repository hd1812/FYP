package hammer.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.rits.cloning.Cloner;

import hammer.common.Pair;
import hammer.common.T1Functor;
import hammer.common.T2Functor;
import hammer.common.T3Functor;
import hammer.common.V1Functor;
import hammer.common.VoidFunctor;
import hammer.confidenceEvaluation.StateMap;
import hammer.confidencemap.ConfidenceMap;
import hammer.core.Core.RankedWeaver;
import hammer.dictionary.impl.Dictionary;
import hammer.exception.HammerException;
import hammer.forwardmodel.ForwardModel;
import hammer.forwardmodel.SimpleForwardModel;
import hammer.inversemodel.InverseModel;
import hammer.inversemodel.SimpleInverseModel;
import hammer.signals.Signals;
import hammer.threadweaver.ThreadWeaver;
import hammer.state.*;
public class Core
{

	/**
	 * Public implementation of Core
	 */
	public Impl pimpl_;

	/**
	 * Constructor
	 */
	public Core()
	{
		pimpl_ = new Impl();
	}

	/**
	 * @author hao Class definition of rankedweaver
	 */
	public class RankedWeaver
	{
		public ThreadWeaver	weaver;
		public Integer			rank;
		public RankedWeaver()
		{
		}
		public RankedWeaver(ThreadWeaver threadWeaver, Integer integer)
		{
			this.weaver = threadWeaver;
			this.rank = integer;
		}
	}

	public class RankedWeaverComparator implements Comparator<String>
	{
		Map<String,RankedWeaver> base;
		public RankedWeaverComparator(Map<String,RankedWeaver> map){
			this.base=map;
		}
		@Override
		public int compare(String o1, String o2)
		{
			Integer rank1=base.get(o1).rank;
			Integer rank2=base.get(o2).rank;
			Double conf1=base.get(o1).weaver.getConfidence();
			Double conf2=base.get(o2).weaver.getConfidence();
			
			if (rank1!=rank2)
			{
				//rank comparison
				//System.out.println("cmp1");
				return rank1.compareTo(rank2);
				
			} else if(!conf1.equals(conf2))
			{
				//confidence interval comparison
				//System.out.println("cmp2");
				return conf2.compareTo(conf1);//Note this comparison order is a bit strange
			}else{
				//string comparison
				//System.out.println("cmp3");
				return o1.compareTo(o2);
			}
		}
	}
	
	/**
	 * @author hao Define a pair of states
	 */
	public class StatePair extends Pair<State, State>
	{
		public StatePair(State name, State rank)
		{
			super(name, rank);
		}
	}

	/**
	 * Create a new instance of core
	 * 
	 * @return
	 */
	public static Core make()
	{
		return new Core();
	}

	/**
	 * Add a state to state queue
	 * 
	 * @param current
	 * @param target
	 */
	public void addState(final State current, final State target)
	{
		try
		{
			StatePair toAdd = new StatePair(current, target);
			pimpl_.ReadyToRunMutex_.lock();
			pimpl_.stateQueue_.add(toAdd); // add state pair to state queue
			pimpl_.StateUpdated_ = false;
			pimpl_.ReadyToRunCondition_.signal(); // latest current and
													// predicted state pair
		} finally
		{
			pimpl_.ReadyToRunMutex_.unlock();
		}

	}

	private Cloner	cloner	= new Cloner();
	
	/**
	 * Register models
	 * 
	 * @param inverseModel
	 * @param forwardModel
	 * @throws HammerException
	 */
	public void registerInverseForwardPair(final InverseModel inverseModel, final ForwardModel forwardModel,final Core core,final TreeMap<String,RankedWeaver> sortedweavers)
			throws HammerException
	{
		// make a start a new thread with specified model pairs
		// Then register the threadweaver under weavers
		
		pimpl_.registerThreadWeaver(ThreadWeaver.make(cloner.deepClone(inverseModel), cloner.deepClone(forwardModel),core,sortedweavers));
	}
	public void registerInverseForwardPair(final String name, final ArrayList<String> subscriptions,
			final T3Functor<Signals, State, State,Signals> inverseModelFunction,
			final T2Functor<State, State, Signals> forwardModelFunction, 
			Core core,TreeMap<String,RankedWeaver> sws) throws HammerException
	{
		InverseModel inv = SimpleInverseModel.make(name, subscriptions, inverseModelFunction);
		ForwardModel fwd = SimpleForwardModel.make(name, subscriptions, forwardModelFunction);
		registerInverseForwardPair(inv, fwd, core,sws);
	}
	public void registerInverseForwardPair(final ArrayList<InverseModel> imArray, ArrayList<ForwardModel> fmArray, Core core,TreeMap<String,RankedWeaver> sws){
		for(InverseModel im: imArray){
			for(ForwardModel fm: fmArray){
				try
				{
					registerInverseForwardPair(cloner.deepClone(im),cloner.deepClone(fm),core,sws);
				} catch (HammerException e)
				{
				}
			}
		}
	}

	public final ConfidenceMap getConfidenceMap() throws InterruptedException
	{
		pimpl_.ThreadWait();;
		return pimpl_.getConfidenceMap();
	}

	public void setConfidence(String name, Double confidence) throws InterruptedException
	{
		pimpl_.ThreadWait();
		for (RankedWeaver RankedWeaver_ : pimpl_.weavers_.values())
		{
			if (RankedWeaver_.weaver.getName() == name)
			{
				RankedWeaver_.weaver.setConfidence(confidence);
				return;
			}
		}
	}

	public void resetConfidences() throws InterruptedException
	{
		pimpl_.ThreadWait();
		pimpl_.RunModelsMutex_.lock();

		for (RankedWeaver RankedWeaver_ : pimpl_.weavers_.values())
		{
			RankedWeaver_.weaver.resetConfidence();
		}

		pimpl_.RunModelsMutex_.unlock();
	}

	public void ThreadWait() throws InterruptedException
	{
		pimpl_.ThreadWait();
	}

	public class Impl
	{
		public Impl()
		{

			weavers_ = new HashMap<String,RankedWeaver>(); // All thread weavers are
														// registered here
			stateQueue_ = new LinkedList<StatePair>(); // Future state pairs are
														// saved here
			shortcuts_ = new Dictionary<Integer>(); // Each inverse and forward
													// model pair with execution
													// rank is saved here
			focuses_=new ArrayList<String>();
			actions_=new Dictionary<String>();
			selectedModels_=new Dictionary<String>();
			sortedWeavers_=new TreeMap<String, RankedWeaver>(new RankedWeaverComparator(weavers_)); //Reference the original weaver//
			
			executing_ = true;
			StateUpdated_ = true;
			
			ReadyToRunMutex_ = new ReentrantLock();
			RunModelsMutex_ = new ReentrantLock();

			ReadyToRunCondition_ = ReadyToRunMutex_.newCondition();
			RunModelsCondition_ = RunModelsMutex_.newCondition();
			StateUpdatedCondition_ = ReadyToRunMutex_.newCondition();
			OneTreadReadyCondition_ = ReadyToRunMutex_.newCondition();
			
			coreThread_ = new CoreThread();
			Thread t = new Thread(coreThread_);
			t.start();
		}

		/**
		 * Variable definition
		 */
		public Map<String,RankedWeaver>			weavers_;
		public Queue<StatePair>					stateQueue_;
		public Dictionary<Integer>				shortcuts_;
		public ArrayList<String>				focuses_;
		public TreeMap<String,RankedWeaver>	 	sortedWeavers_;
		public Dictionary<String>				actions_;
		public Dictionary<String>				selectedModels_;
		public Pair<Dictionary<String>,Dictionary<String>> selectedResults;   
		public T2Functor<Double, StateMap,String>confidenceFunctor_;
		public T3Functor<Pair<Dictionary<String>,Dictionary<String>>,TreeMap<String,RankedWeaver>,State,State>	selector_;
		
		/**
		 * Operation states definitions
		 */
		boolean	executing_;
		boolean	StateUpdated_;

		/**
		 * Lock definitions
		 */
		Lock	ReadyToRunMutex_;
		Lock	RunModelsMutex_;

		/**
		 * Running condition definitions
		 */
		private Condition	ReadyToRunCondition_;
		private Condition	RunModelsCondition_;
		private Condition	StateUpdatedCondition_;
		public  Condition	OneTreadReadyCondition_;
		/**
		 * Counter
		 */
		int RunModelsCounter_;
		public int ReadyModelsCounter_;
		
		/**
		 * Thread
		 */
		CoreThread coreThread_ = new CoreThread();

		/**
		 * Callback Functor
		 */
		V1Functor<Condition> CoreReadyCallbackFunctor_;
		
		/**
		 * This function registers the functor to communicate with higher hierarchy, main thread;
		 * @param functor
		 */
		public void RegisterCoreReadyCallback(V1Functor<Condition> functor){
			CoreReadyCallbackFunctor_=functor;
		}
		
		/**
		 * @author hao Definition of call back function
		 */
		public class RunModelsFunctor extends VoidFunctor
		{
			public void Function()
			{
				RunModelsCallback();
			}
		}

		public class ReadyModelsFunctor extends VoidFunctor
		{
			public void Function()
			{
				ReadyModelsCallback();
			}
		}
		
		/**
		 * @author hao Main thread in core
		 */
		public class CoreThread implements Runnable
		{
			public void run()
			{
				State current;
				State target;
				while (true)
				{

					ReadyToRunMutex_.lock();
					// ReadyToRun state gets current/target state
					
					while (executing_ && stateQueue_.isEmpty())
					{
						StateUpdated_ = true;
						StateUpdatedCondition_.signalAll(); // Signal only if no more state to execute
						try
						{
							ReadyToRunCondition_.await(); // wait to get current and target states
						} catch (InterruptedException e)
						{
						}
					}

					if (!executing_)
					{
						break;
					}

					// poll retrieve and delete the name state pair in the queue
					final StatePair front = stateQueue_.poll();
					current = front.first;
					target = front.second;

					// StateUpdated state updates confidences and runs inverse and forward model
					try
					{
						// Threads are sorted in rank for every time stamp
						sortedWeavers_.clear();
						sortedWeavers_.putAll(weavers_);
						
						runInverseAndForwardModels(current, target);
						
						ReadyModelsCounter_=0;
						//debugging
						//System.out.println("Significant data: "+focuses_);
						focuses_.clear();
						//System.out.println("=====================================================================");
					} catch (InterruptedException e)
					{
					}
					ReadyToRunMutex_.unlock();
				}
			}
		};

		public void registerThreadWeaver(final ThreadWeaver weaver) throws HammerException
		{
			RunModelsMutex_.lock();
			//debugging
			//System.out.println("start register weavers");
			int maxExecutionRank = 0;
			Iterator<String> it = shortcuts_.data.keySet().iterator();
			Iterator<String> end = shortcuts_.data.keySet().iterator();
			while (end.hasNext())
			{
				end.next();
			}

			for (String dep : weaver.getDependencies())
			{
				if (it == end)
				{
					throw new HammerException(dep + " not found, but listed as dependency for " + weaver.getName());
				}

				// Assign a execution rank 1 class higher than the maximum of
				// its offspring
				
				// Debugging
				//System.out.println("dep: "+dep);
				int currentExecutionRank = shortcuts_.data.get(dep) + 1;
				if (currentExecutionRank > maxExecutionRank)
				{
					maxExecutionRank = currentExecutionRank; // Keep updating
																// the max rank
				}
			}

			RunModelsFunctor RunModelsCallback_ = new RunModelsFunctor();
			ReadyModelsFunctor ReadyModelsCallback_=new ReadyModelsFunctor();
			
			// Register the core callback function to thread callback function
			weaver.registerCallback(RunModelsCallback_);
			weaver.registerReadyCallback(ReadyModelsCallback_);
			
			// Careful, name set the watermark..
			shortcuts_.put(weaver.imName_, maxExecutionRank);

			// debugging
			System.out.println("Execution Rank: "+maxExecutionRank);

			// Create pair of thread weaver with rank of current maximum
			// execution rank.
			// It means a model may be allocated with higher execution rank than
			// it should
			// if it is registered after most other models.
			RankedWeaver newWeaver = new RankedWeaver(weaver, maxExecutionRank);

			// debugging
			//System.out.println("weaver name " + weaver.getName());

			// and save it with the collection of thread weavers
			weavers_.put(newWeaver.weaver.getName(),newWeaver);
			//System.out.println(newWeaver.weaver.getName() + "is registered");
			RunModelsMutex_.unlock();
		}

		// cannot override wait function, so create threadwait instead
		public void ThreadWait() throws InterruptedException
		{

			ReadyToRunMutex_.lock();

			try
			{
				while (executing_ && !StateUpdated_)
				{
					StateUpdatedCondition_.await();
				}
			} finally
			{
				ReadyToRunMutex_.unlock();
			}
		}

		// This function sets inputs for each model pairs and triggers them into
		// operating stage
		public void runInverseAndForwardModels(final State current, final State target) throws InterruptedException
		{
			ArrayList<String> focusesTemp=new ArrayList<String>();
			Signals tempSignals=new Signals();
			RunModelsMutex_.lock();
			try
			{
				RunModelsCounter_ = 0;
				int currentExecutionRank = 0;
				ConfidenceMap confidences = getConfidenceMap();

				//Debugging
				//System.out.println("weavers: "+weavers_.values());
				//System.out.println(sortedWeavers_.values());
				
				for (RankedWeaver RankedWeaver_ : sortedWeavers_.values())
				{
					focusesTemp.addAll(RankedWeaver_.weaver.inverseModel_.getSubcriptions());
					
					// If current thread requires higher execution rank. wait
					// for all threads in the lower rank to finish
					if (RankedWeaver_.rank > currentExecutionRank)
					{
						while (RunModelsCounter_ > 0)
						{
							RunModelsCondition_.await();// model pairs operate sequentially
						}
						
						System.out.println("------------------------Run in Parellel------------------------------");
						
						// Increment the current execution rank
						++currentExecutionRank;
						confidences = getConfidenceMap();

					}

					// debugging
					System.out.println("weaver " + RankedWeaver_.weaver.getName() + "\t with rank " + RankedWeaver_.rank + " running in rank " + currentExecutionRank + " || Confidence: " + confidences.get(RankedWeaver_.weaver.getName()));
					
					// Throw the error message if thread rank does not equals to
					// execution rank
					assert(RankedWeaver_.rank == currentExecutionRank) : "Execution Rank mismatch";

					// Increment the number of running thread
					++RunModelsCounter_;

					// set inputs for each thread weaver, which triggers a
					// thread to run inverse and forward model
					RankedWeaver_.weaver.setInputs(current, target, confidences);

				}
				while (RunModelsCounter_ > 0)
				{
					RunModelsCondition_.await();
				}
				select(sortedWeavers_,current,target);//select actions after all models finishing operating
				for(int i=focusesTemp.size()-1;i>0;i--){
					String aspect_=focusesTemp.get(i);
					if(!focuses_.contains(aspect_)){
						focuses_.add(aspect_);
					}
				}
				System.out.println("Suggested actions: "+actions_.data+"selected by model: "+selectedModels_.data);
				System.out.println("===============================================================================================");
			
			} finally
			{
				RunModelsMutex_.unlock();
			}

		}

		// It decrements number of running models. Sequential control.
		public void RunModelsCallback()
		{

			RunModelsMutex_.lock();

			try
			{
				--RunModelsCounter_;
				if (RunModelsCounter_ == 0)
				{
					RunModelsCondition_.signal();
				}
			} finally
			{
				RunModelsMutex_.unlock();
			}

		}

		public void ReadyModelsCallback()
		{
			//OneTreadReadyCondition_.signal();
			ReadyModelsCounter_++;
		}
		
		/**
		 * Compute the confidence value of an inverse model pair.
		 * 
		 * @param Actual
		 * @param Predicted
		 * @param inverseModelName
		 * @return
		 * @throws HammerException
		 */
		
		ConfidenceMap getConfidenceMap()
		{
			ConfidenceMap result = ConfidenceMap.make();
			for (RankedWeaver RankedWeaver_ : weavers_.values())
			{
				result.put(RankedWeaver_.weaver.getName(), RankedWeaver_.weaver.getConfidence());
			}
			return result;
		}
	}
	
	public double confidence(final State Actual, final State Predicted, final String inverseModelName, final String aspect)
	{
		//debugging
		//System.out.println("ready to run confidence function");
		// Check functions are the same
		if (pimpl_.confidenceFunctor_ == null)
		{
			try
			{
				//System.out.println("Confidence Function was not defined");
				throw new HammerException("Confidence Function was not defined");
			} catch (HammerException e)
			{
			}
		}

		// Create a new state map
		StateMap stateMap = StateMap.make(Actual, Predicted, inverseModelName);
		//stateMap.
		
		// And evaluate the confidence with user-defined function
		//debugging
		stateMap.setAspects(Predicted.getAspects());//to fix the problem of subscriptions temporally
		//System.out.println("statemap aspects"+stateMap.getAspects()+"Actual aspect"+Actual.getAspects()+"predicted aspect"+Predicted.getAspects());
		//System.out.println("run confidence function");
		//System.out.println(pimpl_.confidenceFunctor_.Function(stateMap));
		return pimpl_.confidenceFunctor_.Function(stateMap,aspect);
	}

	public void setConfidenceFunction(final T2Functor<Double, StateMap,String> confidenceFunctor)
	{
		pimpl_.confidenceFunctor_ = confidenceFunctor;
	}
	
	public void setSelector(final T3Functor<Pair<Dictionary<String>,Dictionary<String>>,TreeMap<String,RankedWeaver>,State,State> selector){
		pimpl_.selector_=selector;
	}
	
	public void select(TreeMap<String,RankedWeaver>sortedWeavers,State current,State target){
		if (pimpl_.selector_ == null)
		{
			try
			{
				//System.out.println("Confidence Function was not defined");
				throw new HammerException("Selector Function was not defined");
			} catch (HammerException e)
			{
			}
		}
		pimpl_.selectedResults=pimpl_.selector_.Function(sortedWeavers, current, target);
		pimpl_.actions_=pimpl_.selectedResults.first;
		pimpl_.selectedModels_=pimpl_.selectedResults.second;
	}
	
}
