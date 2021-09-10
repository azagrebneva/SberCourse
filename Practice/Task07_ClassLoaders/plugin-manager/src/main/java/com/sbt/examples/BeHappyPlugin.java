package com.sbt.examples;

import com.sbt.examples.Plugin;

/**
 * Тестовый класс имеющий такое же название как и плагин
 */
@SuppressWarnings("ALL")
public class BeHappyPlugin implements Plugin {

    public BeHappyPlugin() {
    }

    @Override
    public void doUsefull() {
        System.out.println("Be happy in java core ...");
    }
}
