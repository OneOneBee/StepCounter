package edu.neu.cs6650.project2.MyClient;

import java.util.ArrayList;
import java.util.List;

public class Phase
{
    private String phaseName;
    private String url;
    private int threadCount;
    private int numTestPerPhase;
    private int startTimeInterval;
    private int endTimeInterval;
    private int dayNum;
    private int userPopulation;
    private int stepUpperBound;
    
    private List<Thread> threads;
    private long startTime;
    private long endTime;
    
    private int totalRequestCount;
    private int successRequestCount;
    private List<Long> requestTimes;
    
    
    public Phase(String phaseName, String url, int threadCount, int numTestPerPhase, 
    		int startTimeInterval, int endTimeInterval, int dayNum, int userPopulation, int stepUpperBound)
    {
        this.phaseName = phaseName;
        this.url = url;
        this.threadCount = threadCount;
        this.numTestPerPhase = numTestPerPhase;
        this.startTimeInterval = startTimeInterval;
        this.endTimeInterval = endTimeInterval;
        this.dayNum = dayNum;
        this.userPopulation = userPopulation;
        this.stepUpperBound = stepUpperBound;
    }

    public int getTotalRequestCount()
    {
     
    	return this.totalRequestCount;
    }

    public int getSuccessRequestCount()
    {
    	
    	return this.successRequestCount;
    }

    public List<Long> getRequestTimes()
    {
    	return this.requestTimes;
    }

    public void run() {
    	
        threads = new ArrayList<Thread>();
        requestTimes = new ArrayList<Long>();
    	
        startTime = System.currentTimeMillis();

        System.out.println(
                String.format(
                        "%s phase : %d threads each performs POST/GET request for %d times",
                        phaseName, threadCount, numTestPerPhase * (endTimeInterval - startTimeInterval + 1)));
        
        List<RequestTest> requestTests = new ArrayList<>();
        
        for (int time = this.startTimeInterval; time <= this.endTimeInterval; ++time) {
        	for (int i = 0; i < this.threadCount; ++i) {
        		RequestTest test = new RequestTest(url, this.userPopulation, time, this.dayNum, numTestPerPhase, stepUpperBound);
        		requestTests.add(test);
        		Thread thread = new Thread(test);
        		threads.add(thread);
        		thread.run();
        	}
        }
        
        // wait for all threads finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        
        for (RequestTest test : requestTests) {
    		this.totalRequestCount += test.getTotalRequestCount();
    		this.successRequestCount += test.getSuccessRequestCount();
    		requestTimes.addAll(test.getRequestTimes());
        }

        endTime = System.currentTimeMillis();

        System.out.println(
                String.format(
                        "%s phase complete : Time %f seconds",
                        phaseName, (endTime - startTime) / 1000.0));

    }
}
