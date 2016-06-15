package hammer.forwardmodel;

import java.util.Random;

import org.ejml.simple.SimpleMatrix;

import hammer.exception.HammerException;
import hammer.models.SimpleGP;
import hammer.signals.Signals;
import hammer.state.State;

public class Test
{

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
	    SimpleGP _gp=new SimpleGP();
	    Random rand=new Random();
		Double action = 0.0;
		Double base = 0.0;
		base=15.0;

		SimpleMatrix a= new SimpleMatrix(1,1); //
		SimpleMatrix ob=new SimpleMatrix(1,1);
		
		Double result;
		a.set(0,0,1);
		result=_gp.mu(a);
		System.out.println("result test"+result);
		for(int i=0;i<10;i++){
			
			action = rand.nextInt(4)*10.0;
			a.set(0,0,action);

			result=_gp.mu(a);

			ob.set(action);
			if(_gp.isSampleEmpty()||_gp.isObservationEmpty()){
				_gp.setSamples(a);
				_gp.setObservations(ob);
			}
			else{
				_gp.addSample(a);
				_gp.addObs(ob);
			}
			
			System.out.println("action: "+action+" pred: "+result);
		}
		System.out.println("=====================testing===========================");
		for(int i=0;i<5;i++){
			
			action = rand.nextDouble()*40;
			a.set(0,0,action);

			result=_gp.mu(a);
			
			System.out.println("action: "+action+" pred: "+result);
		}
		
		//System.out.println(" init " + base + " prediction " + result);
		//debugging
		//System.out.println("Predict: "+result);
	    return;
	}

}
