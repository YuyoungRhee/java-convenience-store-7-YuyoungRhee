package store.util;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class RetryUtil {

    public static <T> T readValidatedInput(Supplier<T> inputSupplier, Function<T, T> parser, Consumer<T> validator) {
        while (true) {
            try {
                T input = inputSupplier.get();
                T parsedInput = parser.apply(input);
                validator.accept(parsedInput);
                return parsedInput;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
