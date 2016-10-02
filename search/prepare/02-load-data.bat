curl -XPOST http://localhost:9200/_bulk --data-binary @02-load-data.json

pause 

curl http://localhost:9200/product/document/_count?pretty