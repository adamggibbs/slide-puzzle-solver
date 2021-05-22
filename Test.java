import java.util.concurrent.atomic.AtomicReference;

public class Test{
    public static void main(String[] args){
        AtomicReference<Puzzle> t = new AtomicReference<>();
        System.out.println(t);
        if(t.get() != null){
            Puzzle puzzle = new Puzzle(4,4);
            t.set(puzzle);
            System.out.println(t.get());

        }
        
        
    }
}