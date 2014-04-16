package org.jenkinsci.plugins.graphiteIntegrator.metrics;

import hudson.model.Result;
import hudson.model.AbstractBuild;

import java.io.IOException;
import java.io.PrintStream;
import java.net.UnknownHostException;

import org.jenkinsci.plugins.graphiteIntegrator.loggers.GraphiteLogger;
import org.jenkinsci.plugins.graphiteIntegrator.Metric;
import org.jenkinsci.plugins.graphiteIntegrator.Server;

/**
 * 
 * @author Josh Sinfield
 */
public class BuildSuccessfulMetric extends AbstractMetric {

    /**
     * 
     * @param build
     * @param logger
     * @param graphiteLogger
     */
    public BuildSuccessfulMetric(AbstractBuild<?, ?> build, PrintStream logger, GraphiteLogger graphiteLogger) {
        super(build, logger, graphiteLogger);
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
         
         if(build.getResult() == Result.SUCCESS){
             String metricToSend = String.valueOf(1);
             sendMetric(server, metric[0], metricToSend);
         }
         
         
     }
 
 }