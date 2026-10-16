package school42.spring.printers;

import java.time.LocalDateTime;
import school42.spring.renderers.Renderer;

public class PrinterWithDateTimeImpl implements Printer {
    private final Renderer renderer;

    public PrinterWithDateTimeImpl(Renderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void print(String message) {
        LocalDateTime now = LocalDateTime.now();
        renderer.render(now + " " + message);
    }
}