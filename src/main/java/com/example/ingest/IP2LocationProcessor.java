package com.example.ingest;

import com.ip2location.IP2Location;
import com.ip2location.IPResult;

import org.elasticsearch.ingest.AbstractProcessor;
import org.elasticsearch.ingest.IngestDocument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IP2LocationProcessor extends AbstractProcessor {

    public static final String TYPE = "ip2location";

    private final String sourceField;
    private final String targetField;
    private final boolean ignoreMissing;
    private final boolean firstOnly;
    private final List<String> fields;
    private final IP2Location database;

    public IP2LocationProcessor(
        String tag,
        String description,
        String sourceField,
        String targetField,
        boolean ignoreMissing,
        boolean firstOnly,
        List<String> fields,
        IP2Location database
    ) {
        super(tag, description);
        this.sourceField = sourceField;
        this.targetField = targetField;
        this.ignoreMissing = ignoreMissing;
        this.firstOnly = firstOnly;
        this.fields = fields;
        this.database = database;
    }

    @Override
    public IngestDocument execute(IngestDocument document) throws Exception {
        if (!document.hasField(sourceField)) {
            if (ignoreMissing) {
                return document;
            }

            throw new IllegalArgumentException(
                "Missing field: " + sourceField
            );
        }

        Object value = document.getFieldValue(sourceField, Object.class);

        if (value == null) {
            if (ignoreMissing) {
                return document;
            }

            throw new IllegalArgumentException("Null field: " + sourceField);
        }

        List<String> ips;

        if (value instanceof String) {
            ips = List.of((String) value);
        } else if (value instanceof List<?>) {
            ips = ((List<?>) value)
                .stream()
                .filter(v -> v != null)
                .map(Object::toString)
                .toList();
        } else {
            throw new IllegalArgumentException("Field must be string or array: " + sourceField);
        }

        List<Map<String, Object>> results = new ArrayList<>();

        for (String ip : ips) {
            if (ip == null || ip.isBlank()) {
                continue;
            }

            Map<String, Object> geoData;

            try {
                IPResult result = database.IPQuery(ip);
                if (!"OK".equals(result.getStatus())) {
                    continue;
                }
                geoData = buildGeoData(result);
                results.add(geoData);
            } catch (Exception e) {
                continue;
            }

            if (firstOnly) {
                break;
            }
        }

        if (results.isEmpty()) {
            return document;
        }

        if (firstOnly) {
            document.setFieldValue(targetField, results.get(0));
        } else {
            document.setFieldValue(targetField, results);
        }

        return document;
    }

    private Map<String, Object> buildGeoData(IPResult result) {
        Map<String, Object> geoData = new HashMap<>();

        for (String field : fields) {
            switch (field) {
                case "country_code":
                    geoData.put("country_code", result.getCountryShort());
                    break;

                case "country_name":
                    geoData.put("country_name", result.getCountryLong());
                    break;

                case "region_name":
                    geoData.put("region_name", result.getRegion());
                    break;

                case "city_name":
                    geoData.put("city_name", result.getCity());
                    break;

                case "isp":
                    geoData.put("isp", result.getISP());
                    break;

                case "latitude":
                    geoData.put("latitude", result.getLatitude());
                    break;

                case "longitude":
                    geoData.put("longitude", result.getLongitude());
                    break;

                case "location":
                    Map<String, Object> location = new HashMap<>();
                    location.put("lat", result.getLatitude());
                    location.put("lon", result.getLongitude());
                    geoData.put("location", location);
                    break;

                case "domain":
                    geoData.put("domain", result.getDomain());
                    break;

                case "zip_code":
                    geoData.put("zip_code", result.getZipCode());
                    break;

                case "time_zone":
                    geoData.put("time_zone", result.getTimeZone());
                    break;

                case "net_speed":
                    geoData.put("net_speed", result.getNetSpeed());
                    break;

                case "idd_code":
                    geoData.put("idd_code", result.getIDDCode());
                    break;

                case "area_code":
                    geoData.put("area_code", result.getAreaCode());
                    break;

                case "weather_station_code":
                    geoData.put("weather_station_code", result.getWeatherStationCode());
                    break;

                case "weather_station_name":
                    geoData.put("weather_station_name", result.getWeatherStationName());
                    break;

                case "mcc":
                    geoData.put("mcc", result.getMCC());
                    break;

                case "mnc":
                    geoData.put("mnc", result.getMNC());
                    break;

                case "mobile_brand":
                    geoData.put("mobile_brand", result.getMobileBrand());
                    break;

                case "elevation":
                    geoData.put("elevation", result.getElevation());
                    break;

                case "usage_type":
                    geoData.put("usage_type", result.getUsageType());
                    break;

                case "address_type":
                    geoData.put("address_type", result.getAddressType());
                    break;

                case "category":
                    geoData.put("category", result.getCategory());
                    break;

                case "district":
                    geoData.put("district", result.getDistrict());
                    break;

                case "asn":
                    geoData.put("asn", result.getASN());
                    break;

                case "as":
                    geoData.put("as", result.getAS());
                    break;

                case "as_domain":
                    geoData.put("as_domain", result.getASDomain());
                    break;

                case "as_usage_type":
                    geoData.put("as_usage_type", result.getASUsageType());
                    break;

                case "as_cidr":
                    geoData.put("as_cidr", result.getASCIDR());
                    break;
            }
        }
        return geoData;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}