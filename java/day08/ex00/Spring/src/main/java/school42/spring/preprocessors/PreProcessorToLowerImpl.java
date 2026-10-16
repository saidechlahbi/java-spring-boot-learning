package school42.spring.preprocessors;

import java.util.Locale;
public class PreProcessorToLowerImpl implements PreProcessor {
    @Override 
    public String process(String message)
    {
        return message.toLowerCase(Locale.ROOT);
    }
}
