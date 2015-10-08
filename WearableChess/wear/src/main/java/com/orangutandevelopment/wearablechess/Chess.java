package com.orangutandevelopment.wearablechess;

import java.util.ArrayList;

/**
 * Created by mitch on 27/09/2015.
 * Enormous amounts of this code are from Toledo Nanochess (nanochess.org)
 * This code is free for non-commercial use, but for commercial use please contact the author of Nanochess using the URL above.
 */
public class Chess {
    public int B, i, y, u, b, L, I[] = new int[411], G = 120, l[] = {
            5, 3, 4, 6, 2, 4, 3, 5, 1, 1, 1, 1, 1, 1, 1, 1, 9, 9, 9, 9, 9, 9, 9, 9, 13, 11, 12, 14, 10, 12, 11, 13, 0, 11, 0, 34, 33, 55, 94, 0, 1, 2, 3, 3, 2, 1, 0, -1, 1, -10,
            10, -11, -9, 9, 11, 10, 20, -9, -11, -10, -20, -21, -19, -12, -8, 8, 12, 19, 21, 53, 47, 61, 51, 47, 47
    };
    static final int x = 10, z = 15, M = 10000;

    public Chess() {
        for (B = i = y = u = b = 0; B++ < 120;) I[B - 1] = B % x != 0 ? B / x % x < 2 | B % x < 2 ? 7 : (B / x & 4) != 0 ? 0 : l[i++] | 16 : 7;
    }

    public ArrayList<Integer> getLegalMoves(int s) {
        ArrayList<Integer> validSq = new ArrayList<>();
        for (int j = 21; j < 99 && j != 29; j += j > 90 ? -69 : 10) {
            Chess verifier = new Chess();
            verifier.CopyBoard(this);
            if (verifier.Move(s, j))
                validSq.add(j);
        }
        return validSq;
    }

    public int CountPieces() {
        int count = 0;
        for (int j : I){
            if (j != 0 && j != 7 && j != 8 && j != 15)
                ++count;
        }
        return count;
    }

    public void CopyBoard(Chess c) {
        I = c.I.clone(); B = c.B; i = c.i; y = c.y; u = c.u; L = c.L; b = c.b;
    }

    public boolean Move(int s, int f) {
        MovePart(s);
        return MovePart(f);
    }

    public boolean MovePart(int s) {
        i = (I[s] ^ y) & z;
        if (i > 8) {
            B = s;
        }
        else if (B != 0 && i < 9) {
            b = s;
            i = I[B] & z;
            if ((i & 7) == 1 & (b < 29 | b > 90)) i = 14 ^ y;
            X(0, 0, 0, 21, u, 1);
            L = B; //Highlight last moves
            B = 0;
            return (y > 0);
        }
        return false;
    }

    public void Respond() {
        Respond(4);
    }

    public void Respond(int depth) {
        X(0, 0, 0, 21, u, depth /*ply*/ );
        X(0, 0, 0, 21, u, 1);
        L = B; //Highlight last moves
        B = 0;
    }

    int X(int w, int c, int h, int e, int S, int s) {
        int t, o, L, E, d, O = e, N = -M * M, K = 78 - h << x, p, g, n, m, A, q, r, C, a = y > 0 ? -x : x;
        boolean D, J;
        y ^= 8;
        G++;
        D = w > 0 || s > 0 && s >= h && X(0, 0, 0, 21, 0, 0) > M;
        do {
            if ((o = I[p = O]) > 0) {
                q = o & z ^ y;
                if (q < 7) {
                    A = (q-- & 2) > 0 ? 8 : 4;
                    C = (o & z) != 9 ? l[69 + q] : 57;
                    do {
                        r = I[p += l[C]];
                        if (w < 1 | p == w) {
                            g = q > 0 | p + a != S ? 0 : S;
                            if (r < 1 & (q > 0 | A < 3 || g > 0) || (r + 1 & z ^ y) > 9 && q > 0 | A > 2) {
                                if ((r & 7) == 2) {
                                    y ^= 8;
                                    I[G--] = O;
                                    return K;
                                }
                                m = 0;
                                n = o & z;
                                J = true;
                                E = I[p - a] & z;
                                if (q > 0 | E != 7) t = n;
                                else {
                                    n += 2;
                                    t = 6 ^ y;
                                }
                                while (n <= t) {
                                    L = (r > 0 ? l[r & 7 | 32] * 9 - h - q : 0);
                                    if (s > 0) L += (q != 1 ? l[p / x + 37] - l[O / x + 37] + l[p % x + 38] - l[O % x + 38] + o / 16 * 8 : (m > 0 ? 9 : 0)) + (q < 1 ? l[p % x + 38] - 1 + ((I[p - 1] ^ n) < 1 ? 1 : 0) + ((I[p + 1] ^ n) < 1 ? 1 : 0) + l[n & 7 | 32] * 9 - 99 + (g > 0 ? 99 : 0) + (A < 2 ? 1 : 0) : 0) + ((E ^ y ^ 9) < 1 ? 1 : 0);
                                    if (s > h || 1 < s & s == h && L > z | D) {
                                        I[p] = n;
                                        I[O] = 0;
                                        if (m > 0) {
                                            I[g] = I[m];
                                            I[m] = 0;
                                        } else if (g > 0) I[g] = 0;
                                        L -= X(s > h | D ? 0 : p, L - N, h + 1, I[G + 1], E = q > 0 | A > 1 ? 0 : p, s);
                                        if (h < 1 & s == 1 && B == O & i == n & p == b & L > -M) {
                                            G--;
                                            return u = E;
                                        }
                                        J = q != 1 || A < 7 || m > 0 || s < 1 | D | r > 0 | o < z || X(0, 0, 0, 21, 0, 0) > M;
                                        I[O] = o;
                                        I[p] = r;
                                        if (m > 0) {
                                            I[m] = I[g];
                                            I[g] = 0;
                                        } else if (g > 0) I[g] = 9 ^ y;
                                    }
                                    if (L > N) {
                                        I[G] = O;
                                        if (s > 1) {
                                            if (h > 0 && c - L < 0) {
                                                y ^= 8;
                                                G--;
                                                return L;
                                            }
                                            if (h < 1) {
                                                i = n;
                                                B = O;
                                                b = p;
                                            }
                                        }
                                        N = L;
                                    }
                                    if (J) n++;
                                    else {
                                        g = p;
                                        m = p < O ? g - 3 : g + 2;
                                        if (I[m] < z | I[m + O - p] > 0 || I[p += p - O] > 0) n++;
                                    }
                                }
                            }
                        }
                    }
                    while (r < 1 & q > 2 || ((p = O) > 0) && (q > 0 | A > 2 | o > z & r < 1) && ++C > 0 && --A > 0);
                }
            }
            if (++O > 98) O = 20;
        }
        while (e != O);
        y ^= 8;
        G--;
        return N != -M * M && N > -K + 1924 | D ? N : 0;
    }
}
