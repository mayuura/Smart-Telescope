/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication1;

/**
 *
 * @author embouddi
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.JsonObject;
import com.google.gson.Gson;

public class CallPythonScript {
    public static List<String> call(double ra,double dec,int radius){
        List<String> scriptOutput = new ArrayList<>();
        try {
            // Define the input values (replace with your own values)
            /*double ra = 3908.4429453;
            double dec = -1.442668530641793;
            int radius=3;*/
            // Create a list to store the command and its arguments
            List<String> command = new ArrayList<>();
            command.add("C:\\Program Files\\Python311\\python.exe"); // Path to Python interpreter
            command.add("main.py"); // Your Python script filename
            command.add(String.valueOf(ra)); // Convert the double values to strings
            command.add(String.valueOf(dec));
            command.add(String.valueOf(radius));
            // Create a ProcessBuilder to run the Python script with arguments
            ProcessBuilder processBuilder = new ProcessBuilder(command);

            // Set the working directory for the Python script
            processBuilder.directory(new java.io.File("Z:\\Bureau\\CROC\\PIST\\script"));

            // Start the process
            Process process = processBuilder.start();
            
          
            // Read the output from the Python script
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                //System.out.println(line);
                scriptOutput.add(line);
            }

            // Wait for the process to finish
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Python script executed successfully.");
            } else {
                System.err.println("Python script execution failed with exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return scriptOutput;
    }
    public static void main(String[] args) {
        try {
            // Define the input values (replace with your own values)
            double ra = 3908.4429453;
            double dec = -1.442668530641793;
            int radius=3;
            // Create a list to store the command and its arguments
            List<String> command = new ArrayList<>();
            command.add("C:\\Program Files\\Python311\\python.exe"); // Path to Python interpreter
            command.add("main.py"); // Your Python script filename
            command.add(String.valueOf(ra)); // Convert the double values to strings
            command.add(String.valueOf(dec));
            command.add(String.valueOf(radius));
            // Create a ProcessBuilder to run the Python script with arguments
            ProcessBuilder processBuilder = new ProcessBuilder(command);

            // Set the working directory for the Python script
            processBuilder.directory(new java.io.File("Z:\\Bureau\\CROC\\PIST\\script"));

            // Start the process
            Process process = processBuilder.start();

            // Read the output from the Python script
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Wait for the process to finish
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Python script executed successfully.");
            } else {
                System.err.println("Python script execution failed with exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void processData(){
        //
    }
    public static JsonObject call2(double ra, double dec, int radius) {
    try {
        List<String> command = new ArrayList<>();
        command.add("C:\\Program Files\\Python311\\python.exe"); // Path to Python interpreter
        command.add("main.py"); // Your Python script filename
        command.add(String.valueOf(ra)); // Convert the double values to strings
        command.add(String.valueOf(dec));
        command.add(String.valueOf(radius));

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.directory(new java.io.File("Z:\\Bureau\\CROC\\PIST\\script"));
        Process process = processBuilder.start();

        // Read the output from the Python script as JSON
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder jsonOutput = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonOutput.append(line);
        }

        // Wait for the process to finish
        int exitCode = process.waitFor();
        if (exitCode == 0) {
            System.out.println("Python script executed successfully.");
        } else {
            System.err.println("Python script execution failed with exit code: " + exitCode);
            return null;
        }

        // Parse the JSON data in Java using Gson
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonOutput.toString(), JsonObject.class);

        return jsonObject;
    } catch (IOException | InterruptedException e) {
        e.printStackTrace();
        return null;
    }
    }
}



