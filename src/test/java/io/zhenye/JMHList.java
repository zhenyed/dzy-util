package io.zhenye;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@Threads(2)
public class JMHList {

    private static final List<String> list = new ArrayList<>();

    private static final List<String> synchronizedList = Collections.synchronizedList(list);
    private static final List<String> vector = new Vector<>();
    private static final List<String> cow = new CopyOnWriteArrayList<>();

    static {
        list.add("1");
        vector.add("1");
        cow.add("1");
    }

    @Benchmark
    public void synchronizedListTest() {
        synchronizedList.get(0);
    }

    @Benchmark
    public void vectorTest() {
        vector.get(0);
    }

    @Benchmark
    public void cowTest() {
        cow.get(0);
    }

    public static void main(String[] args) throws Exception {
        Options opts = new OptionsBuilder()
                .include(JMHList.class.getSimpleName())
                .resultFormat(ResultFormatType.JSON)
                .build();
        new Runner(opts).run();
    }


}
