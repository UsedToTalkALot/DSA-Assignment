import java.util.ArrayList;
import java.util.Collections;

public class ScoreTracker {

    ArrayList<Double> marks = new ArrayList<Double>();


    void addScore(double score){
        marks.add(score);
    }

    double getMedianScore(){
        Collections.sort(marks); //we need to sort first
        
        int n = marks.size();
        if(n%2 == 0){
            return (marks.get(n/2)+marks.get((n/2)-1))/2; // median = ((n/2) th item + (n/2)+1 th item)/2 //-1 because indexing starts from 0
        }
        else{
            return marks.get((n/2));
        }
    }

    public static void main(String[] args) {
        ScoreTracker scoreTracker = new ScoreTracker();
        scoreTracker.addScore(85.5);
        scoreTracker.addScore(92.3);
        scoreTracker.addScore(77.8);
        scoreTracker.addScore(90.1);

        System.out.println("median score = "+scoreTracker.getMedianScore());
    }
    
    
}
