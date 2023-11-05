package com.example.myapplication;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DatabaseQueryTask extends AsyncTask<Double, Void, List<String>> {
    private ScriptExecutionListener listener;

    public DatabaseQueryTask(ScriptExecutionListener listener) {
        this.listener = listener;
    }

    @Override
    protected List<String> doInBackground(Double... params) {
        double ra = params[0];
        double dec = params[1];
        int radius = params[2].intValue(); // Assuming the third parameter is the radius

        List<String> scriptOutput = new ArrayList<>();
        int exitCode = -1; // Initialize exitCode to handle scenarios where it isn't assigned

        try {
            List<String> command = new ArrayList<>();
            command.add("python"); // Adjust for Python installation on Android
            command.add("@script/main.py"); // Your Python script filename
            command.add(String.valueOf(ra));
            command.add(String.valueOf(dec));
            command.add(String.valueOf(radius));

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            // Set the working directory for the Python script (adjust as per your path)
            processBuilder.directory(new java.io.File("C:\\Users\\El Mehdi\\Desktop\\PIST\\PIST\\PIST\\APP\\app\\src\\main\\script"));

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                scriptOutput.add(line);
            }

            exitCode = process.waitFor(); // Assign the exit code here

            if (exitCode == 0) {
                System.out.println("Python script executed successfully.");
            } else {
                System.err.println("Python script execution failed with exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        // After the execution, inform the listener
        listener.onScriptExecuted(scriptOutput, exitCode);

        return scriptOutput;
    }

    @Override
    protected void onPostExecute(List<String> result) {
        // Handle the result in the UI thread
        // Update your UI with the result here
    }
}
