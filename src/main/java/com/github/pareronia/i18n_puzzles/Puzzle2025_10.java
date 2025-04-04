package com.github.pareronia.i18n_puzzles;

import static java.util.stream.Collectors.toMap;

import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.crypto.bcrypt.BCrypt;

import com.github.pareronia.i18n_puzzles.common.Sample;
import com.github.pareronia.i18n_puzzles.common.Samples;
import com.github.pareronia.i18n_puzzles.common.SolutionBase;
import com.github.pareronia.i18n_puzzles.common.Utils;

public class Puzzle2025_10 extends SolutionBase<Integer>{

    private final Map<Integer, Boolean> cache = new ConcurrentHashMap<>();

    private Puzzle2025_10(final boolean debug) {
        super(debug);
    }

    public static Puzzle2025_10 create() {
        return new Puzzle2025_10(false);
    }

    public static Puzzle2025_10 createDebug() {
        return new Puzzle2025_10(true);
    }

    private Set<String> variants(final String secret) {
        record State(int i, String var) {}
        final Set<String> ans = new HashSet<>();
        final Deque<State> q = new ArrayDeque<>();
        q.add(new State(0, ""));
        while (!q.isEmpty()) {
            final State state = q.poll();
            if (state.i == secret.length()) {
                ans.add(state.var);
            } else {
                final String c = String.valueOf(secret.charAt(state.i));
                final String n = Normalizer.normalize(c, Form.NFD);
                if (!c.equals(n)) {
                    q.add(new State(state.i + 1, state.var + n));
                }
                q.add(new State(state.i + 1, state.var + c));
            }
        }
        return ans;
    }

    private boolean check(final String secret, final String hash) {
        return cache.computeIfAbsent(
            Objects.hash(secret, hash),
            k -> variants(secret).stream().anyMatch(s ->
                BCrypt.checkpw(s.getBytes(StandardCharsets.UTF_8), hash)));
    }

    @Override
    public Integer solve(final List<String> input) {
        final List<List<String>> blocks = Utils.toBlocks(input);
        final Map<String, String> auths = blocks.get(0).stream()
                .map(line -> line.split(" "))
                .collect(toMap(split -> split[0], split -> split[1]));
        return (int) blocks.get(1).parallelStream()
                .map(line -> line.split(" "))
                .filter(split -> {
                    final String username = split[0];
                    final String cand = Normalizer.normalize(split[1], Form.NFC);
                    return check(cand, auths.get(username));
                })
                .count();
    }

    @Override
    @Samples({
        @Sample(input = TEST, expected = "4")
    })
    public void samples() { }

    public static void main(final String[] args) throws Exception {
        Puzzle2025_10.create().run();
    }

    private static final String TEST = """
            etasche $2b$07$0EBrxS4iHy/aHAhqbX/ao.n7305WlMoEpHd42aGKsG21wlktUQtNu
            mpataki $2b$07$bVWtf3J7xLm5KfxMLOFLiu8Mq64jVhBfsAwPf8/xx4oc5aGBIIHxO
            ssatterfield $2b$07$MhVCvV3kZFr/Fbr/WCzuFOy./qPTyTVXrba/2XErj4EP3gdihyrum
            mvanvliet $2b$07$gf8oQwMqunzdg3aRhktAAeU721ZWgGJ9ZkQToeVw.GbUlJ4rWNBnS
            vbakos $2b$07$UYLaM1I0Hy/aHAhqbX/ao.c.VkkUaUYiKdBJW5PMuYyn5DJvn5C.W
            ltowne $2b$07$4F7o9sxNeaPe..........l1ZfgXdJdYtpfyyUYXN/HQA1lhpuldO

            etasche .pM?XÑ0i7ÈÌ
            mpataki 2ö$p3ÄÌgÁüy
            ltowne 3+sÍkÜLg._
            ltowne 3+sÍkÜLg?_
            mvanvliet *íÀŸä3hñ6À
            ssatterfield 8É2U53N~Ë
            mpataki 2ö$p3ÄÌgÁüy
            mvanvliet *íÀŸä3hñ6À
            etasche .pM?XÑ0i7ÈÌ
            ssatterfield 8É2U53L~Ë
            mpataki 2ö$p3ÄÌgÁüy
            vbakos 1F2£èÓL
            """;
}
