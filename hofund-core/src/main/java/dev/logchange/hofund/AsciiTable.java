package dev.logchange.hofund;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AsciiTable {

    private final List<String> headers;
    private final List<List<String>> rows;

    public AsciiTable(List<String> headers) {
        this.headers = headers;
        this.rows = new ArrayList<>();
    }


    public void addRow(String... columns) {
        if (columns.length != headers.size()) {
            throw new IllegalArgumentException("Number of columns must match number of headers");
        }
        rows.add(Arrays.asList(columns));
    }

    public String printTable() {
        StringBuilder table = new StringBuilder();
        int[] columnWidths = new int[headers.size()];

        // Calculate the width of each column
        for (int i = 0; i < headers.size(); i++) {
            columnWidths[i] = headers.get(i).length();
        }

        for (List<String> row : rows) {
            for (int i = 0; i < row.size(); i++) {
                columnWidths[i] = Math.max(columnWidths[i], row.get(i).length());
            }
        }

        // Append the headers
        table.append(printSeparator(columnWidths));
        table.append(printRow(headers, columnWidths));
        table.append(printSeparator(columnWidths));

        // Append the rows
        for (List<String> row : rows) {
            table.append(printRow(row, columnWidths));
        }
        table.append(printSeparator(columnWidths));

        return table.toString();
    }

    private String printRow(List<String> row, int[] columnWidths) {
        StringBuilder rowString = new StringBuilder();
        rowString.append("| ");
        for (int i = 0; i < row.size(); i++) {
            if (i == row.size() - 1) {
                rowString.append(padRight(row.get(i), columnWidths[i])).append(" |");
            } else {
                rowString.append(padRight(row.get(i), columnWidths[i])).append(" | ");
            }
        }
        rowString.append("\n");
        return rowString.toString();
    }

    private String printSeparator(int[] columnWidths) {
        StringBuilder separator = new StringBuilder();
        separator.append("+");
        for (int width : columnWidths) {
            separator.append(repeat("-", width + 2)).append("+");
        }
        separator.append("\n");
        return separator.toString();
    }

    private String padRight(String text, int length) {
        return String.format("%-" + length + "s", text);
    }

    private String repeat(String str, int times) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < times; i++) {
            result.append(str);
        }
        return result.toString();
    }

}
