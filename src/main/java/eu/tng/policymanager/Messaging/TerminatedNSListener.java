/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.tng.policymanager.Messaging;

import eu.tng.policymanager.RulesEngineApp;
import eu.tng.policymanager.RulesEngineService;
import eu.tng.policymanager.rules.generation.Util;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.logging.Level;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author eleni
 */
@Component
public class TerminatedNSListener {

    @Autowired
    LogsFormat logsFormat;

    @Autowired
    RulesEngineService rulesEngineService;

    @RabbitListener(queues = RulesEngineApp.NS_TERMINATION_QUEUE)
    public void terminatedNSMessageReceived(byte[] message) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        try {
            String terminatedNSasYaml = new String(message, StandardCharsets.UTF_8);
            String jsonobject_as_string = Util.convertYamlToJson(terminatedNSasYaml);
            logsFormat.createLogInfo("I", timestamp.toString(), "NS Termination Message received", jsonobject_as_string, "200");

            JSONObject jsonobject = new JSONObject(jsonobject_as_string);
            String ns_uuid = jsonobject.getString("serv_id");
            rulesEngineService.removeKnowledgebase(ns_uuid);
        } catch (Exception e) {
            logsFormat.createLogInfo("E", timestamp.toString(), "Ignoring message from NS_TERMINATION_QUEUE", e.getMessage(), "200");
        }

    }

}
