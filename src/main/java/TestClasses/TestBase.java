package TestClasses;

import java.util.Map;
import java.util.Queue;

public abstract class TestBase {
    public abstract void run(Queue<String> in, Queue<String> out, Map<Integer, TestClasses.Station> stations, int param);
}
