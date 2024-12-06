package codeConstructsEvaluation.transformation;
import java.lang.instrument.Instrumentation;

/**
 * Evaluates size of Java object
 * 
 * @author Jakub Perdek, https://www.baeldung.com/java-size-of-object
 *
 */
public class ObjectSizeAnalyzer {
    private static volatile Instrumentation globalInstrumentation;

    public static void premain(String agentArgs, Instrumentation inst) {
        globalInstrumentation = inst;
    }

    public static long getObjectSize(final Object object) {
        if (globalInstrumentation == null) {
            throw new IllegalStateException("Agent not initialized.");
        }
        return globalInstrumentation.getObjectSize(object);
    }
}