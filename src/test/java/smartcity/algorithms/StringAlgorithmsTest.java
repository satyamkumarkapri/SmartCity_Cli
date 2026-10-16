package smartcity.algorithms;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import smartcity.algorithms.strings.*;
import smartcity.algorithms.suffix.Kasai;
import smartcity.algorithms.suffix.SuffixArray;
import smartcity.algorithms.suffix.SuffixAutomaton;
import smartcity.structures.CustomHashMap;
import smartcity.structures.IntArrayList;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Module 2: String & Suffix Algorithms Tests")
public class StringAlgorithmsTest {

    @Test
    @DisplayName("Exact Search: Edge cases (empty string, empty pattern, pattern > text)")
    public void testExactSearchEdgeCases() {
        assertEquals(0, NaiveSearch.search("", "pat").size());
        assertEquals(0, NaiveSearch.search("text", "").size());
        assertEquals(0, NaiveSearch.search("short", "longer_pattern").size());

        assertEquals(0, KMP.search("", "pat").size());
        assertEquals(0, KMP.search("text", "").size());
        assertEquals(0, KMP.search("short", "longer_pattern").size());

        assertEquals(0, ZAlgorithm.search("", "pat").size());
        assertEquals(0, ZAlgorithm.search("text", "").size());
        assertEquals(0, ZAlgorithm.search("short", "longer_pattern").size());

        assertEquals(0, RabinKarp.search("", "pat").size());
        assertEquals(0, RabinKarp.search("text", "").size());
        assertEquals(0, RabinKarp.search("short", "longer_pattern").size());
    }

    @Test
    @DisplayName("Exact Search: Overlapping matches and repeated characters")
    public void testOverlappingMatches() {
        String text = "AAAAA";
        String pattern = "AAA"; // Should match at 0, 1, 2

        IntArrayList naive = NaiveSearch.search(text, pattern);
        IntArrayList kmp = KMP.search(text, pattern);
        IntArrayList z = ZAlgorithm.search(text, pattern);
        IntArrayList rk = RabinKarp.search(text, pattern);

        assertEquals(3, naive.size());
        assertEquals(3, kmp.size());
        assertEquals(3, z.size());
        assertEquals(3, rk.size());

        for (int i = 0; i < 3; i++) {
            assertEquals(i, naive.get(i));
            assertEquals(i, kmp.get(i));
            assertEquals(i, z.get(i));
            assertEquals(i, rk.get(i));
        }
    }

    @Test
    @DisplayName("Exact Search: Unicode and special punctuation characters")
    public void testUnicodeSearch() {
        String text = "Algorithms & Data Structures: Алгоритм — 算法 100%!";
        String pattern = "Алгоритм";

        IntArrayList naive = NaiveSearch.search(text, pattern);
        IntArrayList kmp = KMP.search(text, pattern);
        IntArrayList z = ZAlgorithm.search(text, pattern);
        IntArrayList rk = RabinKarp.search(text, pattern);

        assertEquals(1, naive.size());
        assertEquals(1, kmp.size());
        assertEquals(1, z.size());
        assertEquals(1, rk.size());
        assertEquals(naive.get(0), kmp.get(0));
    }

    @Test
    @DisplayName("Randomized Cross-Verification between Naive, KMP, Z, and Rabin-Karp")
    public void testRandomizedCrossVerification() {
        Random rng = new Random(42);
        char[] alphabet = {'a', 'b', 'c'};

        for (int trial = 0; trial < 100; trial++) {
            int tLen = 20 + rng.nextInt(60);
            int pLen = 1 + rng.nextInt(6);

            StringBuilder tSb = new StringBuilder();
            for (int i = 0; i < tLen; i++) tSb.append(alphabet[rng.nextInt(alphabet.length)]);
            String text = tSb.toString();

            StringBuilder pSb = new StringBuilder();
            for (int i = 0; i < pLen; i++) pSb.append(alphabet[rng.nextInt(alphabet.length)]);
            String pattern = pSb.toString();

            IntArrayList naive = NaiveSearch.search(text, pattern);
            IntArrayList kmp = KMP.search(text, pattern);
            IntArrayList z = ZAlgorithm.search(text, pattern);
            IntArrayList rk = RabinKarp.search(text, pattern);

            assertEquals(naive.size(), kmp.size(), "KMP match count mismatch");
            assertEquals(kmp.size(), z.size(), "Z-Algorithm match count mismatch");
            assertEquals(z.size(), rk.size(), "Rabin-Karp match count mismatch");

            for (int i = 0; i < naive.size(); i++) {
                assertEquals(naive.get(i), kmp.get(i));
                assertEquals(kmp.get(i), z.get(i));
                assertEquals(z.get(i), rk.get(i));
            }
        }
    }

    @Test
    @DisplayName("Aho-Corasick Multi-Pattern Search with duplicates and overlaps")
    public void testAhoCorasick() {
        AhoCorasick ac = new AhoCorasick();
        ac.addPattern("he");
        ac.addPattern("she");
        ac.addPattern("his");
        ac.addPattern("hers");
        ac.buildFailureLinks();

        CustomHashMap<String, IntArrayList> res = ac.search("ushers");
        assertTrue(res.get("she").contains(1));
        assertTrue(res.get("he").contains(2));
        assertTrue(res.get("hers").contains(2));
        assertEquals(0, res.get("his").size());
    }

    @Test
    @DisplayName("Suffix Array, Kasai LCP and Binary Search")
    public void testSuffixArrayAndKasai() {
        String text = "banana";
        int[] sa = SuffixArray.buildSuffixArray(text);
        // Suffixes of "banana":
        // 5: a
        // 3: ana
        // 1: anana
        // 0: banana
        // 4: na
        // 2: nana
        int[] expectedSA = {5, 3, 1, 0, 4, 2};
        assertArrayEquals(expectedSA, sa);

        int[] lcp = Kasai.buildLCP(text, sa);
        int[] expectedLCP = {1, 3, 0, 0, 2, 0};
        assertArrayEquals(expectedLCP, lcp);

        IntArrayList anMatches = SuffixArray.search(text, "an", sa);
        assertEquals(2, anMatches.size()); // "ana" (pos 3) and "anana" (pos 1)
        assertTrue(anMatches.contains(1));
        assertTrue(anMatches.contains(3));

        // Test SA-IS linear construction
        int[] saLinear = SuffixArray.buildSAIS(text);
        assertArrayEquals(expectedSA, saLinear);
    }

    @Test
    @DisplayName("Suffix Array SA-IS O(N) Cross-Validation with Prefix Doubling")
    public void testSAISCrossValidation() {
        String[] samples = {
            "banana", "abracadabra", "mississippi", "smartcity",
            "MUNICIPAL WATER PIPELINE REGULATORY STANDARD",
            "POWER OUTAGE REPORTED IN DISTRICT 4 DOWNTOWN SUBSTATION"
        };
        for (String sample : samples) {
            int[] saPrefixDoubling = SuffixArray.buildSuffixArray(sample);
            int[] saIS = SuffixArray.buildSAIS(sample);
            assertArrayEquals(saPrefixDoubling, saIS, "Mismatch for string: " + sample);
        }
    }

    @Test
    @DisplayName("Suffix Automaton (DAWG): Substring search and distinct substrings")
    public void testSuffixAutomaton() {
        String text = "ababa";
        SuffixAutomaton sam = new SuffixAutomaton(text);

        assertTrue(sam.containsSubstring("aba"));
        assertTrue(sam.containsSubstring("bab"));
        assertTrue(sam.containsSubstring("ababa"));
        assertFalse(sam.containsSubstring("abb"));
        assertFalse(sam.containsSubstring("c"));

        // Distinct substrings of "ababa":
        // Length 1: "a", "b" (2)
        // Length 2: "ab", "ba" (2)
        // Length 3: "aba", "bab" (2)
        // Length 4: "abab", "baba" (2)
        // Length 5: "ababa" (1)
        // Total = 9
        assertEquals(9, sam.countDistinctSubstrings());
    }
}
