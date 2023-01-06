package shop.yesaladin.shop.category.exception;

import java.util.stream.Collectors;
import org.springframework.validation.BindingResult;

public class ValidationFailedException extends RuntimeException {
    public ValidationFailedException(BindingResult bindingResult) {
        super(bindingResult.getAllErrors()
                .stream()
                .map(error -> new StringBuilder()
                        .append("Message=").append(error.getDefaultMessage()).append("\n"))
                .collect(Collectors.joining(" | ")));
    }
}
