import java.util.Optional;

public class OptionalEmail {
    public static void main(String[] args) {
        Optional<String> email = Optional.of("student@example.com");
        Optional<String> emptyEmail = Optional.empty();

        System.out.println("Email using ifPresent:");
        email.ifPresent(value -> System.out.println(value));

        String chosenEmail = emptyEmail.orElse("not-provided@example.com");
        System.out.println("Email using orElse: " + chosenEmail);

        try {
            String requiredEmail = emptyEmail.orElseThrow(
                    () -> new IllegalStateException("Email address is missing."));
            System.out.println("Required email: " + requiredEmail);
        } catch (IllegalStateException exception) {
            System.out.println("orElseThrow handled missing value: " + exception.getMessage());
        }

        // Demonstrate orElseThrow when a value is present as well.
        String presentEmail = email.orElseThrow(
                () -> new IllegalStateException("Email address is missing."));
        System.out.println("Present email using orElseThrow: " + presentEmail);
    }
}
