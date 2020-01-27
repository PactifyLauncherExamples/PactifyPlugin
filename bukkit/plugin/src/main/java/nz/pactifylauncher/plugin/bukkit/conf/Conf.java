package nz.pactifylauncher.plugin.bukkit.conf;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

import java.util.List;

@lombok.Builder(builderClassName = "Builder")
@Data
public class Conf {
    private JoinActions loginWithPactify;
    private JoinActions loginWithoutPactify;

    @lombok.Builder(builderClassName = "Builder")
    @Data
    public static class JoinActions {
        private @Singular("message") List<Action> messages;
        private @Singular("command") List<Action> commands;
    }

    @RequiredArgsConstructor
    @Data
    public static class Action {
        private final @NonNull String value;
        private final int delay;
    }
}
