/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package org.jenkinsci.plugins.graphiteIntegrator.metrics;

import hudson.model.AbstractBuild;
import hudson.tasks.test.AbstractTestResultAction;

import java.io.IOException;
import java.io.PrintStream;
import java.net.UnknownHostException;

import org.jenkinsci.plugins.graphiteIntegrator.Metric;
import org.jenkinsci.plugins.graphiteIntegrator.Server;
import org.jenkinsci.plugins.graphiteIntegrator.loggers.GraphiteLogger;

/**
 * 
 * @author joachimrodrigues
 */
public class FailTestsMetric extends AbstractMetric {

    /**
     * 
     * @param build
     * @param logger
     * @param graphiteLogger
     */
    public FailTestsMetric(AbstractBuild<?, ?> build, PrintStream logger, GraphiteLogger graphiteLogger, String baseQueueName) {
        super(build, logger, graphiteLogger, baseQueueName);
    }

    /**
     * 
     * @param server
     * @param metric
     * @throws UnknownHostException
     * @throws IOException
     */
    @Override
    public void sendMetric(Server server, Metric... metric) throws UnknownHostException, IOException {
        final AbstractTestResultAction<?> testResult = build.getAction(AbstractTestResultAction.class);

        if (testResult != null) {
            final String metricToSend = Integer.toString(testResult.getFailCount());

            sendMetric(server, metric[0], metricToSend);
        }
    }

}
