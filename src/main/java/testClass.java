import com.ebot.MikoBot.MainClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class testClass {
    public static void main(String[] args){
        try {
            run("git init");
            run("git add .");
            run("git commit -m \"first commit\"");
            run("git remote add origin https://github.com/exos288/miko-bot-data.git");
            run("git push -u origin master");
            run("git add .");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public static void run(String command) throws IOException, InterruptedException {
        Process proc = Runtime.getRuntime().exec(command);
        // Read the output
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(proc.getInputStream()));

        String line = "";
        while((line = reader.readLine()) != null) {
            System.out.print(line + "\n");
        }
        proc.waitFor();
    }
}
