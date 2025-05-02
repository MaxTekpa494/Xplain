package fr.uge.Xplain;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import io.quarkus.runtime.annotations.RegisterForReflection;

@QuarkusMain
@RegisterForReflection
public class Main {
    public static void main(String[] args) {
        System.out.println("Lancement de l'application...");
        Quarkus.run(args);
    }
}