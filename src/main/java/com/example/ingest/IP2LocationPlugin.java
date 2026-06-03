package com.example.ingest;

import org.elasticsearch.ingest.Processor;
import org.elasticsearch.plugins.IngestPlugin;
import org.elasticsearch.plugins.Plugin;

import java.util.Map;

public class IP2LocationPlugin extends Plugin implements IngestPlugin {

    @Override
    public Map<String, Processor.Factory>
    getProcessors(Processor.Parameters parameters) {
        return Map.of(
            IP2LocationProcessor.TYPE,
            new IP2LocationProcessorFactory()
        );
    }
}