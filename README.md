# Elasticsearch IP2Location Processor
This is IP2Location Processor for Elasticsearch Ingest Pipeline that enables Elasticsearch's users to get geolocation information such as country, region, district, city, latitude, longitude, ZIP code, time zone, Internet Service Provider (ISP), domain name, connection speed, IDD code, area code, weather station code, weather station name, mobile country code (MCC), mobile network code (MNC), mobile brand, elevation, usage type, address type, IAB category, ASN, AS domain, AS usage type and AS CIDR by IP address. The processor reads the geolocation information from **IP2Location BIN data** file.

Supported IPv4 and IPv6 address.

For deatiled usage of IP2Location Processor in Elasticsearch Ingest Pipeline, please take a look on this [tutorial](https://blog.ip2location.com/knowledge-base/enhance-elasticsearch-ingest-pipelines-with-the-ip2location-processor/).


## Dependencies (IP2LOCATION BIN DATA FILE)
This processor requires IP2Location BIN data file to function. You may download the BIN data file at
* IP2Location LITE BIN Data (Free): https://www.ip2location.com/database/lite
* IP2Location Commercial BIN Data (Commercial): https://www.ip2location.com/database/ip2location


## Installation
Install this processor by the following code:
```
/usr/share/elasticsearch/bin/elasticsearch-plugin install file://elasticsearch-ingest-ip2location.zip
```


## Pipeline Creation
```
curl -X PUT "http://localhost:9200/_ingest/pipeline/ip2location" -H "Content-Type: application/json" -d '
{
  "description" : "Pipeline for geolocation info using IP2Location processor",
  "processors" : [
    {
      "ip2location": {
        "field": "ip",
        "target_field": "geo",
        "database_file": "/usr/share/elasticsearch/plugins/ip2location/DB.BIN",
        "fields": [
          "country_code",
          "country_name",
          "region_name",
          "city_name",
          "location",
          "isp",
          "asn"
        ]
      }
    }
  ]
}'
```


## Document Insertion
```
curl -X PUT "http://localhost:9200/index-1/_doc/my_id?pipeline=ip2location" -H "Content-Type: application/json" -d '
{
  "ip": "8.8.8.8"
}'
```


## Document Retrieval
```
curl -X GET "http://localhost:9200/index-1/_doc/my_id" -H "Content-Type: application/json"
```


## IP2Location Processor Configuration
|Setting|Required|Description|
|---|---|---|
|field|Yes|The field used to retrieve the IP address for geolocation lookup.|
|database_file|Yes|The path where the IP2Location BIN database file located.|
|target_field|No|The field that stores the geographic information retrieved from the database. Default is `geo`.|
|ignore_missing|No|The field that used to check if the field does not exist, the processor exits quietly without modifying the document. Default is `false`.|
|first_only|No|The field that check first found ip geolocation data if the `field` setting contains array. Default is `true`.|
|fields|No|The field used to define which fields should be included in the database query results. Available fields are "country_code", "country_name", "region_name", "city_name", "isp", "latitude", "longitude", "location", "domain", "zip_code", "time_zone", "net_speed", "idd_code", "area_code", "weather_station_code", "weather_station_name", "mcc", "mnc", "mobile_brand", "elevation", "usage_type", "address_type", "category", "district", "asn", "as", "as_domain", "as_usage_type", "as_cidr".|


## Sample Output
|Field|Description|
|---|---|
|address_type|the IP address type (A-Anycast, B-Broadcast, M-Multicast & U-Unicast) of IP address or domain name|
|area_code|the varying length number assigned to geographic areas for call between cities|
|as|Autonomous system (AS) name|
|asn|the Autonomous system number (ASN)|
|as_cidr|CIDR range for the whole AS|
|as_domain|Domain name of the AS registrant|
|as_usage_type|Usage type of the AS registrant|
|category|the IAB content taxonomy category of IP address or domain name|
|city_name|the city name|
|country_code|the two-character country code based on ISO 3166|
|country_name|the country name based on ISO 3166|
|district|the district or county name|
|domain|the Internet domain name associated to IP address range|
|elevation|the elevation|
|idd_code|the IDD prefix to call the city from another country|
|ip_address|the IP address|
|isp|the Internet Service Provider (ISP) name|
|latitude|the city latitude|
|location|the city location|
|longitude|the city longitude|
|mcc|the mobile country code|
|mnc|mobile network code|
|mobile_brand|the mobile brand|
|net_speed|the Internet Connection Speed (DIAL) DIAL-UP,(DSL) DSL/CABLE or(COMP) COMPANY|
|region_name|the region or state name|
|time_zone|the Time zone in UTC (Coordinated Universal Time)|
|usage_type|the usage type|
|weather_station_code|the special code to identify the nearest weather observation station|
|weather_station_name|the name of the nearest weather observation station|
|zip_code|the ZIP code|


## Support
Email: support@ip2location.com  
URL: [https://www.ip2location.com](https://www.ip2location.com)
