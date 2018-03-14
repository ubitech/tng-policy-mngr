package eu.tng.policymanager.Messaging;

import eu.tng.policymanager.RulesEngineService;
import eu.tng.policymanager.facts.LogMetric;
import java.util.LinkedHashMap;
import java.util.logging.Level;

import org.springframework.stereotype.Component;

import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class MonitoringListener {

    private static final Logger logger = Logger.getLogger(MonitoringListener.class.getName());

    @Autowired
    RulesEngineService rulesEngineService;

    public void monitoringAlertReceived(LinkedHashMap message) {

        logger.log(Level.INFO, "monitoring alert   is like this " + message.toString());
        logger.log(Level.INFO, "alert name: " + message.get("alertname"));

        if (message.containsKey("alertname")) {
            LogMetric logMetric = new LogMetric("pilotTranscodingService", "vnf1", message.get("alertname").toString());
            rulesEngineService.createLogFact(logMetric);
        }

    }

}