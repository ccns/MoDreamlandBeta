package com.ccns.beta.modreamlandbeta;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;

public class Telnet extends Thread implements Serializable {
    public Socket socket;
    public BufferedReader in;
    public PrintStream out;
    public HashMap<String, String> controlSet;

    public Telnet(Socket s) throws Exception {
        socket = s;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "BIG5"));
        out = new PrintStream(socket.getOutputStream());
        ControlMes();
    }

    public void putUser(String user) {
        out.print(user);
        out.print(controlSet.get("^M"));
    }

    public void putPw(String pw) {
        out.print(pw);
        out.print(controlSet.get("^M"));
    }

    public void favorite() {
        out.print("F");
        out.print(controlSet.get("^M"));
    }

    public void board() {
        out.print("C");
        out.print(controlSet.get("^M"));
    }

    public void articleList(String code) {
        out.print("s");
        out.print(code);
        out.print(controlSet.get("^M"));
    }

    public void article(String num) {
        out.print(num);
        out.print(controlSet.get("^M"));
        out.print(controlSet.get("^M"));
    }

    public void topArticle(int num) {
        int i = -num;
        out.print("$");
        for(int j=1;j<i;j++)
            out.print(controlSet.get("^_U"));
        out.print(controlSet.get("^M"));
    }

    public void sentcmd(String cmd) {
        out.print(cmd);
    }

    private void ControlMes() {
        controlSet = new HashMap<String, String>();

        addControler("^A", "" + (char) 0x01);
        addControler("^B", "" + (char) 0x02);
        addControler("^C", "" + (char) 0x03);
        addControler("^D", "" + (char) 0x04);
        addControler("^E", "" + (char) 0x05);
        addControler("^F", "" + (char) 0x06);
        addControler("^G", "" + (char) 0x07);
        addControler("^H", "" + (char) 0x08);
        addControler("^I", "" + (char) 0x09);
        addControler("^J", "" + (char) 0x0a);
        addControler("^K", "" + (char) 0x0b);
        addControler("^L", "" + (char) 0x0c);
        addControler("^M", "" + (char) 0x0d);
        addControler("^N", "" + (char) 0x0e);
        addControler("^O", "" + (char) 0x0f);
        addControler("^P", "" + (char) 0x10);
        addControler("^Q", "" + (char) 0x11);
        addControler("^R", "" + (char) 0x12);
        addControler("^S", "" + (char) 0x13);
        addControler("^T", "" + (char) 0x14);
        addControler("^U", "" + (char) 0x15);
        addControler("^V", "" + (char) 0x16);
        addControler("^W", "" + (char) 0x17);
        addControler("^X", "" + (char) 0x18);
        addControler("^Y", "" + (char) 0x19);
        addControler("^Z", "" + (char) 0x1a);
        addControler("^[", "" + (char) 0x1b);
        addControler("^\\", "" + (char) 0x1c);
        addControler("^]", "" + (char) 0x1d);
        addControler("^-", "" + (char) 0x1f);

        addControler("^_U", "" + (char) 27 + (char) 79 + (char) 65);
        addControler("^_D", "" + (char) 27 + (char) 79 + (char) 66);
        addControler("^_L", "" + (char) 27 + (char) 79 + (char) 68);
        addControler("^_R", "" + (char) 27 + (char) 79 + (char) 67);
        addControler("^_HOME", "" + (char) 27 + (char) 91 + '1' + (char) 126);
        addControler("^_INS", "" + (char) 27 + (char) 91 + '2' + (char) 126);
        addControler("^_DEL", "" + (char) 27 + (char) 91 + '3' + (char) 126);
        addControler("^_END", "" + (char) 27 + (char) 91 + '4' + (char) 126);
        addControler("^_PGUP", "" + (char) 27 + (char) 91 + '5' + (char) 126);
        addControler("^_PGDN", "" + (char) 27 + (char) 91 + '6' + (char) 126);

        /* 鍵盤設定 *
         KEY_TAB 9
         KEY_ENTER 10
         KEY_ESC 27
         KEY_UP -1
         KEY_DOWN -2
         KEY_RIGHT -3
         KEY_LEFT -4
         KEY_HOME -21
         KEY_INS -22
         KEY_DEL -23
         KEY_END -24
         KEY_PGUP -25
         KEY_PGDN -26
         I_TIMEOUT -31
         I_OTHERDATA -32
         Ctrl(c) ( c & 037 )
         Meta(c) ( c + 0x0200 )
         isprint2(c) ((c >= ' ')) /* ((c & 0x80 || isprint(c))) */

    }

    private void addControler(String key, String value) {
        controlSet.put(key, value);
    }
}

