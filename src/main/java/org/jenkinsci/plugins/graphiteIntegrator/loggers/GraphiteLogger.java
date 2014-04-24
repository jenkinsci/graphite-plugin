
package org.jenkinsci.plugins.graphiteIntegrator.loggers;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.*;


/**
 * @author joachimrodrigues
 *
 */
public class GraphiteLogger {

    /**
     * 
     */
    PrintStream logger ;

    /**
     *
     * @param logger
     */
    public GraphiteLogger(PrintStream logger) {
        this.logger = logger;
    }
    
    
    /**
     *
     * @param graphiteHost
     * @param graphitePort
     * @param queue
     * @param metric
     * @throws UnknownHostException
     * @throws IOException
     */
    public void logToGraphite(String graphiteHost, String graphitePort, String queue, String metric, String protocol) throws UnknownHostException, IOException {
    	
    	if (protocol.equals("TCP")) {
    		logToGraphiteTCP(graphiteHost, graphitePort, queue, metric);
    	}
    	
    	if (protocol.equals("UDP")) {
    		logToGraphiteUDP(graphiteHost, graphitePort, queue, metric);
    	}
    }
    
    private void logToGraphiteUDP(String graphiteHost, String graphitePort, String queue, String metric) throws UnknownHostException, IOException {
        //TMP to test
        long timestamp = System.currentTimeMillis()/1000;
        String data = queue + " " + metric + " " + timestamp + "\n";
        
    	int intPort = Integer.parseInt(graphitePort);
    	byte[] buffer = data.getBytes();
        InetAddress IPAddress = InetAddress.getByName(graphiteHost);
    	
        try {
        	DatagramSocket sock = new DatagramSocket(intPort);
        	DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, IPAddress, intPort);
        	sock.send(sendPacket);
        	sock.close();
        } catch(IOException e) {
        	e.printStackTrace();
        }
    }
    
    private void logToGraphiteTCP(String graphiteHost, String graphitePort, String queue, String metric) throws UnknownHostException, IOException  {
    	Socket conn = new Socket(graphiteHost, Integer.parseInt(graphitePort));
		
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        String data = queue + " " + metric + " " + (System.currentTimeMillis()/1000) + "\n";
        dos.writeBytes(data);
        conn.close();
    }
}
