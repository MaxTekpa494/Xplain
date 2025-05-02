Préparer sa machine
	Cinq conditions doivent être réunies pour pouvoir lancer notre projet (en plus des triviales comme “posséder un ordinateur”):
Disposer de GraalVM, version 23+, sur sa machine
Disposer de Maven, version 3.9+, sur sa machine
Disposer de NodeJs (22+) et de la commande npm (11+)
Être en mesure de lancer des commandes depuis une invite de commandes (Windows) ou un terminal (Linux)
Disposer d’un système d’exploitation Linux x86_64, MacOS x86_64 ou Windows x86_64.
	Afin de passer à l’étape suivante, il vous faut ouvrir votre logiciel lanceur de commandes et vous placer dans le répertoire Xplain de l’application. Normalement, vous devriez voir, en lançant la commande ls, au moins

mvnw    pom.xml    src


La commande java -version devrait afficher quelque chose similaire à

java version "23.0.1" 2024-10-15
Java(TM) SE Runtime Environment Oracle GraalVM 23.0.1+11.1 (build 23.0.1+11-jvmci-b01)
Java HotSpot(TM) 64-Bit Server VM Oracle GraalVM 23.0.1+11.1 (build 23.0.1+11-jvmci-b01, mixed mode, sharing)


Et la commande mvn -v devrait afficher quelque chose comme

Apache Maven 3.9.9 (8e8579a9e76f7d015ee5ec7bfcdc97d260186937)
Maven home: C:\your\maven\home\apache-maven-3.9.9-bin\apache-maven-3.9.9


Monter l’application
	Monter l’application se fait à la condition d’avoir respecté la préparation indiquée dans la partie précédente. Pour monter l’application, il vous suffit de lancer la commande suivante:

./mvnw package 


N’hésitez pas à lire la documentation Maven pour connaître les autres options qui peuvent être ajoutées à la précédente commande.
Lancer l’application
	Si le montage s’est produit sans problème, vous devriez pouvoir lancer le serveur Quarkus, et donc l’application, avec la commande suivante:

java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED -jar target/Xplain-runner.jar


	Vous êtes désormais en mesure de vous connecter au serveur en ouvrant votre navigateur et allant à l’url http://0.0.0.0:8080. Vous pouvez désormais naviguer dans l’application et tester les différents cas d’utilisation listés ci-dessous, ils sont triés par ordre d’importance.

