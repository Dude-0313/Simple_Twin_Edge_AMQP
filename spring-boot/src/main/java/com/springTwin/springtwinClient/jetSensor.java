package com.springTwin.springtwinClient;


public class jetSensor {

	public enum Mode{
		SIMULATED,
		REAL
	};
	
	private double[] sim_values;
	private double value = 0.0;
	private int interval_in_secs=1;
	private Mode mode=Mode.SIMULATED;
	
	
	public	jetSensor() {
		mode = Mode.REAL;
		value = 0.0;
		interval_in_secs=1;
	}

	//Setup a simulated Sensor
	public	jetSensor(double[] inValues,int interval) {
		mode= Mode.SIMULATED;
		load_values(inValues);
		set_interval(interval);
	}
	
	public void set_interval(int duration) {
		interval_in_secs=duration;
	}
	
	// Set simulated values
	public void load_values(double[] in) {
	   sim_values = in;
	}

	//Read value from an actual sensor
	public void read_value() {
		   //value = in;
		}

	//Read value from an simulated sensor
	public void read_value(double in) {
		   value = in;
		}
	
	
	 public double get_value() {
		   return value ;
	 }
	 
	 public boolean startSimulation() {
		 Thread simSensor = new Thread(new Simulation());
		 simSensor.start();
		 return true;
	 }
	 
	 class Simulation implements Runnable{
		 
		 private boolean repeatable = false;
		 private static int index=0;
		 
		 public int max_items = 0;
		 
		 public void run() {
			 
			 if(index >= max_items) {
				 if(repeatable)
					 index=0;
			 }
			 else index++;
			 
			 read_value(sim_values[index]);
			 try {
				 Thread.sleep(interval_in_secs * 1000);
			 }catch(InterruptedException e) {}
		 }
	 }
	 
	
}
