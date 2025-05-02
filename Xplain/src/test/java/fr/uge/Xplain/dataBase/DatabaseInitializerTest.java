package fr.uge.Xplain.dataBase;

import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import javax.sql.DataSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class DatabaseInitializerTest {
    @Inject
    DataSource dataSource;

    @Inject
    DatabaseInitializer databaseInitializer;

    @BeforeEach
    public void setUp() {
        databaseInitializer.init();
    }

    @Test
    public void initCreatesTablesTest() throws Exception {
        try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()) {

            // Vérifie l'existence de la table 'conversations'
            ResultSet resultSet = statement
                    .executeQuery("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'CONVERSATIONS'");
            assertTrue(resultSet.next(), "La table 'CONVERSATIONS' devrait exister");

            // Vérifie l'existence de la table 'messages'
            resultSet = statement.executeQuery("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'MESSAGES'");
            assertTrue(resultSet.next(), "La table 'MESSAGES' devrait exister");

            resultSet = statement.executeQuery("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'INNEXISTANTE'");
            assertFalse(resultSet.next(), "La table 'INNEXISTANTE' ne devrait pas exister");
        }
    }

        @Test
    public void printTableStatusTest() throws Exception {
        // Capture la sortie standard
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // Appelle la méthode à tester
        databaseInitializer.printTableStatus();

        // Restaure la sortie standard
        System.setOut(originalOut);

        // Vérifie la sortie
        String output = outputStream.toString();
        assertTrue(output.contains("Tables existantes"));
        assertTrue(output.contains("CONVERSATIONS")); 
        assertTrue(output.contains("MESSAGES")); 
        assertFalse(output.contains("INNEXISTANTE")); 
    }
}
