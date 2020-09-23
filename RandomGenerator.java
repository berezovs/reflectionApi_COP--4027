/*
Author: Serghei Berezovschi
Project: Reflection(Project1)
Class: COP--4027
*/
public class RandomGenerator {
    //generates a random double in the specified range
    public static double generateRandomNumber(int min, int max){
        return  (Math.random() * (max - min + 1) + min);
    }
}
