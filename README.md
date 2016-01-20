## IDOC Checker

### Running

    mvn clean compile assembly:single
    java -cp target/idoc-0.0.1-SNAPSHOT-jar-with-dependencies.jar uo.idoc.IdocCheckerMain -- \
        --username my.gmail --imageUrl "http://www.map.com/map/" --outputPath "/home/$USER/" --password --recipient me@gmail.com

The application will prompt for your GMail password on the console.
