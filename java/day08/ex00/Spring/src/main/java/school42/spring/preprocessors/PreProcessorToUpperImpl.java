package school42.spring.preprocessors;

import java.util.Locale;

public class PreProcessorToUpperImpl implements PreProcessor {
    @Override
    public String process(String message) {
        return message.toUpperCase(Locale.ROOT);
    }
}