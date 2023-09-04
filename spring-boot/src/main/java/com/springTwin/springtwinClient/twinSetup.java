package com.springTwin.springtwinClient;
import org.json.*; 

public class twinSetup {

	// TODO : Change to command line args
//	private static final String DATA_PATH = "/home/kuljeet/Twin/data/train_data/train_FD001/";
	private static final String DATA_PATH = "data/train_data/train_FD001/";
	private static final String FILE_PREFIX = "motor";
	private static final String FILE_SUFFIX = ".csv";
	
	
	public int twinNodes;
	twinMotor twinNode[];
	
	public twinSetup(int node_cnt) throws InterruptedException {
		int n=0;
		twinNode = new twinMotor[node_cnt];
		for (n=0;n<node_cnt;n++) {
			String filename =  DATA_PATH + FILE_PREFIX + Integer.toString(n+1) + FILE_SUFFIX;
			System.out.printf("Creating twin of motor %d... \n",n+1);
			System.out.println("Data Source : "+filename);
			twinNode[n] = new twinMotor(twinMotor.Mode.SIMULATED,n+1);
			System.out.printf("Setting up twin of motor %d... \n",n+1);
			if(twinNode[n].setup_motor(filename)==false) throw new InterruptedException();
		}
		twinNodes = node_cnt;

		System.out.println("Starting Simulation..");
		startSimulation();
//		System.out.printf("Twin %d Sensor %d : %f \n", 1,1, twinNode[0].get_sensor_value(1));
//		System.out.printf("Twin %d Settingr %d : %f \n", 1,1, twinNode[0].get_setting(1));
		
	}
	
	public int get_device_count() {
		return twinNodes;
	}

	public JSONObject get_device_telemetry(int no) {
		try {
			if(no > 0 && no <= twinNodes)
				return twinNode[no-1].get_telemetry();
		} catch(JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getRoutingKey(int no) {
		String routing_key =null ;
		if(no > 0 && no <= twinNodes)
			routing_key = new String(FILE_PREFIX+"_"+twinNode[no-1].getMotorId());
		return routing_key;
	}
	
	public void startSimulation() {
		int n=0;
		for (n=0;n<twinNodes;n++) {
			twinNode[n].startSimulation();
		}
	}
}
