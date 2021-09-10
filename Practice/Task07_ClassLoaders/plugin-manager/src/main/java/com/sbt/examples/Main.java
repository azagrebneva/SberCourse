package com.sbt.examples;

import java.net.MalformedURLException;

/* Задание 7. Загрузчики классов
  7.1 Ваша задача написать загрузчик плагинов в вашу систему.
  Допустим вы пишите свой браузер, и хотите чтобы люди имели
  возможность писать плагины для него.
  Соответственно, разные разработчики могут назвать свои классы
  одинаковым именем, ваш загрузчик должен корректно это обрабатывать.
*/

public class Main {

    public static void main(String[] args) throws MalformedURLException {

        // Загружаются плагины с одинаковыми именами (BeHappyPlugin), но разными действиями

        // папка 1
        PluginManager pluginManager = new PluginManager("F:\\Java\\SberCourse\\Practice\\Task07_ClassLoaders\\plugin-manager\\plugins\\PluginSet1");
        System.out.println("Initialization set 1");
        pluginManager.initializePlugin();
        pluginManager.startAll();

        // папка 2
        PluginManager pluginManager2 = new PluginManager("F:\\Java\\SberCourse\\Practice\\Task07_ClassLoaders\\plugin-manager\\plugins\\PluginSet2");
        System.out.println("\nInitialization set 2");
        pluginManager2.initializePlugin();
        pluginManager2.startAll();

        // ядро
        System.out.println("\nPlugin in core ... ");
        Plugin beHappyPlugin = new BeHappyPlugin();
        beHappyPlugin.doUsefull();
    }
}
