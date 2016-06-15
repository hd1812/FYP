package virtual_patient.paramGen;

//parameters to simulate glucose and insulin dynamics
public class Params
{	
	//Glucose KInetics
	public Double V_G;
	public Double k_1;
	public Double k_2;
	
	//Insulin Kinetics
	public Double V_I;
	public Double m_1;
	public Double m_2;
	public Double m_3;
	public Double m_4;
	public Double m_5;
	public Double m_6;
	public Double HE_b;
	
	//Rate of Appearance
	public Double k_max;
	public Double k_min;
	public Double k_abs;
	public Double k_gri;
	public Double f;
	public Double a;
	public Double b;
	
	//Endogenous Production
	public Double k_p1;
	public Double k_p2;
	public Double k_p3;
	public Double k_p4;
	public Double k_i;
	
	//Utilization
	public Double F_cns;
	public Double V_m0;
	public Double V_mx;
	public Double K_m0;
	public Double p_2U;
	
	//Secretion
	public Double K;
	public Double alpha;
	public Double beta;
	public Double gamma;
	
	//Renal Excretion
	public Double k_e1;
	public Double k_e2;

	//Subcutaneous Insulin
	public Double k_a1;
	public Double k_a2;
	public Double k_d;
	//-------------------------------Glucose subsystem--------------------------------------------
	
	//Variables for G_p_d1 -- 1st derivative of plasma glucose masses
	public Double G_p_d1;
	public Double EGP;
	public Double Ra;	
	public Double E;	
	public Double E_b;
	public Double G_pb;		//Gp at time 0
	public Double G_p;
	
	//Variables for Gt_d1 -- 1st derivative of rapidly equilibrating tissues
	public Double G_t_d1;
	public Double G_tb;		//Gt at time 0
	public Double G_t;		//rapidly equilibrating tissues
	
	//Variables for G -- plasma glucose concentration	
	public Double G_b;		//G at time 0
	public Double G;	
	
	//------------------------------Insulin Subsystem---------------------------------------------
	//Variables to find coeffs
	
	public Double HE;	//Hepatic Extraction
	
	public Double S;
	public Double S_b;
	public Double D_b; //basal degradation, not insulin injection
	
	//Variables to find I_t_d1
	public Double I_l_d1;
	public Double I_1;
	public Double I_1_d1;
	public Double I_lb;			//I_l at time 0
	public Double I_l;		//initialisation
	
	//Variables to find I_p_d1
	public Double I_p_d1;
	public Double I_pb;
	public Double I_p;		//Initialisation
	
	//Variables to find I
	public Double I_b;			//I at time 0
	public Double I;		//Initialisation
	
	//----------------------------Unit Processes----------------------------------------

	//1. Variables to find Endogenous Glucose Production
	public Double EGP_b;	//basal state
	public Double I_d;
	public Double I_po;
	
	//Variables to find I_1_d1 and I_d_d1
	public Double I_d_d1;
	
	//Variables to find basal state k_p1;
	public Double I_pob;
	
	//2. Variables to find glucose rate of Appearance
	public Double Q_sto;
	public Double Q_sto1;
	public Double Q_sto2;
	public Double Q_sto1_d1;
	public Double Q_sto2_d1;	
	public Double Q_gut;
	public Double Q_gut_d1;
	public Double BW;	//body weight default 70
	public Double D_;	//calculated from integrating d
	public Double d_;	//insulin dose
	public Double k_gut;
	
	//3. Glucose utilisation
	public Double K_mx;
	public Double V_m;
	public Double K_m;
	
	public Double X;
	public Double X_b;
	public Double X_d1;
	public Double U;
	public Double U_ii;
	public Double U_id;
	public Double U_b;
	
	//4. Insulin Secretion
	public Double I_po_d1;
	public Double S_po;
	public Double Y;
	public Double Y_b;
	public Double h;
	public Double Y_d1;
	
	//5. Glucose Renal Excretion
	//**
	
	//Subcutaneous Insulin Kinetics
	public Double S_1;
	public Double S_2;
	public Double S_1_d1;
	public Double S_2_d1;
	public Double u_;		//dose
	
	public void matchType(String option){
		
		//params with same values; checked
		k_a1=0.00;
		k_a2=0.017;
		k_d=0.011;
		BW=70.0;
		X_b=0.0;	//??
		K_mx=0.0;//collapse to 0, dalla man	//??
		EGP_b=1.9;
		Q_sto=0.0;	//
		Q_sto1=0.0;	//
		Q_sto2=0.0;	//
		Q_gut=0.0;	//
		X_b=0.0;		//
		Y_b=0.0;		//
		S_1=0.0;		//
		S_2=0.0;		//
		//params with different values
		if(option.equals("normal")){
			
			V_G=1.88;		//checked
			k_1=0.065;
			k_2=0.079;
			
			//Insulin Kinetics
			V_I=0.05;
			m_1=0.190;
			m_2=0.484;
			m_4=0.194;
			m_5=0.0304;
			m_6=0.6471;
			HE_b=0.6;
			
			//Rate of Appearance
			k_max=0.0558;
			k_min=0.0080;
			k_abs=0.057;
			k_gri=0.0558;
			f=0.90;
			a=0.00013;
			b=0.82;
			
			//Endogenous Production
			k_p1=2.7;
			k_p2=0.0021;
			k_p3=0.009;
			k_p4=0.0618;
			k_i=0.0079;
			
			//Utilization
			F_cns=1.0;
			V_m0=2.5;
			V_mx=0.047;
			K_m0=225.59;
			p_2U=0.0331;
			
			//Secretion
			K=2.3;
			alpha=0.05;
			beta=0.11;
			gamma=0.5;
			
			//Renal Excretion
			k_e1=0.005;
			k_e2=339.0;
		}
		//From Antoine's PT1DM
		else if(option.equals("type1")){

			V_G=1.88;
			k_1=0.065;
			k_2=0.079;
			
			//Insulin Kinetics
			V_I=0.05;
			m_1=0.190;
			m_2=0.484;
			m_4=0.194 *0.66;
			m_5=0.0304 *0.9;
			m_6=0.6471*0.9;
			HE_b=0.6;
			
			//Rate of Appearance
			k_max=0.0558;
			k_min=0.0080;
			k_abs=0.057;
			k_gri=0.0558;
			f=0.90;
			a=0.01;
			b=0.82;
			
			//Endogenous Production
			k_p1=2.70 *1.35;
			k_p2=0.0021;
			k_p3=0.009 *0.66;
			k_p4=0.0618 *0.66;
			k_i=0.0079;
			
			//Utilization
			F_cns=1.0;
			V_m0=2.5;
			V_mx=0.047;
			K_m0=225.59;
			p_2U=0.0331 * 0.66;
			
			//Secretion
			K=2.3*0.33;
			alpha=0.050/0.33;
			beta=0.11 *0.33;
			gamma=0.5*0.33;
			
			//Renal Excretion
			k_e1=0.0005;
			k_e2=339.0;
		}
		else if(option.equals("type2")){
			
			V_G=1.49;
			k_1=0.042;
			k_2=0.071;
			
			//Insulin Kinetics
			V_I=0.04;
			m_1=0.379;
			m_2=0.673;
			m_4=0.269;
			m_5=0.0526;
			m_6=0.8118;
			HE_b=0.6;
			
			//Rate of Appearance
			k_max=0.0465;
			k_min=0.0076;
			k_abs=0.023;
			k_gri=0.0465;
			f=0.90;
			a=0.09;
			b=0.68;
			
			//Endogenous Production
			k_p1=3.09;
			k_p2=0.0007;
			k_p3=0.005;
			k_p4=0.0786;
			k_i=0.0066;
			
			//Utilization
			F_cns=1.0;
			V_m0=4.65;
			V_mx=0.034;
			K_m0=466.21;
			p_2U=0.084;
			
			//Secretion
			K=0.99;
			alpha=0.013;
			beta=0.05;
			gamma=0.5;
			
			//Renal Excretion
			k_e1=0.007;
			k_e2=269.0;
		}
		else {
			System.out.println("Patient type undefined");
		}
	}

	public Params()
	{
	}
	public Params(String s)
	{
		this.matchType(s);
	}
	public Params(Double weight)
	{
		this.BW=weight;
	}
	public Params(String s,Double weight)
	{
		this.matchType(s);
		this.BW=weight;
	}
}
