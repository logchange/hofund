package dev.logchange.hofund.connection.spring.datasource.postgresql;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostgreSQLConnectionTest {

    @Mock
    private DatabaseMetaData databaseMetaData;

    @InjectMocks
    private PostgreSQLConnection sut;

    @Test
    void givenConnectionUrl_whenGetTarget_databaseNameReturned() throws SQLException {
        //given:
        String url = "spring.datasource.url=jdbc:postgresql://localhost:5432/cart";
        when(databaseMetaData.getURL()).thenReturn(url);

        //when:
        String result = sut.getTarget();

        //then:
        assertThat(result).isEqualTo("cart");
    }

    @Test
    void givenConnectionUrlWithEncoding_whenGetTarget_databaseNameReturned() throws SQLException {
        //given:
        String url = "spring.datasource.url=jdbc:postgresql://localhost:5432/cart?encoding=UTF-8";
        when(databaseMetaData.getURL()).thenReturn(url);

        //when:
        String result = sut.getTarget();

        //then:
        assertThat(result).isEqualTo("cart");
    }

}