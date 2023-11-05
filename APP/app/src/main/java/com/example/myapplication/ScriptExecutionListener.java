package com.example.myapplication;

import java.util.List;

public interface ScriptExecutionListener {
    void onScriptExecuted(List<String> output, int exitCode);
}
