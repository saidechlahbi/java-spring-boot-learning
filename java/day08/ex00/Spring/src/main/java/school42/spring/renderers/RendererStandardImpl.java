package school42.spring.renderers;

import  school42.spring.preprocessors.PreProcessor;
public class RendererStandardImpl implements Renderer {
    private final PreProcessor PreProcessor;
    public RendererStandardImpl(PreProcessor PreProcessor)
    {
        this.PreProcessor = PreProcessor;
    }
    public void render(String message)
    {
        String result = PreProcessor.process(message);
        System.out.println(result);
    }
}
