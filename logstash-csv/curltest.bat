curl http://localhost:9200/twitter/tweet-2016.09.24/_count?pretty=true

pause

curl http://localhost:9200/twitter/tweet-2016.09.24/_mapping?pretty=true

pause

curl http://localhost:9200/twitter/_stats?pretty=true

curl http://localhost:9200/twitter/tweet-2016.09.24/_search?q=computing
