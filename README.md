## IDOC Checker

This tool monitors a particular resource on the UO:R website, `map/house.txt`, which holds the location of all houses on the server, including their type (e.g. villa, keep, castle, etc.). By monitoring this list of houses, we can determine after each day, what houses have fallen (i.e "IDOCs") or been built since the previous day.

Every day around 4:30 AM New York time, the server updates the resource above with the current set of houses on the server. By watching this URL for changes, we can detect when this update happens and produce some interesting results.

When the tool detects that the house list has changed, it takes a screen capture of the current map, and produces an overlay highlighting all the houses that have been built and fallen since the previous update. It then uploads that image in four pieces (to avoid imgur file size limits), creates a gallery and emails that gallery to the recipient email addresses provided to the application.

The application sends email via the OAuth2 authenticated GMail API using the client secret provided to the application. It uploads to imgur anonymously via the client ID provided.

### Running

    mvn clean compile assembly:single
    java -cp target/idoc-0.0.1-SNAPSHOT-jar-with-dependencies.jar uo.idoc.main.FileDiffMain -- \    
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
