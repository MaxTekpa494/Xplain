package fr.uge.Xplain.llm;

import io.smallrye.common.annotation.RunOnVirtualThread;

@RunOnVirtualThread
public record UserCompilerPayload(String userMessage, String compilerErrors, String model) { }
