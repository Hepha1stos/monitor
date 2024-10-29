package com.monitor.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DBManager {

    private Connection conn;
    private String dbPath;
    private static final Logger LOGGER = LogManager.getLogger(DBManager.class);

    /**
     * Constructor: Establishes a connection to the database and creates tables
     * if the database does not exist.
     */
    public DBManager() {
        try {
            initializeDBPath();

            String url = "jdbc:sqlite:" + getDBPath();

            initializeConnection(url);

            if (getConnection() != null) {
                LOGGER.info("Connection to database at {} established.", getDBPath());
            }
            createTables();
        } catch (SQLException e) {
            LOGGER.error("Error while connecting to the database:", e);
        }
    }

    /**
     * Retrieves the current database connection.
     *
     * @return Connection object
     */
    public Connection getConnection() {
        return this.conn;
    }

    /**
     * Sets a new database connection based on the provided URL.
     *
     * @param url JDBC URL of the database
     */
    public void setConnection(String url) {
        try {
            this.conn = DriverManager.getConnection(url);
            LOGGER.info("Database connection set to {}.", url);
        } catch (SQLException e) {
            LOGGER.error("Error while setting the database connection:", e);
        }
    }

    /**
     * Retrieves the path to the database.
     *
     * @return Path as a String
     */
    public String getDBPath() {
        return this.dbPath;
    }

    /**
     * Sets the path to the database file.
     *
     * @param projectRoot The root directory of the project
     */
    public void setDBPath(File projectRoot) {
        try {
            // Setzt den Datenbankpfad auf ein Verzeichnis im Projekt, z. B. "src/main/resources/database"
            File databaseDirectory = new File(projectRoot, "src/main/resources/database");
            if (!databaseDirectory.exists()) {
                databaseDirectory.mkdirs(); // Erstellt das Verzeichnis, falls es nicht existiert
            }
            this.dbPath = new File(databaseDirectory, "database.db").getAbsolutePath();
            LOGGER.info("Database path set to {}.", this.dbPath);
        } catch (Exception e) {
            LOGGER.error("Error while setting the database path:", e);
        }
    }

    /**
     * Creates the necessary tables if they do not exist.
     */
    private void createTables() {
        String createProcessTable = "CREATE TABLE IF NOT EXISTS process (\n"
                + "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "    pid INTEGER NOT NULL,\n"
                + "    name TEXT,\n"
                + "    command_line TEXT,\n"
                + "    start_time DATETIME\n"
                + ");";

        String createProcessMetricsTable = "CREATE TABLE IF NOT EXISTS process_metrics (\n"
                + "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "    process_id INTEGER NOT NULL,\n"
                + "    timestamp DATETIME NOT NULL,\n"
                + "    rss INTEGER NOT NULL,\n"
                + "    cpu_usage REAL NOT NULL,\n"
                + "    FOREIGN KEY (process_id) REFERENCES process(id)\n"
                + ");";

        try (Statement stmt = getConnection().createStatement()) {

            stmt.execute(createProcessTable);
            LOGGER.info("Table 'process' created or already exists.");

            stmt.execute(createProcessMetricsTable);
            LOGGER.info("Table 'process_metrics' created or already exists.");

        } catch (SQLException e) {
            LOGGER.error("Error while creating tables:", e);
        }
    }

    /**
     * Closes the database connection.
     */
    public void close() {
        if (getConnection() != null) {
            try {
                getConnection().close();
                LOGGER.info("Database connection closed.");
            } catch (SQLException e) {
                LOGGER.error("Error while closing the database connection:", e);
            }
        }
    }

    /**
     * Initializes the database path using a private method.
     */
    private void initializeDBPath() {
        // Setzt den Pfad relativ zum Projektverzeichnis
        try {
            // Der Datenbankpfad wird jetzt explizit auf ein festes Verzeichnis innerhalb des Projekts gesetzt
            String projectRootPath = System.getProperty("user.dir");
            File projectRoot = new File(projectRootPath);
            setDBPath(projectRoot);
        } catch (Exception e) {
            LOGGER.error("Error while resolving the project path:", e);
        }
    }

    /**
     * Initializes the database connection using a private method.
     *
     * @param url JDBC URL of the database
     * @throws SQLException if a database access error occurs
     */
    private void initializeConnection(String url) throws SQLException {
        this.conn = DriverManager.getConnection(url);
        LOGGER.info("Database connection initialized.");
    }

    /* Main methods */
    public void execute(String sql) {
        try {
            Statement stmt = this.getConnection().createStatement();
            stmt.execute(sql);
            this.getConnection().close();
            LOGGER.info("Executed query: {}", sql);
        } catch (SQLException e) {
            LOGGER.error("Error while trying to execute statement: {}", e);
        }
    }

}
