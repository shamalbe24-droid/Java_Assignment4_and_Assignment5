public class GenericArraySum {

    public static <T extends Number> double sum(T[] numbers) {
        double total = 0.0;
        for (T number : numbers) {
            total += number.doubleValue();
        }
        return total;
    }

    public static void main(String[] args) {
        Integer[] integers = {10, 20, 30, 40};
        Float[] floats = {1.5f, 2.5f, 3.0f};
        Double[] doubles = {2.25, 4.75, 8.0};

        System.out.println("Sum of integers: " + sum(integers));
        System.out.println("Sum of floats: " + sum(floats));
        System.out.println("Sum of doubles: " + sum(doubles));
    }
}
