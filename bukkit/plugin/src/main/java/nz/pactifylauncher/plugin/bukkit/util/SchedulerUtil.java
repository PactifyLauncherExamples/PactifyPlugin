package nz.pactifylauncher.plugin.bukkit.util;

import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.Iterator;

@UtilityClass
public class SchedulerUtil {
    public static void scheduleSyncDelayedTask(JavaPlugin plugin, Collection<Integer> tasksCollection, Runnable task,
                                              long delay) {
        new Task(tasksCollection, task).scheduleSyncDelayedTask(plugin, delay);
    }

    public static void cancelTasks(JavaPlugin plugin, Collection<Integer> tasksCollection) {
        for (Iterator<Integer> it = tasksCollection.iterator(); it.hasNext(); ) {
            plugin.getServer().getScheduler().cancelTask(it.next());
            it.remove();
        }
    }

    @RequiredArgsConstructor
    private static class Task implements Runnable {
        private final Collection<Integer> tasksCollection;
        private final Runnable handle;
        private int taskId;

        @Override
        public void run() {
            tasksCollection.remove(taskId);
            handle.run();
        }

        public void scheduleSyncDelayedTask(JavaPlugin plugin, long delay) {
            taskId = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, delay);
            if (taskId != -1) {
                tasksCollection.add(taskId);
            }
        }
    }
}
