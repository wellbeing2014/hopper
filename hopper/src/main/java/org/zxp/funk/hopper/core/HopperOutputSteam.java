package org.zxp.funk.hopper.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class HopperOutputSteam  extends OutputStream {


    /** Initial buffer size. */
    private static final int INTIAL_SIZE = 132;

    /** Carriage return */
    private static final int CR = 0x0d;

    /** Linefeed */
    private static final int LF = 0x0a;

    /** the internal buffer */
    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream(
      INTIAL_SIZE);

    private boolean skip = false;

    private final int level;
    
    

    /**
     * Creates a new instance of this class.
     * Uses the default level of 999.
     */
    public HopperOutputSteam() {
        this(999);
    }

    /**
     * Creates a new instance of this class.
     *
     * @param level loglevel used to log data written to this stream.
     */
    public HopperOutputSteam(final int level) {
        this.level = level;
    }

    /**
     * Write the data to the buffer and flush the buffer, if a line separator is
     * detected.
     *
     * @param cc data to log (byte).
     * @see java.io.OutputStream#write(int)
     */
    @Override
    public void write(final int cc) throws IOException {
        final byte c = (byte) cc;
        if (c == '\n' || c == '\r') {
            if (!skip) {
                processBuffer();
            }
        } else {
            buffer.write(cc);
        }
        skip = c == '\r';
    }

    /**
     * Flush this log stream.
     *
     * @see java.io.OutputStream#flush()
     */
    @Override
    public void flush() {
        if (buffer.size() > 0) {
            processBuffer();
        }
    }

    /**
     * Writes all remaining data from the buffer.
     *
     * @see java.io.OutputStream#close()
     */
    @Override
    public void close() throws IOException {
        if (buffer.size() > 0) {
            processBuffer();
        }
        super.close();
    }

    /**
     * @return the trace level of the log system
     */
    public int getMessageLevel() {
        return level;
    }

    /**
     * Write a block of characters to the output stream
     *
     * @param b the array containing the data
     * @param off the offset into the array where data starts
     * @param len the length of block
     * @throws java.io.IOException if the data cannot be written into the stream.
     * @see java.io.OutputStream#write(byte[], int, int)
     */
    @Override
    public void write(final byte[] b, final int off, final int len)
            throws IOException {
        // find the line breaks and pass other chars through in blocks
        int offset = off;
        int blockStartOffset = offset;
        int remaining = len;
        while (remaining > 0) {
            while (remaining > 0 && b[offset] != LF && b[offset] != CR) {
                offset++;
                remaining--;
            }
            // either end of buffer or a line separator char
            final int blockLength = offset - blockStartOffset;
            if (blockLength > 0) {
                buffer.write(b, blockStartOffset, blockLength);
            }
            while (remaining > 0 && (b[offset] == LF || b[offset] == CR)) {
                write(b[offset]);
                offset++;
                remaining--;
            }
            blockStartOffset = offset;
        }
    }

    /**
     * Converts the buffer to a string and sends it to {@code processLine}.
     */
    protected void processBuffer() {
        try {
			processLine(buffer.toString(charset));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        buffer.reset();
    }

    /**
     * Logs a line to the log system of the user.
     *
     * @param line
     *            the line to log.
     */
    protected void processLine(final String line) {
        processLine(line, level);
    }

    /**
     * Logs a line to the log system of the user.
     *
     * @param line the line to log.
     * @param logLevel the log level to use
     */
    private  ArrayBlockingQueue<String> cache;
    
    private String runningRegex;
    
    public void setRunningRegex(String runningRegex) {
		this.runningRegex = runningRegex;
	}

    private String charset="utf-8";
    
	public HopperOutputSteam(int cacheSize,String charset) {
		this(999);
		this.cache=new ArrayBlockingQueue<String>(cacheSize);
		this.charset =charset;
	}
    
    
    protected void processLine(String line, int level) {
    	notifyLogEvent(new TomcatLogEventObject(this, line));
    	boolean isRunning = Pattern.matches(runningRegex, line);
    	if(isRunning) {
    		Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(line);
            matcher.find();
            changeRunning(Integer.valueOf(matcher.group()));
    	}
    	while(!cache.offer(line)) cache.poll();
    }
    
    public Iterator<String> getCache(){
    	return cache.iterator();
    }
    
    
    private Vector<TomcatLogEventListener> logeventlist=new Vector<TomcatLogEventListener>();
    
    
    
    
    public void addTomcatLogEventListener(TomcatLogEventListener listener){
    	logeventlist.add(listener);
    }
    
    public void removeTomcatLogEventListener(TomcatLogEventListener listener){
    	logeventlist.remove(listener);
    }
    
   
    
    public void notifyLogEvent(TomcatLogEventObject obj)  
    {   
         Iterator<TomcatLogEventListener> it=logeventlist.iterator();  
         while(it.hasNext())  
         {  
             it.next().logEvent(obj); 
         }  
    }  
    
    public  abstract void changeRunning(int i);
}
