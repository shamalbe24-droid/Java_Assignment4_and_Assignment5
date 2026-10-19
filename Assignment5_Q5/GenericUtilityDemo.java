import java.util.Objects;
import java.util.Optional;

class GenericUtility {

    public static <T> boolean isEqual(T a, T b) {
        return Objects.equals(a, b);
    }

    public static <T> T getDefault(Optional<T> value, T defaultValue) {
        Objects.requireNonNull(value, "Optional value must not be null.");
        return value.orElse(defaultValue);
    }

    public static <T extends Comparable<? super T>> T max(T a, T b) {
        Objects.requireNonNull(a, "First value must not be null.");
        Objects.requireNonNull(b, "Second value must not be null.");
        return a.compareTo(b) >= 0 ? a : b;
    }

    public static boolean isNull(Object obj) {
        return Objects.isNull(obj);
    }
}

public class GenericUtilityDemo {
    public static void main(String[] args) {
        System.out.println("Are strings equal? "
                + GenericUtility.isEqual("Java", "Java"));
        System.out.println("Are integers equal? "
                + GenericUtility.isEqual(10, 20));

        Optional<String> present = Optional.of("Hello");
        Optional<String> absent = Optional.empty();
        System.out.println("Default with value present: "
                + GenericUtility.getDefault(present, "Default text"));
        System.out.println("Default with empty Optional: "
                + GenericUtility.getDefault(absent, "Default text"));

        System.out.println("Larger integer: " + GenericUtility.max(15, 27));
        System.out.println("Larger string: "
                + GenericUtility.max("apple", "orange"));

        String text = null;
        System.out.println("Is null? " + GenericUtility.isNull(text));
        System.out.println("Is null? " + GenericUtility.isNull("not null"));
    }
}
