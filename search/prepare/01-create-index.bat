curl -XDELETE http://localhost:9200/product

curl -XPUT http://localhost:9200/product -d @01-create-index-mapping.json

curl http://localhost:9200/_cat/indices