package org.jenkinsci.plugins.graphiteIntegrator.metrics;

import hudson.model.AbstractBuild;
import java.io.IOException;
import java.io.PrintStream;
import java.net.UnknownHostException;
import org.jenkinsci.plugins.graphiteIntegrator.loggers.GraphiteLogger;
import org.jenkinsci.plugins.graphiteIntegrator.Metric;
import org.jenkinsci.plugins.graphiteIntegrator.Server;
import utils.GraphiteValidator;

/**
 * 
 * @author joachimrodrigues
 */
public abstract class AbstractMetric {

	protected GraphiteValidator validator = new GraphiteValidator();
	protected final AbstractBuild<?, ?> build;
	protected final PrintStream logger;
	protected final GraphiteLogger graphiteLogger;
	protected final String baseQueueName;

	/**
	 * 
	 * @param build
	 * @param logger
	 * @param graphiteLogger
	 */
	public AbstractMetric(AbstractBuild<?, ?> build, PrintStream logger, GraphiteLogger graphiteLogger, String baseQueueName) {
		this.build = build;
		this.logger = logger;
		this.graphiteLogger = graphiteLogger;
		this.baseQueueName = baseQueueName;
	}

	/**
	 * @param server
	 * @param metric
	 * @param metricToSend
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	protected void sendMetric(Server server, Metric metric, String value) throws UnknownHostException,
			IOException {
		logger.println("Trying to send metric to Graphite server : " + server.getIp() + ":" + server.getPort() + ", Metric name: " + metric.getName() +  " On queue : "
				+ metric.getQueueName() + " With value : " + value);
		if (server.getProtocol().equals("UDP")) {
			logger.println("Metric: " + metric.getName() + " with value: "+ value + " correctly sent to " + server.getIp() + ":" + server.getPort()
					+ " on " + metric.getQueueName() + "using UDP");
			graphiteLogger.logToGraphite(server.getIp(), server.getPort(), baseQueueName + "." + metric.getFullQueueAndName(), value.trim(), server.getProtocol());
		}
		else if (server.getProtocol().equals("TCP")) {
			if (validator.isListening(server.getIp(), Integer.parseInt(server.getPort()))) {
				graphiteLogger.logToGraphite(server.getIp(), server.getPort(), baseQueueName + "." + metric.getFullQueueAndName(), value.trim(), server.getProtocol());
				logger.println("Metric: " + metric.getName() + " with value: "+ value + " correctly sent to " + server.getIp() + ":" + server.getPort()
					+ " on " + metric.getQueueName());
			} else {
				logger.println("Metric: " + metric.getName() + " with value: "+ value + " failed when sent to " + server.getIp() + ":" + server.getPort()
					+ " on " + metric.getQueueName());
			}
		}
	}

	/**
	 * 
	 * @param server
	 * @param metric
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public abstract void sendMetric(Server server, Metric... metric) throws UnknownHostException, IOException;
}
