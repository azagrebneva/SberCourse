package com.sbt.examples;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class PluginManager {
    private final File pluginRootDirectory;
    private final List<Plugin> plugins = new LinkedList<>();
    private final ClassLoader pluginLoader;

    public PluginManager(String pluginRootDirectory) throws MalformedURLException {
        File rootDir = new File(pluginRootDirectory);
        if (!rootDir.isDirectory()) {
            throw new IllegalArgumentException("Переданный путь не является директорией");
        }
        this.pluginRootDirectory = rootDir;
        File file = rootDir;
        pluginLoader = new ChildFirstUrlClassLoader(new URL[]{rootDir.toURI().toURL()},null);
    }

    /**
     * Загружает плагины (экземпляры класса)
     * @param pluginName
     * @param pluginClassName
     * @return null if plugin not found
     */
    public Plugin load(String pluginName, String pluginClassName) {
        try {
            return (Plugin) pluginLoader.loadClass(pluginClassName).getConstructor().newInstance();
        } catch (ClassNotFoundException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Инициализирует все плагины из pluginRootDirectory
     */
    public void initializePlugin() {
        List<File> allPlugins = getAllPath();
        for (File file : allPlugins) {
            String pluginName = file.getName();
            String pluginClassName = pluginName.substring(0, (pluginName.length()-".class".length()));
            plugins.add(load(pluginName, pluginClassName));
        }
    }

    /**
     * Запускает все имеющиеся плагины.  т.е вызывает метод doUsefull
     */
    public void startAll() {
        for (Plugin plugin : plugins) {
            plugin.doUsefull();
        }
    }

    /**
     * @return все файлы с плагинами в рут директории
     */
    private List<File> getAllPath() {
        File[] files = pluginRootDirectory.listFiles();
        assert files != null;
        return Arrays.stream(files).filter(file -> !(file.isDirectory())).collect(Collectors.toList());
    }
}

