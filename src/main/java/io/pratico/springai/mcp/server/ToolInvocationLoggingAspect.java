package io.pratico.springai.mcp.server;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Emits a structured trace of every MCP tool call: which tool, which arguments,
 * outcome and elapsed time. Gives a clear reasoning trail to diagnose how an
 * MCP client is using the server without pulling in the full observability stack.
 */
@Aspect
@Component
public class ToolInvocationLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(ToolInvocationLoggingAspect.class);

    @Around("@annotation(org.springaicommunity.mcp.annotation.McpTool)")
    public Object aroundToolInvocation(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String toolName = signature.getDeclaringType().getSimpleName() + "#" + signature.getName();
        String formattedArgs = Arrays.toString(joinPoint.getArgs());

        log.info("[MCP-TOOL-CALL] {} args={}", toolName, formattedArgs);
        long startNanos = System.nanoTime();
        try {
            Object result = joinPoint.proceed();
            long elapsedMs = (System.nanoTime() - startNanos) / 1_000_000;
            log.info("[MCP-TOOL-OK ] {} elapsedMs={} result={}", toolName, elapsedMs, summarize(result));
            return result;
        } catch (Throwable ex) {
            long elapsedMs = (System.nanoTime() - startNanos) / 1_000_000;
            log.warn("[MCP-TOOL-ERR] {} elapsedMs={} error={}: {}",
                    toolName, elapsedMs, ex.getClass().getSimpleName(), ex.getMessage());
            throw ex;
        }
    }

    private static String summarize(Object result) {
        if (result == null) return "null";
        String text = String.valueOf(result);
        return text.length() > 280 ? text.substring(0, 280) + "...(truncated)" : text;
    }
}
