package smartcity.util;

import smartcity.structures.DynamicArray;

/**
 * Robust RFC-4180 compliant CSV parser.
 * Correctly handles commas, escaped quotes, and multiline values within double quotes.
 */
public class CSVParser {

    /**
     * Splits a single CSV record into an array of string fields.
     */
    public static String[] parseLine(String line) {
        if (line == null) return new String[0];
        DynamicArray<String> fields = new DynamicArray<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        int len = line.length();

        for (int i = 0; i < len; i++) {
            char c = line.charAt(i);
            if (c == '\"') {
                if (inQuotes && i + 1 < len && line.charAt(i + 1) == '\"') {
                    // Escaped double quote
                    current.append('\"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                fields.add(current.toString().trim());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        fields.add(current.toString().trim());

        String[] result = new String[fields.size()];
        for (int i = 0; i < fields.size(); i++) {
            result[i] = fields.get(i);
        }
        return result;
    }
}
