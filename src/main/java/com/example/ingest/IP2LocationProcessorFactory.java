package com.example.ingest;

import com.ip2location.IP2Location;

import org.elasticsearch.cluster.metadata.ProjectId;
import org.elasticsearch.ingest.ConfigurationUtils;
import org.elasticsearch.ingest.Processor;

import java.util.List;
import java.util.Map;

public class IP2LocationProcessorFactory implements Processor.Factory {

    @Override
    public Processor create(
        Map<String, Processor.Factory> registry,
        String processorTag,
        String description,
        Map<String, Object> config,
        ProjectId projectId
    ) throws Exception {
        String sourceField = ConfigurationUtils.readStringProperty(IP2LocationProcessor.TYPE, processorTag, config, "field");
        String targetField = ConfigurationUtils.readStringProperty(IP2LocationProcessor.TYPE, processorTag, config, "target_field", "geo");
        String databaseFile = ConfigurationUtils.readStringProperty(IP2LocationProcessor.TYPE, processorTag, config, "database_file", "");
        boolean ignoreMissing = ConfigurationUtils.readBooleanProperty(IP2LocationProcessor.TYPE, processorTag, config, "ignore_missing", true);
        boolean firstOnly = ConfigurationUtils.readBooleanProperty(IP2LocationProcessor.TYPE, processorTag, config, "first_only", true);
        List<String> fields = ConfigurationUtils.readOptionalList(IP2LocationProcessor.TYPE, processorTag, config, "fields");
        IP2Location database = new IP2Location();

        if (fields == null || fields.isEmpty()) {
            fields = List.of("country_code", "country_name", "region_name", "city_name", "isp", "latitude", "longitude", "location", "domain", "zip_code", "time_zone", "net_speed", "idd_code", "area_code", "weather_station_code", "weather_station_name", "mcc", "mnc", "mobile_brand", "elevation", "usage_type", "address_type", "category", "district", "asn", "as", "as_domain", "as_usage_type", "as_cidr");
        }

        database = IP2LocationService.getDatabase(databaseFile);

        return new IP2LocationProcessor(
            processorTag,
            description,
            sourceField,
            targetField,
            ignoreMissing,
            firstOnly,
            fields,
            database
        );
    }
}