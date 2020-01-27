package nz.pactifylauncher.plugin.bukkit.conf;

import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import nz.pactifylauncher.plugin.bukkit.PactifyPlugin;
import org.bukkit.ChatColor;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.representer.BaseRepresenter;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class YamlConfProvider {
    private static final ThreadLocal<Yaml> YAML = ThreadLocal.withInitial(() -> {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(false);
        options.setIndent(2);
        return new Yaml(new SafeConstructor(), new NoAnchorRepresenter(), options);
    });

    private static final String HEADER = "# This is the main configuration file for the Pactify Plugin on Bukkit.\n"
            + "# For a reference for any variable inside this file, check out the Pactify Plugin\n"
            + "# README: https://github.com/PactifyLauncherExamples/PactifyPlugin\n\n";

    public static Conf load(PactifyPlugin plugin) {
        return new YamlConfProvider(plugin, new File(plugin.getDataFolder(), "config.yml")).loadConfig();
    }

    private final PactifyPlugin plugin;
    private final File file;
    private final Map<String, Object> root = new LinkedHashMap<>();

    private Conf loadConfig() {
        readValues();
        Conf conf = Conf.builder()
                .loginWithPactify(getJoinActions("join.with-pactify",
                        Collections.singletonList(ImmutableMap.of("delay", 0, "value", "&eYou are connected with the &cPactify Launcher&e!")),
                        Collections.singletonList(ImmutableMap.of("delay", 20, "value", "/minecraft:effect {{name}} speed 15"))))
                .loginWithoutPactify(getJoinActions("join.without-pactify",
                        Collections.emptyList(),
                        Collections.emptyList()))
                .build();
        writeValues();
        return conf;
    }

    private Conf.JoinActions getJoinActions(String path, List<Map> defMessages, List<Map> defCommands) {
        return Conf.JoinActions.builder()
                .messages(getActionList(path + ".messages", defMessages, s -> ChatColor.translateAlternateColorCodes('&', s)))
                .commands(getActionList(path + ".commands", defCommands, s -> s.startsWith("/") ? s.substring(1) : s))
                .build();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private List<Conf.Action> getActionList(String path, List<Map> def, Function<String, String> valueTransformer) {
        Object value = get(path);
        if (!(value instanceof List)) {
            value = def;
        }
        List<Conf.Action> ret = new ArrayList<>();
        value = ((List<Object>) value).stream().map(e -> {
            String v;
            if ((v = asString(e, null)) != null) {
                if (!v.isEmpty()) {
                    ret.add(new Conf.Action(valueTransformer.apply(v), 0));
                    return ImmutableMap.of("delay", 0, "value", v);
                }
            } else if (e instanceof Map) {
                v = asString(((Map) e).get("value"), "");
                if (!v.isEmpty()) {
                    int delay = asInt(((Map) e).get("delay"), 0);
                    ret.add(new Conf.Action(valueTransformer.apply(v), delay));
                    return ImmutableMap.of("delay", delay, "value", v);
                }
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        set(path, value);
        return ret;
    }

    private void readValues() {
        Map<String, Object> config = null;
        if (file.exists()) {
            try (Reader rd = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
                //noinspection unchecked
                config = (Map<String, Object>) YAML.get().load(rd);
            } catch (IOException | ClassCastException e) {
                plugin.getLogger().log(Level.SEVERE,
                        "Could not load " + file + ", please correct your syntax errors", e);
                throw new RuntimeException(e);
            }
        }
        root.clear();
        if (config != null && !config.isEmpty()) {
            root.putAll(config);
        }
    }

    private void writeValues() {
        String dump = HEADER + YAML.get().dump(root);

        //noinspection ResultOfMethodCallIgnored
        file.getParentFile().mkdirs();
        try (Writer wr = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            wr.write(dump);
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Could not save " + file, e);
        }
    }

    private void set(String path, Object value) {
        Map<String, Object> section = root;
        int i;
        while ((i = path.indexOf('.')) != -1) {
            Object v = section.get(path.substring(0, i));
            if (!(v instanceof Map)) {
                section.put(path.substring(0, i), v = new LinkedHashMap<String, Object>());
            }
            //noinspection unchecked
            section = (Map<String, Object>) v;
            path = path.substring(i + 1);
        }
        section.put(path, value);
    }

    private Object get(String path) {
        Map<String, Object> section = root;
        int i;
        while ((i = path.indexOf('.')) != -1) {
            Object v = section.get(path.substring(0, i));
            if (!(v instanceof Map)) {
                return null;
            }
            //noinspection unchecked
            section = (Map<String, Object>) v;
            path = path.substring(i + 1);
        }
        return section.get(path);
    }

    private static int asInt(Object value, int def) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return def;
    }

    private static String asString(Object value, String def) {
        if (value instanceof String) {
            return (String) value;
        }
        if (value instanceof Number) {
            return value.toString();
        }
        if (value instanceof Boolean) {
            return value.toString();
        }
        return def;
    }

    public static class NoAnchorRepresenter extends Representer {
        public NoAnchorRepresenter() {
            try {
                Field field = BaseRepresenter.class.getDeclaredField("representedObjects");
                field.setAccessible(true);

                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

                field.set(this, new HashMap<Object, Node>() {
                    @Override
                    public Node put(Object k, Node v) {
                        return null;
                    }
                });
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
