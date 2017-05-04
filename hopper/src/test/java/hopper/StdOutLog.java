package hopper;

import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.exec.LogOutputStream;

public class StdOutLog extends LogOutputStream {
	
    private  ArrayBlockingQueue<String> cache;
    
    public StdOutLog(int cacheSize) {
    	cache=new ArrayBlockingQueue<String>(cacheSize);
	}
    
    
    @Override
    protected void processLine(String line, int level) {
    	while(!cache.offer(line)) cache.poll();
    }

};
