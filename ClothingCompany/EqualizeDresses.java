package ClothingCompany;


public class EqualizeDresses {

    public static int minimumMovesToEqualize(int[] sewingMachines) {
        int totalDresses = 0;
        int machinesCount = sewingMachines.length;
       
        for (int dresses : sewingMachines) {//for each loop
            totalDresses += dresses;
        }
        if (totalDresses % machinesCount != 0) {
            return -1;//if sum cant be devided by n we cant equalize dresses
        }
        int targetDresses = totalDresses / machinesCount;
        int moves = 0;

        for (int dresses : sewingMachines) {
            int diff = dresses - targetDresses;
            if (diff > 0) {
                moves += diff;
            }
        }
        return moves;
    }

    public static void main(String[] args) {
        int[] sewingMachines = { 1, 0, 5 };
        System.out.println("Minimun moves = " + minimumMovesToEqualize(sewingMachines));
    }
}


