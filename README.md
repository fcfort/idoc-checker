## IDOC Checker

### Running

    mvn clean compile assembly:single
    java -cp target/idoc-0.0.1-SNAPSHOT-jar-with-dependencies.jar uo.idoc.main.IdocCheckerMain -- \    
      --fileUrl "http://www.example.com/" \
      --username example \
      --password \
      --recipient example@gmail.com \
      --imageUrl "http://www.example.com" \
      --imgurClientId deadbeef \
      --outputPath "~/"

The application will prompt for your GMail password on the console.
