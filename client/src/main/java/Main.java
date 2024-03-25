import client.Repl;
//import server.Server;
public class Main {
    public static void main(String[] args) {
        //Server s = new Server();
        //int p = s.run(0);
        Repl repl = new Repl(0);
        repl.loginScreen();
    }
}
