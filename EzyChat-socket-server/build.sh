#export EZYFOX_SERVER_HOME=
mvn -pl . clean install
mvn -pl EzyChat-common -Pexport clean install
mvn -pl EzyChat-app-api -Pexport clean install
mvn -pl EzyChat-app-entry -Pexport clean install
mvn -pl EzyChat-plugin -Pexport clean install
cp EzyChat-zone-settings.xml $EZYFOX_SERVER_HOME/settings/zones/
