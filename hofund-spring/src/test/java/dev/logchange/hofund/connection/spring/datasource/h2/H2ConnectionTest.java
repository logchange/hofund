package dev.logchange.hofund.connection.spring.datasource.h2;

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
class H2ConnectionTest {

    @Mock
    private DatabaseMetaData databaseMetaData;

    @Mock
    private DataSource dataSource;


    @Test
    void givenConnectionUrl_whenGetTarget_databaseNameReturned() throws SQLException {
        //given:
        String url = "spring.datasource.url=jdbc:h2:mem:17ebc6e8-e833-4157-b8e2-35f113eb404a";
        String productName = "H2";
        when(databaseMetaData.getURL()).thenReturn(url);
        when(databaseMetaData.getDatabaseProductName()).thenReturn(productName);

        //when:
        H2Connection h2Connection = new H2Connection(databaseMetaData, dataSource);
        String resultTarget = h2Connection.getTarget();
        String resultVendor = h2Connection.getDbVendor();

        //then:
        assertThat(resultTarget).isEqualTo("17ebc6e8-e833-4157-b8e2-35f113eb404a");
        assertThat(resultVendor).isEqualTo(productName);
    }
}
