package smartcity.algorithms.dynamicprogramming;

/**
 * Needleman-Wunsch (Global Alignment) and Smith-Waterman (Local Alignment) algorithms.
 * Implements configurable scoring, linear gap penalties, dynamic programming traceback,
 * and aligned sequence reconstruction.
 */
public class SequenceAlignment {

    public static class AlignmentResult {
        public final int score;
        public final String alignedSeq1;
        public final String alignedSeq2;
        public final boolean isLocal;

        public AlignmentResult(int score, String s1, String s2, boolean isLocal) {
            this.score = score;
            this.alignedSeq1 = s1;
            this.alignedSeq2 = s2;
            this.isLocal = isLocal;
        }

        @Override
        public String toString() {
            return String.format("Score: %d (%s)\nSeq1: %s\nSeq2: %s",
                    score, isLocal ? "Smith-Waterman Local" : "Needleman-Wunsch Global", alignedSeq1, alignedSeq2);
        }
    }

    /**
     * Needleman-Wunsch Global Alignment.
     * Aligns both sequences from start to finish.
     * Time Complexity: O(nm), Space: O(nm).
     */
    public static AlignmentResult needlemanWunsch(String s1, String s2,
                                                  int matchScore, int mismatchPenalty, int gapPenalty) {
        if (s1 == null) s1 = "";
        if (s2 == null) s2 = "";
        int n = s1.length();
        int m = s2.length();

        int[][] score = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) score[i][0] = -i * gapPenalty;
        for (int j = 0; j <= m; j++) score[0][j] = -j * gapPenalty;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int match = score[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? matchScore : -mismatchPenalty);
                int delete = score[i - 1][j] - gapPenalty;
                int insert = score[i][j - 1] - gapPenalty;
                score[i][j] = Math.max(Math.max(match, delete), insert);
            }
        }

        // Traceback from (n, m)
        StringBuilder align1 = new StringBuilder();
        StringBuilder align2 = new StringBuilder();
        int i = n, j = m;

        while (i > 0 || j > 0) {
            int current = score[i][j];
            if (i > 0 && j > 0) {
                int diag = s1.charAt(i - 1) == s2.charAt(j - 1) ? matchScore : -mismatchPenalty;
                if (current == score[i - 1][j - 1] + diag) {
                    align1.insert(0, s1.charAt(i - 1));
                    align2.insert(0, s2.charAt(j - 1));
                    i--; j--;
                    continue;
                }
            }
            if (i > 0 && current == score[i - 1][j] - gapPenalty) {
                align1.insert(0, s1.charAt(i - 1));
                align2.insert(0, '-');
                i--;
            } else if (j > 0 && current == score[i][j - 1] - gapPenalty) {
                align1.insert(0, '-');
                align2.insert(0, s2.charAt(j - 1));
                j--;
            }
        }

        return new AlignmentResult(score[n][m], align1.toString(), align2.toString(), false);
    }

    /**
     * Smith-Waterman Local Alignment.
     * Identifies the highest-scoring local matching sub-region.
     * Scores are bounded below by 0; traceback starts at maximum matrix cell.
     * Time Complexity: O(nm), Space: O(nm).
     */
    public static AlignmentResult smithWaterman(String s1, String s2,
                                                int matchScore, int mismatchPenalty, int gapPenalty) {
        if (s1 == null) s1 = "";
        if (s2 == null) s2 = "";
        int n = s1.length();
        int m = s2.length();

        int[][] score = new int[n + 1][m + 1];
        int maxScore = 0;
        int maxI = 0, maxJ = 0;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int diag = s1.charAt(i - 1) == s2.charAt(j - 1) ? matchScore : -mismatchPenalty;
                int match = score[i - 1][j - 1] + diag;
                int delete = score[i - 1][j] - gapPenalty;
                int insert = score[i][j - 1] - gapPenalty;

                score[i][j] = Math.max(0, Math.max(Math.max(match, delete), insert));
                if (score[i][j] > maxScore) {
                    maxScore = score[i][j];
                    maxI = i;
                    maxJ = j;
                }
            }
        }

        // Traceback from maximum scoring cell until 0 is reached
        StringBuilder align1 = new StringBuilder();
        StringBuilder align2 = new StringBuilder();
        int i = maxI, j = maxJ;

        while (i > 0 && j > 0 && score[i][j] > 0) {
            int current = score[i][j];
            int diag = s1.charAt(i - 1) == s2.charAt(j - 1) ? matchScore : -mismatchPenalty;
            if (current == score[i - 1][j - 1] + diag) {
                align1.insert(0, s1.charAt(i - 1));
                align2.insert(0, s2.charAt(j - 1));
                i--; j--;
            } else if (current == score[i - 1][j] - gapPenalty) {
                align1.insert(0, s1.charAt(i - 1));
                align2.insert(0, '-');
                i--;
            } else {
                align1.insert(0, '-');
                align2.insert(0, s2.charAt(j - 1));
                j--;
            }
        }

        return new AlignmentResult(maxScore, align1.toString(), align2.toString(), true);
    }
}
