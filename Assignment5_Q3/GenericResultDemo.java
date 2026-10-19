import java.util.Objects;
import java.util.Optional;

class Result<T extends Number> {
    private final T marks;
    private final double maximumMarks;
    private final double passPercentage;

    public Result(T marks, double maximumMarks, double passPercentage) {
        this.marks = Objects.requireNonNull(marks, "Marks must not be null.");
        if (maximumMarks <= 0) {
            throw new IllegalArgumentException("Maximum marks must be positive.");
        }
        if (passPercentage < 0 || passPercentage > 100) {
            throw new IllegalArgumentException("Pass percentage must be between 0 and 100.");
        }
        this.maximumMarks = maximumMarks;
        this.passPercentage = passPercentage;
    }

    public double getPercentage() {
        return marks.doubleValue() / maximumMarks * 100.0;
    }

    public boolean hasPassed() {
        return getPercentage() >= passPercentage;
    }

    public Optional<T> getMarks() {
        return Optional.ofNullable(marks);
    }

    public void displayResult() {
        getMarks().ifPresent(value -> {
            System.out.println("Marks: " + value);
            System.out.printf("Percentage: %.2f%%%n", getPercentage());
            System.out.println("Result: " + (hasPassed() ? "Pass" : "Fail"));
        });
    }
}

public class GenericResultDemo {
    public static void main(String[] args) {
        Result<Integer> student1 = new Result<>(78, 100, 40);
        Result<Double> student2 = new Result<>(32.5, 50, 40);

        System.out.println("Student 1:");
        student1.displayResult();

        System.out.println("\nStudent 2:");
        student2.displayResult();

        // Objects.requireNonNull rejects a null marks value.
        try {
            Result<Integer> invalid = new Result<>(null, 100, 40);
            invalid.displayResult();
        } catch (NullPointerException exception) {
            System.out.println("\nInput validation: " + exception.getMessage());
        }
    }
}
