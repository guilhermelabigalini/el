curl -XDELETE http://localhost:9200/bustime

curl -XPOST http://localhost:9200/bustime

curl -XPOST http://localhost:9200/bustime/bus/_mapping -d @mapping.json