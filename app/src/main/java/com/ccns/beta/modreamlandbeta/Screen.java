package com.ccns.beta.modreamlandbeta;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Screen implements Serializable {
    char[][] screen;
    int row;
    int col;
    String temp2;

    public Screen() {
        screen = new char[50][100];
        row = 0;
        col = 0;
        temp2 = "";
    }
    // put char at (row,col)
    public void input(char c) {
        if(c > 128) {
            screen[row][col] = '\u0000';
            col++;
        }
        if(row<24 && col<80) {
            screen[row][col] = c;
            col++;
        } else if(col==100) {
            Log.i("Warning","column overflow");
            //lf();
        } else if (row==50) {
            Log.i("Warning","row overflow");
            clean();
        }
    }
    // line feed
    public void lf(){
        col=0;
        row++;
        if(row==24) {
            for(int i=0;i<24;i++) {
                System.arraycopy(screen[i + 1], 0, screen[i], 0, 80);
            }
            row=23;
        }
    }
    // carriage return
    public void cr() {
        col=0;
    }
    // clean screen
    public void clean(){
        screen = new char[50][100];
        row = 0;
        col = 0;
    }
    // move cursor
    public void moveto(int r,int c){
        row = r;
        col = c;
    }
    // K operate
    public void K(){
        for(int i=col;i<80;i++)
            screen[row][i]=' ';
    }// line feed
    public void L(int n){
        for(int k=0;k<n;k++) {
            for (int i = 23; i > 0; i--) {
                System.arraycopy(screen[i - 1], 0, screen[i], 0, 80);
            }
            screen[0] = new char[100];
        }
    }
    // print screen to terminate
    public void printScreen(){
        Log.i("/: ","／／／／／／／／／／／／／／／／／／／／／／／／／／／／／／／／／／／／／／／／");
        for(int i=0;i<24;i++){
            Log.i(String.valueOf(i+1)+": ",new String(screen[i],0,80));
        }
        Log.i("/: ","／／／／／／／／／／／／／／／／／／／／／／／／／／／／／／／／／／／／／／／／");
    }
    // escape operate
    public boolean esc(char temp){
        temp2 += temp;
        switch(temp){
            case 'K':
                Log.i("esc-K", temp2);
                K();
                temp2 = "";
                return true;
            case 'm':
                //Log.i("esc-m", temp2);
                temp2 = "";
                return true;
            case 'H':
                Log.i("esc-H", temp2);
                int a = temp2.indexOf('[');
                int b = temp2.indexOf(';');
                int c = temp2.indexOf('H');
                int row = 0;
                int col = 0;
                if(a+1!=b && a!=-1 && b!=-1 && c!=-1) {
                    String srow = temp2.substring(a + 1, b);
                    row = Integer.valueOf(srow);
                }
                if(b+1!=c && a!=-1 && b!=-1 && c!=-1) {
                    String scol = temp2.substring(b + 1, c);
                    col = Integer.valueOf(scol);
                }
                moveto(row-1,col-1);
                temp2 = "";
                return true;
            case 'J':
                Log.i("esc-J", temp2);
                clean();
                temp2 = "";
                return true;
            case 'L':
                Log.i("esc-L", temp2);
                L(Integer.valueOf(temp2.substring(temp2.indexOf('[')+1, temp2.indexOf('L'))));
                temp2 = "";
                return true;
            default:
                return false;
        }
    }

    public boolean isLoginUser() {
        String str1 = "請輸入代號";
        String str  = new String(screen[21],0,10).replaceAll("\u0000","");
        //Log.i("check","str="+str+",str1="+str1);
        return str.equals(str1);
    }

    public boolean isLoginPw() {
        String str1 = "請輸入密碼";
        String str  = new String(screen[21],36,10).replaceAll("\u0000","");
        //Log.i("check","str="+str+",str1="+str1);
        return str.equals(str1);
    }

    public boolean isMenu() {
        String str1 = "主功能表";
        String str  = new String(screen[0],2,8).replaceAll("\u0000","");
        //Log.i("check","str="+str+",str1="+str1);
        return str.equals(str1);
    }

    public boolean isFavorite() {
        String str1 = "我的最愛";
        String str  = new String(screen[0],2,8).replaceAll("\u0000","");
        //Log.i("check","str="+str+",str1="+str1);
        return str.equals(str1);
    }

    public boolean isBoard() {
        String str1 = "看板列表";
        String str  = new String(screen[0],2,8).replaceAll("\u0000","");
        //Log.i("check","str="+str+",str1="+str1);
        return str.equals(str1);
    }

    public boolean isDoublelogin() {
        String str1 = "偵測到多重登入";
        String str  = new String(screen[23],0,14).replaceAll("\u0000","");
        //Log.i("check","str="+str+",str1="+str1);
        return str.equals(str1);
    }

    public boolean isWrongUser() {
        String str1 = "錯誤的使用者代號";
        String str  = new String(screen[23],4,16).replaceAll("\u0000","");
        //Log.i("check","str="+str+",str1="+str1);
        return str.equals(str1);
    }

    public boolean isWrongPw() {
        String str1 = "密碼輸入錯誤";
        String str  = new String(screen[23],4,12).replaceAll("\u0000","");
        //Log.i("check","str="+str+",str1="+str1);
        return str.equals(str1);
    }

    public boolean isFail() {
        String str1 = "偵測到登入失敗的記錄";
        String str  = new String(screen[23],0,20).replaceAll("\u0000","");
        //Log.i("check","str="+str+",str1="+str1);
        return str.equals(str1);
    }

    public boolean isArticleList() {
        String str1 = "板主";
        String str  = new String(screen[0],2,4).replaceAll("\u0000","");
        //Log.i("check","str="+str+",str1="+str1);
        return str.equals(str1);
    }

    public boolean isArticle() {
        String str1 = "瀏覽";
        String str  = new String(screen[23],2,4).replaceAll("\u0000","");
        //Log.i("check","str="+str+",str1="+str1);
        return str.equals(str1);
    }

    public int[] getPage(){
        int[] page = new int[2];
        String str = new String(screen[23],0,78).replaceAll("\u0000", "");
        int start = str.indexOf('第')+1;
        int end = str.indexOf('頁');
        try {
            String str1 = str.substring(start, end).replaceAll(" ", "");
            int slash = str1.indexOf('/');
            page[0] = Integer.valueOf(str1.substring(0, slash));
            page[1] = Integer.valueOf(str1.substring(slash + 1));
        } catch (Exception e) {
            return getPage();
        }
        return page;
    }

    public String getAuthor(){
        return new String(screen[0],7,56).replaceAll("\u0000","").replaceAll(" ", "");
    }

    public String getBoard(){
        String str = new String(screen[0],71,7).replaceAll("\u0000","").replaceAll(" ","");
        int s = str.indexOf("板");
        return str.substring(s + 1);
    }

    public String getTitle(){
        return new String(screen[1],7,71).replaceAll("\u0000","").replaceAll(" ", "");
    }

    public String getTime(){
        return new String(screen[2],7,71).replaceAll("\u0000","").trim();
    }

    public ArrayList<char[]> getContent(int start){
        ArrayList<char[]> contents = new ArrayList<>();
        for(int i=start;i<23;i++)
            contents.add(screen[i]);
        return contents;
    }

    public ArrayList<HashMap<String,String>> getFavoriteList() {
        ArrayList<HashMap<String,String>> strlist = new ArrayList<>();
        for(int i=3;i<23;i++){
            HashMap<String,String> data = new HashMap<>();
            if(screen[i][5]!=0 && screen[i][5]!=' ') {
                data.put("hot", new String(screen[i], 60, 4).replaceAll("\u0000", "").replaceAll(" ", ""));
                data.put("code", new String(screen[i], 9, 12).replaceAll("\u0000", "").replaceAll(" ", ""));
                data.put("title", new String(screen[i], 27, 32).replaceAll("\u0000", "").trim());
            } else
                break;
            strlist.add(data);
        }
        return strlist;
    }

    public ArticleList getArticleList() {
        ArticleList articleList = new ArticleList();
        int top = 0;
        for(int i=22;i>2;i--){
            int num = getNumAtLine(i);
            String title;
            String hot;
            String author;
            String time;
            if(num>=0) {
                title = new String(screen[i],30,49).replaceAll("\u0000","");
                author = new String(screen[i],17,13).replaceAll("\u0000", "");
                time = new String(screen[i],11,5).replaceAll("\u0000", "").replaceAll(" ", "");
                hot = new String(screen[i], 8, 3).replaceAll("\u0000", "").replaceAll(" ", "");
                if(num == 0) {
                    top++;
                    num = -top;
                }
                articleList.add(new Article(num,title,hot,author,time));
            }
        }
        return articleList;
    }

    public int getNumAtLine(int i) {
        char start = screen[i][5];
        if(start!=0 && start!=' ') {
            if (start == '★')
                return 0;
            else {
                try {
                    return Integer.valueOf(new String(screen[i], 1, 5).replaceAll("\u0000", "").replaceAll(" ", ""));
                } catch (NumberFormatException e) {
                    return -1;
                }
            }
        } else
            return -1;
    }

    //////////unused methods///////////
    // convert to string array
    public String[] toStringArray() {
        String[] ret = new String[screen.length];
        for (int i = 0; i < screen.length; i++) {
            ret[i] = "";
            for(int j = 0; j < screen[i].length; j++) {
                ret[i] += screen[i][j];
            }
        }
        return ret;
    }

    public boolean endOfContent() {
        String str1 = new String(screen[22],0,10).replaceAll("\u0000","");
        return str1.equals("※ Origin:");
    }

    public int getPercent(){
        return Integer.valueOf(new String(screen[23],18,3).replaceAll("\u0000","").replaceAll(" ",""));
    }

    public int[] getLineNum(){
        int[] num = new int[2];
        String str = new String(screen[23],0,78).replaceAll("\u0000","");
        int start = str.indexOf(':')+4;
        int end = str.indexOf('行');
        String str1 = str.substring(start,end).replaceAll(" ","");
        int slash = str1.indexOf('~');
        num[0]=Integer.valueOf(str1.substring(0,slash));
        num[1]=Integer.valueOf(str1.substring(slash+1));
        return num;
    }

    public String getLine(int index){
        return new String(screen[index], 0, 78).replaceAll("\u0000", "") + "\n";
    }
}
