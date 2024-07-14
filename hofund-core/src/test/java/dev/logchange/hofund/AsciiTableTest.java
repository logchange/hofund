package dev.logchange.hofund;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AsciiTableTest {

    @Test
    public void testEmptyTable() {
        AsciiTable tablePrinter = new AsciiTable(Arrays.asList("TYPE", "NAME", "STATUS", "URL"));
        String expected =
                "+------+------+--------+-----+\n" +
                        "| TYPE | NAME | STATUS | URL |\n" +
                        "+------+------+--------+-----+\n" +
                        "+------+------+--------+-----+\n";
        assertEquals(expected, tablePrinter.printTable());
    }

    @Test
    public void testSingleRow() {
        AsciiTable tablePrinter = new AsciiTable(Arrays.asList("TYPE", "NAME", "STATUS", "URL"));
        tablePrinter.addRow("Type1", "Name1", "Status1", "http://url1.com");
        String expected =
                "+-------+-------+---------+-----------------+\n" +
                        "| TYPE  | NAME  | STATUS  | URL             |\n" +
                        "+-------+-------+---------+-----------------+\n" +
                        "| Type1 | Name1 | Status1 | http://url1.com |\n" +
                        "+-------+-------+---------+-----------------+\n";
        assertEquals(expected, tablePrinter.printTable());
    }

    @Test
    public void testMultipleRows() {
        AsciiTable tablePrinter = new AsciiTable(Arrays.asList("TYPE", "NAME", "STATUS", "URL"));
        tablePrinter.addRow("Type1", "Name1", "Status1", "http://url1.com");
        tablePrinter.addRow("Type2", "Name2", "Status2", "http://url2.com");
        tablePrinter.addRow("Type3", "Name3", "Status3", "http://url3.com");
        String expected =
                "+-------+-------+---------+-----------------+\n" +
                        "| TYPE  | NAME  | STATUS  | URL             |\n" +
                        "+-------+-------+---------+-----------------+\n" +
                        "| Type1 | Name1 | Status1 | http://url1.com |\n" +
                        "| Type2 | Name2 | Status2 | http://url2.com |\n" +
                        "| Type3 | Name3 | Status3 | http://url3.com |\n" +
                        "+-------+-------+---------+-----------------+\n";
        assertEquals(expected, tablePrinter.printTable());
    }

    @Test
    public void testAddRowWithInvalidColumnCount() {
        AsciiTable tablePrinter = new AsciiTable(Arrays.asList("TYPE", "NAME", "STATUS", "URL"));
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            tablePrinter.addRow("Type1", "Name1", "Status1"); // Missing one column
        });
        assertEquals("Number of columns must match number of headers", thrown.getMessage());
    }
}
