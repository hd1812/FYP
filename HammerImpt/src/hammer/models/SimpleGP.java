package hammer.models;


import java.lang.*;
import org.ejml.simple.SimpleMatrix;
import org.ejml.*;
import org.ejml.factory.*;
import org.ejml.interfaces.linsol.LinearSolver;
import org.ejml.data.DenseMatrix64F;

public class SimpleGP {

    private double _sig;
    private double _mean=0;
    private double _noise=0.001;
    public SimpleMatrix _observations=new SimpleMatrix(0,0);  //vector
    public SimpleMatrix _samples=new SimpleMatrix(0,0); // matrix
    public SimpleMatrix _kernel=new SimpleMatrix(0,0);
    public SimpleMatrix _invertedKernel=new SimpleMatrix(0,0);


    public SimpleGP()
    {
	_sig=0.1;
    }
    public SimpleGP(double sig)
    {
	_sig=sig;
    }


    public void addSample(SimpleMatrix a)
    {
    	//copy inputs data into sample
    	//System.out.println("----------_samples-----------");
    	//System.out.println(_samples);
    	_samples=_samples.combine(_samples.numRows(), 0, a);
    	//System.out.println(_samples);
    	//System.out.println("----------end-----------");
		//update kernel and inverse kernels
		_computeKernel();
		_computeInvertedKernel();
    }
    
    public void addObs(SimpleMatrix a){
    	//System.out.println("--------_observations------------");
    	//System.out.println(_observations);
    	_observations=_observations.combine(_observations.numRows(), 0, a);
    	//System.out.println(_observations);
    	//System.out.println("-----------end----------");

		for(int i=0; i<_observations.numCols();i++){
			_observations.set(_observations.numRows()-1,i,a.get(0,i));
	    }
		_mean=_observations.elementSum()/_observations.numRows();//update mean
    }

    public double mu(SimpleMatrix a)
    {
		//todo add check if a is vector
		if(_samples.numRows()==0){
		    return _mean;
		}
		
		//debugging
		//System.out.println("=============MU=============");
		//System.out.println(_k(a).toString()+_invertedKernel.toString()+_observations.toString());
		return _mean + _k(a).transpose().mult(_invertedKernel.mult(_observations.minus(_mean))).get(0,0);
    }
    public double sigma(SimpleMatrix a)
	{
		if(_samples.numRows()==0){
	    	return _kernelFunction(a,a);
		}
		return _kernelFunction(a,a) - _k(a).transpose().mult(_invertedKernel.mult(_k(a))).get(0,0);
    }

    public void setObservations(SimpleMatrix observations)
	{
		_observations=observations.copy();
		_mean=_observations.elementSum()/_observations.numRows();
    }
    public void setSamples(SimpleMatrix samples)
	{
		_samples=samples.copy();
		_computeKernel();
		_computeInvertedKernel();
    }

    public SimpleMatrix getInvertedKernel()
    {return _invertedKernel;}
    public SimpleMatrix getKernel()
    {return _kernel;}



    //K*=[K(x*,x1),K(x*,x2),...]
    private SimpleMatrix _k(SimpleMatrix a)
    {
		SimpleMatrix k = new SimpleMatrix(_samples.numRows(),1);
		for(int i=0;i<_samples.numRows();i++){
			//debugging
			//System.out.println("a:");
			//System.out.println(a.toString());
			//System.out.println("_samples:");
			//System.out.println(_samples.toString());
		    k.set(i,0,_kernelFunction(a,_samples.extractVector(true,i)));
		}
		return k;
    }

    //Gaussian Kernel covariance function
    private double _kernelFunction(SimpleMatrix a, SimpleMatrix b)
    {
	//todo add check if a and b are vectors
    	//System.out.println("norm: "+a.minus(b).normF()+"\n a "+a+"\n b: "+b+" \n sig:"+_sig+"\n k:"+Math.exp(-a.minus(b).normF()/(2*_sig*_sig)));
    	//Math.exp(-Math.pow(a.minus(b).normF(), 2)/(2*_sig*_sig));
    	return Math.exp(-a.minus(b).normF()/(2*_sig*_sig));

    }
    
    //kernels n*n matrix
    private void _computeKernel()
    {
		if(_samples.numRows()==0){
		  _kernel=new SimpleMatrix(0,0);
		}
		
		tuneKernel();//modify sig to tune kernel parameter
		
		_kernel=new SimpleMatrix(_samples.numRows(),_samples.numRows());
	
		for(int i=0;i<_samples.numRows();i++)
		{
			_kernel.set(i,i,_kernelFunction(_samples.extractVector(true,i),_samples.extractVector(true,i)) + _noise);
			for(int j=i+1;j<_samples.numRows();j++)
			{
				_kernel.set(i,j,_kernelFunction(_samples.extractVector(true,i),_samples.extractVector(true,j)));
				_kernel.set(j,i,_kernelFunction(_samples.extractVector(true,i),_samples.extractVector(true,j)));
			}
		}
		//debugging
	}

    private void _computeInvertedKernel()
    {
	LinearSolver< DenseMatrix64F> solver =   LinearSolverFactory.lu(_kernel.numRows());
	if( !solver.setA(_kernel.getMatrix()) ) {
	    throw new IllegalArgumentException("Singular matrix");
	}
	
	/*	if( solver.quality() <= 1e-8 )
	    throw new IllegalArgumentException("Nearly singular matrix");
	*/

	_invertedKernel=new SimpleMatrix(_kernel.numRows(),_kernel.numRows());

	solver.invert(_invertedKernel.getMatrix());
	
    }
    
    public boolean isSampleEmpty(){
    	if(_samples.numRows()<=0){
    		return true;
    	}
    	else{
    		return false;
    	}
    }

    public boolean isObservationEmpty(){
    	if(_observations.numRows()<=0){
    		return true;
    	}
    	else{
    		return false;
    	}
    }
    
    public void tuneKernel(){
    	Double sum=0.0;
    	SimpleMatrix a=new SimpleMatrix();
    	SimpleMatrix b=new SimpleMatrix();
    	for(int i=0;i<_samples.numRows();i++){
    		for(int j=0;j<_samples.numRows();j++){
    			a=_samples.extractVector(true,i);
    			b=_samples.extractVector(true,j);
    			sum+=a.minus(b).normF();
    		}
    	}
    	_sig=Math.sqrt(sum/(_samples.numRows()*_samples.numRows())/2);
    }
    
}