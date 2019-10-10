package db;//import com.github.shimopus.revolutmoneyexchange.exceptions.ImpossibleOperationExecution;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;


public class DataSource {

    private static final Logger log = LogManager.getLogger(DataSource.class);
    private static final HikariDataSource ds=new HikariDataSource();
    static {

        ds.setJdbcUrl("jdbc:h2:mem:test;" +
                "INIT=RUNSCRIPT FROM 'classpath:storedProcedure/schema.sql'\\;RUNSCRIPT FROM 'classpath:storedProcedure/init_data.sql';" +
                "TRACE_LEVEL_FILE=4");
        ds.setUsername("sa");
        ds.setPassword("sa");
        ds.setAutoCommit(false);
        log.info("database initialized");
    }

    private DataSource() {}

    public static Connection getConnection() throws Exception{
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            throw new Exception(e);
        }
    }
}
