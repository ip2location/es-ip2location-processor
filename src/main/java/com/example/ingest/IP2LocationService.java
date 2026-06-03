package com.example.ingest;

import com.ip2location.IP2Location;

import java.util.concurrent.ConcurrentHashMap;

public class IP2LocationService {

    private static final ConcurrentHashMap<String, IP2Location> DATABASES = new ConcurrentHashMap<>();

    public static IP2Location getDatabase(String databasePath) throws Exception {
        return DATABASES.computeIfAbsent(
                databasePath,
                path -> {
                    try {
                        IP2Location db = new IP2Location();
                        db.Open(path, true);
                        return db;
                    } catch (Exception e) {
                        throw new RuntimeException("Failed loading IP2Location BIN: " + path, e);
                    }
                }
        );
    }
}