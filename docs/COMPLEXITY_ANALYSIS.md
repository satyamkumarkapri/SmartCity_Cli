# SmartCityX Complexity & Theoretical Analysis

This document provides mathematical invariants, correctness proofs, asymptotic bounds, and theoretical foundations for the core algorithm families implemented in **SmartCityX — Integrated Smart-City Analytics Platform**.

---

## 1. String & Suffix Algorithms

### 1.1 Knuth-Morris-Pratt (KMP)
- **Correctness Invariant**: The failure function $\pi[i]$ stores the length of the longest proper prefix of pattern $P[0 \dots i]$ that is also a suffix of $P[0 \dots i]$. When a mismatch occurs at $P[j]$ against text $T[i]$, no characters prior to $T[i - j + \pi[j-1]]$ can start a valid match.
- **Time Complexity Analysis**:
  - Precomputation of $\pi$: During iteration $i=1 \dots M-1$, $j$ increases by at most 1 per iteration ($M-1$ increments). In the `while (j > 0 && ...)` loop, $j$ decreases by at least 1. Thus, total decreases cannot exceed total increases, bounding inner loop iterations to $O(M)$.
  - Matching Phase: Text index $i$ increments exactly $N$ times. Pattern index $j$ increases by at most 1 each step. Total backward fallbacks via $j = \pi[j-1]$ are bounded by $N$. Total comparisons: $\le 2N$.
  - **Overall Bound**: $O(N + M)$ time, $O(M)$ auxiliary space.

### 1.2 Z-Algorithm
- **Correctness Invariant**: $Z[i]$ is the length of the longest common prefix between $T$ and the suffix of $T$ starting at index $i$. An active box $[L, R]$ maintains the rightmost substring matching a prefix of $T$ discovered so far ($R = \max(j + Z[j] - 1)$).
- **Complexity**: If $i \le R$, $Z[i] \ge \min(R - i + 1, Z[i - L])$. Explicit character comparisons occur only when $i + Z[i - L] > R$, directly expanding $R$. Since $R$ increases monotonically from $0$ to $N$, total character comparisons are strictly bounded by $2N$. Time: $O(N)$, Space: $O(N)$.

### 1.3 Rabin-Karp Rolling Hash
- **Hash Function**: For window $W$ of length $M$, $H(W) = \left(\sum_{k=0}^{M-1} W[k] \cdot B^{M-1-k}\right) \bmod Q$, where $B = 31$ and $Q = 10^9 + 7$.
- **Window Update**: $H(T[i+1 \dots i+M]) = \left( (H(T[i \dots i+M-1]) - T[i] \cdot B^{M-1}) \cdot B + T[i+M] \right) \bmod Q$.
- **Collision Invariant**: A hash match indicates a *candidate* match. Actual equality is verified character-by-character to eliminate false positives.
  - Expected Time: $O(N + M)$ when hash distribution is uniform over $\mathbb{Z}_Q$.
  - Worst Case: $O(N \cdot M)$ if an adversary constructs a text causing collisions at every offset.

### 1.4 Kasai's LCP Construction
- **Correctness Invariant**: Let $rank[i]$ be the position of suffix $i$ in the suffix array $SA$. If $LCP(rank[i], rank[i]-1) = h$, then for the next suffix $i+1$, its $LCP(rank[i+1], rank[i+1]-1) \ge h - 1$.
- **Time Complexity**: The prefix comparison length $h$ decreases by at most 1 per suffix (at most $N$ decreases) and never exceeds $N$. Hence, the while loop increments $h$ at most $2N$ times across the entire algorithm. Total time: $O(N)$ strictly linear. Space: $O(N)$.

### 1.5 Suffix Automaton (DAWG)
- **Structural Properties**: A suffix automaton for text $T$ of length $N$ contains at most $2N - 1$ states and at most $3N - 4$ transitions (for $N \ge 2$).
- **Linear Construction**: Online character-by-character addition traverses the suffix link tree. Splitting state $q$ into clone $clone$ maintains right-equivalence classes ($endpos$). Construction runs in $O(N \cdot \Sigma)$ time and space.

---

## 2. Advanced Dynamic Programming

### 2.1 Edit Distance (Levenshtein & Damerau-Levenshtein)
- **Optimal Substructure**: The optimal alignment between $A[1 \dots i]$ and $B[1 \dots j]$ is composed of optimal alignments of smaller prefixes:
  $$DP[i][j] = \min \begin{cases}
  DP[i-1][j] + c_{del} \\
  DP[i][j-1] + c_{ins} \\
  DP[i-1][j-1] + (A[i] == B[j] ? 0 : c_{sub}) \\
  DP[i-2][j-2] + c_{trans} & \text{(if } A[i]==B[j-1] \land A[i-1]==B[j]\text{)}
  \end{cases}$$
- **OSA vs Unrestricted Damerau-Levenshtein**: Optimal String Alignment (OSA) prohibits an edited substring from being modified more than once (e.g., cannot transpose and then insert between transposed characters). Unrestricted Damerau-Levenshtein uses a character position array $da[\Sigma]$ to look back arbitrarily far, correctly finding edit distances where transpositions interleave with insertions.
- **Space Optimization**: Since $DP[i][j]$ depends only on rows $i-1$ and $i-2$, space can be reduced to $O(\min(N, M))$ when traceback reconstruction is not required.

### 2.2 Bitmask Dynamic Programming
- **Travelling Salesperson Problem (TSP)**:
  - State: $DP[mask][u]$ is the minimum cost of visiting exactly the subset of vertices encoded by $mask$, ending at vertex $u$.
  - Recurrence: $DP[mask][u] = \min_{v \in mask \setminus \{u\}} \left( DP[mask \setminus \{u\}][v] + cost(v, u) \right)$.
  - Time: $\sum_{k=1}^N \binom{N}{k} \cdot k \cdot (N-k) = O(N^2 \cdot 2^N)$.
  - Space: $O(N \cdot 2^N)$. Safe input limit: $N \le 20$.

- **Sum Over Subsets (SOS DP)**:
  - Objective: Compute $F[mask] = \sum_{sub \subseteq mask} A[sub]$ for all $mask < 2^N$.
  - Naive iteration over submasks takes $\sum_{k=0}^N \binom{N}{k} 2^k = 3^N$.
  - Fast Walsh-Hadamard SOS DP defines subproblem $DP[i][mask]$ as the sum over submasks differing from $mask$ only in the first $i$ bits:
    $$DP[i][mask] = \begin{cases} DP[i-1][mask] & \text{if bit } i \text{ is } 0 \\ DP[i-1][mask] + DP[i-1][mask \oplus 2^i] & \text{if bit } i \text{ is } 1 \end{cases}$$
  - Time: $O(N \cdot 2^N)$, Space: $O(2^N)$ in-place.

---

## 3. Network Flow & Duality

### 3.1 Max-Flow Min-Cut Theorem
- **Fundamental Duality**: For any network $(G, s, t, c)$ with flow $f$ and cut $(S, T)$ where $s \in S, t \in T$:
  $$\text{val}(f) \le \text{cap}(S, T)$$
  Equality holds if and only if $f$ is a maximum flow and $(S, T)$ is a minimum cut, in which case every forward edge across the cut is saturated ($f(u, v) = c(u, v)$) and every backward edge has zero flow ($f(v, u) = 0$).

### 3.2 Dinic's Algorithm
- **Invariants**:
  1. The level graph $L_G$ constructed by BFS contains only edges $(u, v)$ in the residual network where $level[v] = level[u] + 1$.
  2. In each phase, DFS sends blocking flow along paths strictly composed of level-graph edges. Current-edge pointers prevent revisiting dead-end saturated edges.
  3. The distance from source to sink in the level graph strictly increases after each phase: $level'[t] > level[t]$.
- **Complexity**:
  - The number of phases is at most $V - 1$.
  - Each phase pushes blocking flow in $O(V \cdot E)$ time using current-edge optimization.
  - Overall time: $O(V^2 \cdot E)$. On unit capacity networks, the phase count is bounded by $O(\sqrt{V})$, giving $O(E \sqrt{V})$.

---

## 4. NP-Completeness & Approximation Guarantees

### 4.1 Vertex Cover 2-Approximation
- **Algorithm**: Compute a maximal matching $M$ by greedily adding edges $(u, v)$ with neither endpoint covered, and select both $u$ and $v$ into cover $C$.
- **Approximation Ratio Proof**:
  - Every edge in $M$ is disjoint; no single vertex can cover more than one edge in $M$.
  - Therefore, any valid vertex cover $C^*$ must contain at least one endpoint from each edge in $M$: $|C^*| \ge |M|$.
  - The algorithm selects both endpoints of every edge in $M$: $|C| = 2 |M|$.
  - Combining inequalities: $|C| = 2 |M| \le 2 |C^*|$. The approximation ratio is guaranteed to be $\le 2$.

### 4.2 0/1 Knapsack FPTAS
- **Scaling Mechanism**: Let $V_{\max} = \max_i v_i$. Scale all values: $v'_i = \lfloor \frac{v_i \cdot N}{\varepsilon \cdot V_{\max}} \rfloor$.
- **Dynamic Programming on Values**: Compute minimum weight to achieve total scaled profit $V'$:
  $DP[i][p] = \min(DP[i-1][p], DP[i-1][p - v'_i] + w_i)$.
- **Time Complexity**: $O(N \cdot \sum v'_i) = O(N^2 \cdot \frac{N}{\varepsilon}) = O(N^3 / \varepsilon)$.
- **Approximation Guarantee**: The solution $S$ satisfies $\sum_{i \in S} v_i \ge (1 - \varepsilon) \cdot \text{OPT}$.

### 4.3 Parameterized Vertex Cover (Bounded Search Tree)
- **Invariant**: Any uncovered edge $(u, v)$ requires either $u \in C$ or $v \in C$.
- **Branching Rule**: Branch on $u$ (parameter $k-1$) and branch on $v$ (parameter $k-1$).
- **Recurrence**: $T(k) = 2 \cdot T(k - 1) + O(V + E) \implies O(2^k \cdot (V + E))$. Fixed-Parameter Tractable (FPT).

---

## 5. Randomized Algorithms

### 5.1 Miller-Rabin Primality Test
- **Number Theoretical Invariant**: Let $N - 1 = 2^s \cdot d$ with $d$ odd. By Fermat's Little Theorem, if $N$ is prime, for any base $a \not\equiv 0 \pmod N$:
  Either $a^d \equiv 1 \pmod N$ or $a^{d \cdot 2^r} \equiv -1 \pmod N$ for some $0 \le r < s$.
- **Error Probability**: For any odd composite $N$, at most $\frac{1}{4}$ of the bases $a \in [2, N-2]$ are false witnesses (strong pseudoprimes). After $k$ independent random bases, the probability of false primality is $\le 4^{-k} = 2^{-2k}$.
- **Deterministic 64-bit Basis**: For any 64-bit integer $N < 2^{64}$, testing against the 12 deterministic witnesses $\{2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37\}$ yields a 100% deterministic, exact primality verdict.

### 5.2 Reservoir Sampling
- **Inductive Invariant**: After processing $i$ items from the stream ($i \ge K$), the reservoir contains a uniform random sample of size $K$ from the first $i$ items. Every item in the stream has an exact probability of $\frac{K}{i}$ of being in the reservoir.
- **Proof**:
  - Base case: For $i = K$, items $1 \dots K$ are in the reservoir with probability $K/K = 1$.
  - Step: For the $(i+1)$-th item, it is chosen with probability $\frac{K}{i+1}$. An existing item is replaced with probability $\frac{K}{i+1} \cdot \frac{1}{K} = \frac{1}{i+1}$.
  - The probability that an item previously in the reservoir survives is:
    $$\frac{K}{i} \times \left(1 - \frac{1}{i+1}\right) = \frac{K}{i} \times \frac{i}{i+1} = \frac{K}{i+1}$$
  - By induction, after stream termination at $N$, every item's retention probability is identically $\frac{K}{N}$.

---

## 6. Parallel Algorithms (Work & Span Model)

### 6.1 Work, Span, and Speedup
- **Work ($T_1$)**: Total number of operations executed on a single processor.
- **Span ($T_\infty$)**: The length of the longest critical path of sequential dependencies in the computation DAG.
- **Speedup**: $S_P = \frac{T_1}{T_P} \le \frac{T_1}{T_\infty}$ (cannot exceed the parallelism $\frac{T_1}{T_\infty}$).
- **Efficiency**: $E_P = \frac{S_P}{P} = \frac{T_1}{P \cdot T_P} \le 1$.

### 6.2 Brent's Theorem
For a computation with work $T_1$ and span $T_\infty$ executed on $P$ parallel processors using a greedy scheduler:
$$T_P \le \frac{T_1 - T_\infty}{P} + T_\infty$$
- **Significance**: If $P \ll \frac{T_1}{T_\infty}$, execution achieves near-linear speedup $T_P \approx \frac{T_1}{P}$. As $P \to \infty$, runtime asymptotically approaches the critical path limit $T_\infty$.

### 6.3 Blelloch Work-Efficient Scan
- **Upsweep Phase (Reduce)**: Computes partial sums in a balanced binary tree. Work: $O(N)$, Span: $O(\log N)$.
- **Downsweep Phase**: Traverses down the tree propagating prefix sums and writing exclusive offsets. Work: $O(N)$, Span: $O(\log N)$.
- **Total Bounds**: Work $T_1 = O(N)$ (asymptotically identical to sequential loop), Span $T_\infty = O(\log N)$.
