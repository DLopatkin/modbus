package main.models;

import java.util.Queue;

public abstract class TestBase {
    public abstract void run(Queue<String> out, Station station, int param);
}
