package secretSharing;

import java.util.HashSet;
import java.util.Set;

public class Secret {
    Set<Integer> known = new HashSet<>();
    int individuals = -1;
    int first=-1;;
    int flag=0;

    void setIndividuals(int num){
        this.individuals = num;
    }
    void setFirstPerson(int index){
        this.first = index;
    }

    void addInterval(int start, int end){
        if(individuals == -1){
            System.out.println( "Error: Number of individuals not set" );
            return;
        }

        if(start == first && first>=0){ //reset the flag
            flag = 1;
        }

        if(flag == 0){//flag will be 0 until strting index is not the first person
            return;
        }

        if(start>individuals-1 || end>individuals-1){//fool proofing
            System.out.println("interval out of bound");
            return;
        }

        if( (start<0 || end<0) || (start>end) ){//fool proofing
            System.out.println( "Invalid Interval: ("+start+","+end+")");
            return ;
        
        }
        for (int i=start;i<=end;++i) {
            known.add(i);
        }

    }

    Set<Integer> whoKnows(){
        return known;
    }

    public static void main(String[] args) {
        Secret sc = new Secret();
        // sc.addInterval(0, 2); //error because interval not set
        sc.setIndividuals(5);
        sc.setFirstPerson(0);
        sc.addInterval(0, 2);
        sc.addInterval(1, 3);
        // sc.addInterval(2, 7); //error because more value than previously set interval
        System.out.println(sc.whoKnows());
        
    }


    
}
