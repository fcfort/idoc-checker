## IDOC Checker

### Running

    mvn clean compile assembly:single
    java -cp target/idoc-0.0.1-SNAPSHOT-jar-with-dependencies.jar uo.idoc.main.IdocCheckerMain -- \    
      --fileUrl "http://www.example.com/" \
      --recipient example@gmail.com \
      --imageUrl "http://www.example.com" \
      --imgurClientId deadbeef \
      --outputPath "~/"

The application will open a browser window for OAuth authentication upon
starting. In addition, a client secret for the GMail API from cloud.google.com
named "client_secret.json" needs to be placed in src/main/resources.

### Testing

Some integration tests require an imgur client ID. Place a file called
"imgur_client_id" in src/test/resources.