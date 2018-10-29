package edu.neu.cs6650.project2.MyClient;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class RequestTest implements Runnable{
	private String url;
	private int userPopulation;
	private int timeInterval;
	private int day;
	private int numTestPerPhase;
	private int stepUpperBound;
	
    private int totalRequestCount;
    private int successRequestCount;
    private List<Long> requestTimes;
	
	public RequestTest(String url, int userPopulation, int timeInterval, int day, int numTestPerPhase, int stepUpperBound) {
		this.url = url;
		this.userPopulation = userPopulation;
		this.timeInterval = timeInterval;
		this.day = day;
		this.numTestPerPhase = numTestPerPhase;
		this.stepUpperBound = stepUpperBound;
	}
	
	public int getTotalRequestCount() {
		
		return this.totalRequestCount;
	}
	
	public int getSuccessRequestCount() {
		
		return this.successRequestCount;
	}
	
	public List<Long> getRequestTimes() {
		
		return this.requestTimes;
	}
	
	public void run() {
		
		this.requestTimes = new ArrayList<Long>();
        //String threadId = new Long(Thread.currentThread().getId()).toString();

       
//        WebTarget webTarget = client.target(url);
//        Invocation.Builder getCaller = webTarget.request();
//        Invocation.Builder postCaller = webTarget.request();
		
		for (int i = 0; i < numTestPerPhase; ++i) {
			// post1
			DummyData data1 = new DummyData(userPopulation, day, timeInterval, stepUpperBound);
			post(data1);
			// post2
			DummyData data2 = new DummyData(userPopulation, day, timeInterval, stepUpperBound);
			post(data2);
			// get1
			getCurrentDay(data1.getUserID());
			// get2
			getGivenDay(data2.getUserID(), data2.getDay());
			// post3
			DummyData data3 = new DummyData(userPopulation, day, timeInterval, stepUpperBound);
			post(data3);
			
			this.totalRequestCount += 5;
		}
	}
	
	private boolean post(DummyData data) {
		
		Client client = ClientBuilder.newClient();
		
		long stime = System.currentTimeMillis();
		String postUrl = url + "/" + data.getUserID() + "/" + data.getDay() + "/" + data.getTimeInterval() + "/" + data.getStepCount();
		try {
			
			WebTarget webTarget = client.target(postUrl);		
			Invocation.Builder postCaller = webTarget.request();
			postCaller.post(Entity.entity(data, MediaType.TEXT_PLAIN));
			
			long etime = System.currentTimeMillis();
			this.requestTimes.add(etime - stime);
			this.successRequestCount++;
			//System.out.println("Post success!");
			return true;
		} catch (Exception e) {
			System.out.println("Post error : " + e.getMessage());
			return false;
		}
		
	}
	
	private boolean getCurrentDay(int userID) {
		
		Client client = ClientBuilder.newClient();
		
		long stime = System.currentTimeMillis();
		String getUrl = url + "/current/" + userID;
		try {
			WebTarget webTarget = client.target(getUrl);
			Invocation.Builder getCaller = webTarget.request();
			getCaller.get(Integer.class);
			
			long etime = System.currentTimeMillis();
			this.requestTimes.add(etime - stime);
			this.successRequestCount++;
			//System.out.println("Get curr day success!");
			return true;
		} catch (Exception e) {
			System.out.println("Get curr day error : " + e.getMessage());
			return false;
		}
	}
	
	private boolean getGivenDay(int userID, int day) {
		
		Client client = ClientBuilder.newClient();
		
		long stime = System.currentTimeMillis();
		String getUrl = url + "/single/" + userID + "/" + day;
		try {
			
			WebTarget webTarget = client.target(getUrl);
			Invocation.Builder getCaller = webTarget.request();
			getCaller.get(Integer.class);
			
			long etime = System.currentTimeMillis();
			this.requestTimes.add(etime - stime);
			this.successRequestCount++;
			//System.out.println("Get given day success!");
			return true;
		} catch (Exception e) {
			System.out.println("Get given day error : " + e.getMessage());
			return false;
		}
	}
	
}
