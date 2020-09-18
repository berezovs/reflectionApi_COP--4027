public class RandomGenerator {
    public static double generateRandomNumber(int min, int max){
        return  (Math.random() * (max - min + 1) + min);
    }
}
