set MAVEN_OPTS=-Xmx1024m -XX:MaxPermSize=256m
mvn clean jetty:run -Pdev,mysql