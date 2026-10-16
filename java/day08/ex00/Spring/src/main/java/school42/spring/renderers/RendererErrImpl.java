
package school42.spring.renderers;

import  school42.spring.preprocessors.PreProcessor;

public class RendererErrImpl implements Renderer {
    private final PreProcessor PreProcessor;
    public RendererErrImpl(PreProcessor PreProcessor)
    {
        this.PreProcessor = PreProcessor;
    }
    public void render(String message)
    {
        String result = PreProcessor.process(message);
        System.err.println(result);
    }
}
