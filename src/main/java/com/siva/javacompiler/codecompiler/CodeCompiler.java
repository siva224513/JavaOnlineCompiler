package com.siva.javacompiler.codecompiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.simple.JSONObject;

public class CodeCompiler {

    public void writeInFile(String filePath, String code) {
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(code.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public JSONObject run(String filePath, JSONObject in) throws IOException, InterruptedException {
        writeInFile(filePath + "/Main.java", (String) in.get("code"));
        JSONObject json = new JSONObject();
        File directory = new File(filePath.replace("G:", "g:").replace("/", "//"));

        String compileCommand = "javac Main.java";
        String executeCommand = "java -cp . Main";

        ProcessBuilder compilePb = new ProcessBuilder(compileCommand.split(" "));
        compilePb.directory(directory);
        Process compileProcess = compilePb.start();
        int compileExitCode = compileProcess.waitFor();

        if (compileExitCode == 0) {
            ProcessBuilder executePb = new ProcessBuilder(executeCommand.split(" "));
            executePb.directory(directory);
            Process executeProcess = executePb.start();

            int executeExitCode = executeProcess.waitFor();

            if (executeExitCode == 0) {
                json.put("output", printProcessOutput(executeProcess.getInputStream()));
                json.put("exitCode", executeExitCode);
            } else {
                json.put("error", printProcessOutput(executeProcess.getErrorStream()));
                json.put("exitCode", executeExitCode);
            }
        } else {
            json.put("compilationError", printProcessOutput(compileProcess.getErrorStream()));
            json.put("exitCode", compileExitCode);
        }
        return json;
    }

    private String printProcessOutput(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder res = new StringBuilder();
        String s;
        while ((s = br.readLine()) != null) {
            res.append(s).append("\n");
        }
        return res.toString();
    }
}
