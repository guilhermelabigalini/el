set logstash=D:\Dev\elasticsearch\logstash-2.4.0\bin\logstash.bat

rem %logstash% -f apache-pipeline.conf --configtest

%logstash% -f apache-pipeline.conf