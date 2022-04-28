import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class MyClient7 {
  public static void main(String args[]) throws Exception {
    Socket socket = new Socket("127.0.0.1", 50000);
    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
    out.write("HELO\n".getBytes());
    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    String response = in.readLine();
    System.out.println("HELO response: " + response);
    String username = System.getProperty("user.name");
    String command = "AUTH " + username + "\n";
    out.write(command.getBytes());
    response = in.readLine();
    System.out.println("AUTH response: " + response);

    while (true) {
      out.write("REDY\n".getBytes());
      out.flush();
      String RedyMsg = in.readLine();
      System.out.println("REDY: " + RedyMsg);
      String[] REDYres = RedyMsg.split(" ");

      String REDYTitle = REDYres[0];
      String JobId = REDYres[2];
      String JobCore = REDYres[4];
      String JobMemory = REDYres[5];
      String JobDisk = REDYres[6];
      String GETCMD = "GETS Capable " + JobCore + " " + JobMemory + " " + JobDisk + "\n";

      if (REDYTitle.equals("NONE")) {
        break;
      }

      if (!REDYTitle.equals("NONE")) {
        out.write(GETCMD.getBytes());
        out.flush();
        String DATAString = in.readLine();
        String[] GETMsg = DATAString.split(" ");
        int lenght = Integer.parseInt(GETMsg[1]);

        out.write("OK\n".getBytes());
        out.flush();

        ArrayList<String> serverList = new ArrayList<String>();
        for (int i = 0; i < lenght; i++) {
          response = in.readLine();
          serverList.add(response);
        }

        String FC = serverList.get(0);
        String[] FCInfo = FC.split(" ");
        String serverType = FCInfo[0];
        String serverID = FCInfo[1];

        out.write("OK\n".getBytes());
        out.flush();

        String SCHDCMD = "SCHD" + " " + JobId + " " + serverType + " " + serverID + "\n";
        out.write(SCHDCMD.getBytes());
        out.flush();
        response = in.readLine();

      }

    }

    out.write("QUIT\n".getBytes());
    out.flush();
    out.close();
    socket.close();
  }

}
