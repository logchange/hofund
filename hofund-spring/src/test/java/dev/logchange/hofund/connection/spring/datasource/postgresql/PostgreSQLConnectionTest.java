package dev.logchange.hofund.connection.spring.datasource.postgresql;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostgreSQLConnectionTest {

    @Mock
    private DatabaseMetaData databaseMetaData;

    @Mock
    private DataSource dataSource;

    @Test
    void givenConnectionUrl_whenGetTarget_databaseNameReturned() throws SQLException {
        //given:
        String url = "spring.datasource.url=jdbc:postgresql://localhost:5432/cart";
        String productName = "PostgreSQL";
        when(databaseMetaData.getURL()).thenReturn(url);
        when(databaseMetaData.getDatabaseProductName()).thenReturn(productName);

        //when:
        PostgreSQLConnection postgreSQLConnection = new PostgreSQLConnection(databaseMetaData, dataSource);
        String resultTarget = postgreSQLConnection.getTarget();
        String resultVendor = postgreSQLConnection.getDbVendor();

        //then:
        assertThat(resultTarget).isEqualTo("cart");
        assertThat(resultVendor).isEqualTo(productName);
    }

    @Test
    void givenConnectionUrlWithEncoding_whenGetTarget_databaseNameReturned() throws SQLException {
        //given:
        String url = "spring.datasource.url=jdbc:postgresql://localhost:5432/cart?encoding=UTF-8";
        String productName = "PostgreSQL";
        when(databaseMetaData.getURL()).thenReturn(url);
        when(databaseMetaData.getDatabaseProductName()).thenReturn(productName);

        //when:
        PostgreSQLConnection postgreSQLConnection = new PostgreSQLConnection(databaseMetaData, dataSource);
        String resultTarget = postgreSQLConnection.getTarget();
        String resultVendor = postgreSQLConnection.getDbVendor();

        //then:
        assertThat(resultTarget).isEqualTo("cart");
        assertThat(resultVendor).isEqualTo(productName);
    }

}