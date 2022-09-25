package de.fhkiel.ki.cathedral;

import de.fhkiel.ki.cathedral.game.Board;
import de.fhkiel.ki.cathedral.game.Building;
import de.fhkiel.ki.cathedral.game.Direction;
import de.fhkiel.ki.cathedral.game.Placement;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
public class BoardBenchmark {

    @State(Scope.Benchmark)
    public static class EmptyBoard {
        public static Board base = new Board();
        public static Placement placement = new Placement(5, 5, Direction._0, Building.Black_Inn);

        public Board test;

        @Setup(Level.Invocation)
        public void setup(){
            test = base.copy();
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void benchmarkPlaceOnEmptyboard(EmptyBoard board) {
        board.test.placeBuilding(board.placement);
    }

    @State(Scope.Benchmark)
    public static class FiveTurnsInBoard {
        public static Board base = new Board();
        public static Placement placement = new Placement(8, 7, Direction._270, Building.Black_Castle);

        public Board test;

        public FiveTurnsInBoard(){
            base.placeBuilding(new Placement(3, 3, Direction._0, Building.Blue_Cathedral));
            base.placeBuilding(new Placement(8, 3, Direction._270, Building.Black_Academy));
            base.placeBuilding(new Placement(7, 1, Direction._0, Building.White_Castle));
            base.placeBuilding(new Placement(7, 5, Direction._90, Building.Black_Manor));
            base.placeBuilding(new Placement(6, 7, Direction._0, Building.White_Infirmary));
        }

        @Setup(Level.Invocation)
        public void setup(){
            test = base.copy();
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void benchmarkPlaceOnFiveTurnsInBoard(FiveTurnsInBoard board) {
        board.test.placeBuilding(board.placement);
    }

    @State(Scope.Benchmark)
    public static class TenTurnsInBoard {
        public static Board base = new Board();
        public static Placement placement = new Placement(1, 6, Direction._0, Building.White_Abbey);

        public Board test;

        public TenTurnsInBoard(){
            base.placeBuilding(new Placement(3, 3, Direction._0, Building.Blue_Cathedral));
            base.placeBuilding(new Placement(8, 3, Direction._270, Building.Black_Academy));
            base.placeBuilding(new Placement(7, 1, Direction._0, Building.White_Castle));
            base.placeBuilding(new Placement(7, 5, Direction._90, Building.Black_Manor));
            base.placeBuilding(new Placement(6, 7, Direction._0, Building.White_Infirmary));

            base.placeBuilding(new Placement(8, 7, Direction._270, Building.Black_Castle));
            base.placeBuilding(new Placement(4, 1, Direction._180, Building.White_Academy));
            base.placeBuilding(new Placement(1, 1, Direction._0, Building.Black_Infirmary));
            base.placeBuilding(new Placement(4, 8, Direction._90, Building.White_Manor));
            base.placeBuilding(new Placement(3, 7, Direction._270, Building.Black_Tower));
        }

        @Setup(Level.Invocation)
        public void setup(){
            test = base.copy();
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void benchmarkPlaceOnTenTurnsInBoard(TenTurnsInBoard board) {
        board.test.placeBuilding(board.placement);
    }

    @State(Scope.Benchmark)
    public static class FifteenTurnsInBoard {
        public static Board base = new Board();
        public static Placement placement = new Placement(8, 9, Direction._90, Building.Black_Bridge);

        public Board test;

        public FifteenTurnsInBoard(){
            base.placeBuilding(new Placement(3, 3, Direction._0, Building.Blue_Cathedral));
            base.placeBuilding(new Placement(8, 3, Direction._270, Building.Black_Academy));
            base.placeBuilding(new Placement(7, 1, Direction._0, Building.White_Castle));
            base.placeBuilding(new Placement(7, 5, Direction._90, Building.Black_Manor));
            base.placeBuilding(new Placement(6, 7, Direction._0, Building.White_Infirmary));

            base.placeBuilding(new Placement(8, 7, Direction._270, Building.Black_Castle));
            base.placeBuilding(new Placement(4, 1, Direction._180, Building.White_Academy));
            base.placeBuilding(new Placement(1, 1, Direction._0, Building.Black_Infirmary));
            base.placeBuilding(new Placement(4, 8, Direction._90, Building.White_Manor));
            base.placeBuilding(new Placement(3, 7, Direction._270, Building.Black_Tower));

            base.placeBuilding(new Placement(1, 6, Direction._0, Building.White_Abbey));
            base.placeBuilding(new Placement(1, 4, Direction._0, Building.Black_Abbey));
            base.placeBuilding(new Placement(5, 4, Direction._270, Building.White_Tower));
            base.placeBuilding(new Placement(0, 8, Direction._0, Building.Black_Square));
            base.placeBuilding(new Placement(9, 1, Direction._0, Building.White_Bridge));
        }

        @Setup(Level.Invocation)
        public void setup(){
            test = base.copy();
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void benchmarkPlaceOnFifteenTurnsInBoard(FifteenTurnsInBoard board) {
        board.test.placeBuilding(board.placement);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void benchmarkNewBoardAndFifteenTurns() {
        Board base = new Board();

        base.placeBuilding(new Placement(3, 3, Direction._0, Building.Blue_Cathedral));
        base.placeBuilding(new Placement(8, 3, Direction._270, Building.Black_Academy));
        base.placeBuilding(new Placement(7, 1, Direction._0, Building.White_Castle));
        base.placeBuilding(new Placement(7, 5, Direction._90, Building.Black_Manor));
        base.placeBuilding(new Placement(6, 7, Direction._0, Building.White_Infirmary));

        base.placeBuilding(new Placement(8, 7, Direction._270, Building.Black_Castle));
        base.placeBuilding(new Placement(4, 1, Direction._180, Building.White_Academy));
        base.placeBuilding(new Placement(1, 1, Direction._0, Building.Black_Infirmary));
        base.placeBuilding(new Placement(4, 8, Direction._90, Building.White_Manor));
        base.placeBuilding(new Placement(3, 7, Direction._270, Building.Black_Tower));

        base.placeBuilding(new Placement(1, 6, Direction._0, Building.White_Abbey));
        base.placeBuilding(new Placement(1, 4, Direction._0, Building.Black_Abbey));
        base.placeBuilding(new Placement(5, 4, Direction._270, Building.White_Tower));
        base.placeBuilding(new Placement(0, 8, Direction._0, Building.Black_Square));
        base.placeBuilding(new Placement(9, 1, Direction._0, Building.White_Bridge));
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void benchmarkNewBoardAndTenTurns() {
        Board base = new Board();

        base.placeBuilding(new Placement(3, 3, Direction._0, Building.Blue_Cathedral));
        base.placeBuilding(new Placement(8, 3, Direction._270, Building.Black_Academy));
        base.placeBuilding(new Placement(7, 1, Direction._0, Building.White_Castle));
        base.placeBuilding(new Placement(7, 5, Direction._90, Building.Black_Manor));
        base.placeBuilding(new Placement(6, 7, Direction._0, Building.White_Infirmary));

        base.placeBuilding(new Placement(8, 7, Direction._270, Building.Black_Castle));
        base.placeBuilding(new Placement(4, 1, Direction._180, Building.White_Academy));
        base.placeBuilding(new Placement(1, 1, Direction._0, Building.Black_Infirmary));
        base.placeBuilding(new Placement(4, 8, Direction._90, Building.White_Manor));
        base.placeBuilding(new Placement(3, 7, Direction._270, Building.Black_Tower));
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void benchmarkNewBoardAndFiveTurns() {
        Board base = new Board();

        base.placeBuilding(new Placement(3, 3, Direction._0, Building.Blue_Cathedral));
        base.placeBuilding(new Placement(8, 3, Direction._270, Building.Black_Academy));
        base.placeBuilding(new Placement(7, 1, Direction._0, Building.White_Castle));
        base.placeBuilding(new Placement(7, 5, Direction._90, Building.Black_Manor));
        base.placeBuilding(new Placement(6, 7, Direction._0, Building.White_Infirmary));
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public Board benchmarkNewBoardAndNoTurns() {
        return new Board();
    }

    private static Board boardToCopy = new Board();
    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public Board benchmarkCopyBoardAndNoTurns() {
        return boardToCopy.copy();
    }
}
