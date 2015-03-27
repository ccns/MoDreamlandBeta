package com.ccns.beta.modreamlandbeta;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


public class Dreamland extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, ArticleListFragment.OnFragmentInteractionListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    final static int STATE_WELCOME = 0;
    final static int STATE_ARTICLE_LIST = 1;
    final static int STATE_ARTICLE = 2;
    final static int WAIT = 2;

    public static Socket socket = null;
    public static Telnet telnet = null;
    public static Screen screen = null;
    String url = "ccns.cc";
    public static int stime = 0;
    boolean timerflag = false;
    int state;

    telnetThread tt = null;

    Intent i;
    ArrayList<HashMap<String,String>> favolist;
    String board_code;
    public static BoardList boardList;
    private int topCount;

    ArticleListFragment listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dreamland);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, WelcomeFragment.newInstance(true,"登入中..."))
                .commit();
        state = STATE_WELCOME;

        //get login information from login activity
        i = getIntent();
        Bundle bi = i.getExtras();
        String user = bi.getString("user");
        String pw = bi.getString("pw");
        boolean del = bi.getBoolean("del");

        //initialize screen and timer
        screen = new Screen();
        Timer timer = new Timer();
        timer.schedule(task, 0, 1000);

        //login
        login(user, pw, del);
    }

    // board select
    @Override
    public void onNavigationDrawerItemSelected(String code) {
        // update the main content by replacing fragments
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, WelcomeFragment.newInstance(true,"載入文章列表..."))
                .commit();
        mTitle = "Loading...";
        restoreActionBar();
        new articleListThread(code,false).start();
    }

    // article select
    @Override
    public void onFragmentInteraction(int position,int state) {
        switch(state) {
            case ArticleListFragment.STATE_ITEM_CLICKED:
                ArticleList articles = boardList.getArticlesByCode(board_code);
                if (position == articles.size() - 1) {
                    articles.get(position).setTitle("載入中...");
                    listFragment.updateList(articles);
                    new articleListThread(board_code, true).start();
                } else {
                    Intent i = new Intent(Dreamland.this, ArticleActivity.class);
                    Bundle b = new Bundle();
                    Article a = articles.get(position);
                    b.putSerializable("article", a);
                    b.putString("boardCode", board_code);
                    i.putExtras(b);
                    startActivity(i);
                }
                break;
            case ArticleListFragment.STATE_REFRESH:
                new articleListThread(board_code,false,true).start();
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    protected void onDestroy() {
        tt.interrupt();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.dreamland, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        switch(state) {
            case STATE_ARTICLE:
                getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, listFragment)
                    .commit();
                state = STATE_ARTICLE_LIST;
                mTitle = board_code;
                restoreActionBar();
                break;
            case STATE_ARTICLE_LIST:
                if(mNavigationDrawerFragment.isDrawerOpen())
                    super.onBackPressed();
                else
                    mNavigationDrawerFragment.openTheDrawer();
                break;
            default:
                super.onBackPressed();
        }
    }

    public class telnetThread extends Thread {
        @Override
        public void run() {
            try {
                socket = new Socket(url, 23);
                telnet = new Telnet(socket);
                char temp;
                timerflag = true;
                while((temp=(char)telnet.in.read())!=-1){
                    stime = 0;

                    if(temp == 27){
                        //Log.i("Read","Esc at row" + String.valueOf(screen.row));
                        do {
                            temp=(char)telnet.in.read();
                            if(screen.esc(temp))
                                break;
                        }while(true);
                    } else if(temp == '\r') {
                        //Log.i("Read","Enter at row" + String.valueOf(screen.row));
                        screen.cr();
                    } else if(temp == '\n') {
                        //Log.i("Read","LF at row" + String.valueOf(screen.row));
                        screen.lf();
                    } else {
                        screen.input(temp);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Dreamland.this.finish();
            }
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class printThread extends Thread {
        @Override
        public void run() {
            while(stime<WAIT); // if stime < 5, do nothing
            screen.printScreen();
        }
    }

    public class loginThread extends Thread {
        String user;
        String pw;
        boolean del;
        public loginThread(String user,String pw,boolean del){
            this.user = user;
            this.pw = pw;
            this.del = del;
        }
        @Override
        public void run() {
            while(stime < WAIT);
            while(!screen.isLoginUser());
            telnet.putUser(user);
            while(stime < WAIT);
            while(true) {
                if(screen.isWrongUser()) {
                    Log.i("Login", "Wrong user.");
                    Message msg = new Message();
                    msg.what=1;
                    loginhandler.sendMessage(msg);
                    break;
                }else if (screen.isLoginPw()) {
                    telnet.putPw(pw);
                    break;
                }
            }
            while(stime < WAIT);
            while(true) {
                if (screen.isWrongPw()) {
                    Log.i("Login", "Wrong pw.");
                    Message msg = new Message();
                    msg.what = 2;
                    loginhandler.sendMessage(msg);
                    break;
                }else if (screen.isDoublelogin()) {
                    Log.i("Login","Double login.");
                    if (del)
                        telnet.sentcmd("Y");
                    else
                        telnet.sentcmd("N");
                    telnet.sentcmd(telnet.controlSet.get("^M"));
                } else if (screen.isFail()) {
                    Log.i("Login","Fail login history detected.");
                    telnet.sentcmd(telnet.controlSet.get("^M"));
                } else if (!screen.isMenu()) {
                    telnet.sentcmd(telnet.controlSet.get("^_L"));
                } else if (screen.isMenu()) {
                    Message msg = new Message();
                    msg.what = 0;
                    loginhandler.sendMessage(msg);
                    break;
                }
            }
        }



        Handler loginhandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //return login state
                Bundle br = new Bundle();
                br.putInt("state",msg.what);
                i.putExtras(br);
                setResult(123, i);
                TextView tv = (TextView) findViewById(R.id.tvw);
                switch (msg.what) {
                    case 0:
                        tv.setText("登入成功!等待看板載入");
                        new favoThread().start();
                        break;
                    case 1: // wrong username
                        Dreamland.this.finish();
                        break;
                    case 2: // wrong pw
                        Dreamland.this.finish();
                        break;
                    default:
                        tv.setText("What the fuck?!");
                        break;
                }
            }
        };
    }

    public class favoThread extends Thread {
        @Override
        public void run() {
            while(!screen.isMenu()) {
                telnet.sentcmd(telnet.controlSet.get("^_L"));
            }
            telnet.favorite();
            while(stime < WAIT);
            while(!screen.isFavorite());
            Log.i("Favorite", "Favorite list loaded.");
            Message msg = new Message();
            msg.what=1;
            favohandler.sendMessage(msg);
        }

        Handler favohandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                TextView tv = (TextView) findViewById(R.id.tvw);
                ProgressBar pb = (ProgressBar) findViewById(R.id.pb);
                switch (msg.what) {
                    case 1:
                        favolist = screen.getFavoriteList();
                        ArrayList<DrawerItem> drawerItems = regularList();
                        drawerItems.addAll(toDrawerItemList(favolist,"我的最愛"));
                        mNavigationDrawerFragment.getListView().setAdapter(new DrawerAdapter(
                                Dreamland.this,
                                drawerItems));
                        pb.setVisibility(View.GONE);
                        tv.setText("請點左上角選單選擇看板");
                        mNavigationDrawerFragment.openTheDrawer();
                        boardList = new BoardList();
                        break;
                    default:
                        break;
                }
            }
        };

        private ArrayList<DrawerItem> toDrawerItemList(ArrayList<HashMap<String,String>> list,String title) {
            ArrayList<DrawerItem> drawerItems = new ArrayList<>();
            drawerItems.add(new DrawerItem(title));
            for(int i = 0 ; i < list.size() ; i++) {
                drawerItems.add(new DrawerItem(list.get(i)));
            }
            return drawerItems;
        }

        private ArrayList<DrawerItem> regularList() {
            ArrayList<DrawerItem> drawerItems = new ArrayList<>();
            drawerItems.add(new DrawerItem("常用看板"));
            String[] code = { "shit" , "course" , "poor" , "market" , "NCKU_Work" , "House" , "FOODstuff"};
            String[] title = { "雪特板" , "選課板" , "瀑峨板" , "二手板" , "校內打工" , "租屋板" , "美食板"};
            for(int i = 0 ; i < code.length ; i++) {
                HashMap<String,String> data = new HashMap<>();
                data.put("hot","");
                data.put("code",code[i]);
                data.put("title",title[i]);
                drawerItems.add(new DrawerItem(data));
            }
            return drawerItems;
        }
    }

    public class articleListThread extends Thread {
        boolean append = false;
        boolean haveLoaded = false;
        boolean refresh = false;
        int position;
        int last;
        ArticleList articles;
        ArticleList nowList;
        public articleListThread(String code, boolean app) {
            board_code = code;
            append = app;
            if(boardList.getArticlesByCode(code) == null)
                boardList.addArticleListByCode(code);
            else
                haveLoaded = true;
            articles = boardList.getArticlesByCode(code);
        }
        public articleListThread(String code, boolean app, boolean refresh) {
            board_code = code;
            append = app;
            if(boardList.getArticlesByCode(code) == null)
                boardList.addArticleListByCode(code);
            else
                haveLoaded = true;
            articles = boardList.getArticlesByCode(code);
            this.refresh = refresh;
        }
        @Override
        public void run() {
            if(!append) {
                while (!screen.isMenu()) {
                    telnet.sentcmd(telnet.controlSet.get("^_L"));
                    while (stime < WAIT) ;
                }
                telnet.board();

                while (stime < WAIT) ;
                while (!screen.isBoard());
                Log.i("Board", "Board list loaded.");
                telnet.articleList(board_code);

                while (stime < WAIT) ;
                if(!haveLoaded || refresh) {
                    do {
                        telnet.sentcmd(telnet.controlSet.get("^_END"));
                        while(stime < WAIT);
                    } while (!screen.isArticleList());
                    Log.i("ArticleList", "Article list loaded.");
                    Message msg = new Message();
                    if(refresh) {
                        msg.what = 4;
                        telnet.sentcmd(telnet.controlSet.get("^_END"));
                        while(stime < WAIT);
                        while (!screen.isArticleList());
                    } else
                        msg.what = 1;
                    nowList = screen.getArticleList();
                    articleListHandler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = 3;
                    articleListHandler.sendMessage(msg);
                }
            } else {
                // if reading article, get out
                if(screen.isArticle())
                    telnet.sentcmd(telnet.controlSet.get("^_L"));
                while(stime < WAIT);
                while(!screen.isArticleList());

                // go to unloadd page
                last = articles.getLastArticle().getNum();
                int bottom,top;
                do {
                    bottom = screen.getNumAtLine(22);
                    top = screen.getNumAtLine(3);
                    telnet.sentcmd(telnet.controlSet.get("^_PGUP"));
                    while(stime < WAIT);
                    while(!screen.isArticleList());
                    while(screen.getNumAtLine(3) == top);
                } while( (screen.getNumAtLine(3) >= last && last>0) || screen.getNumAtLine(3) == 0) ;

                while(stime < WAIT);
                while(!screen.isArticleList());
                while(screen.getNumAtLine(22) == bottom);
                Log.i("Article", "Article list loaded.");
                nowList = topFix(screen.getArticleList());
                Message msg = new Message();
                msg.what = 2;
                articleListHandler.sendMessage(msg);
            }
        }

        Handler articleListHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Fragment fragment;
                FragmentManager fragmentManager = getSupportFragmentManager();
                switch (msg.what) {
                    case 1:
                        articles.addAll(nowList);
                        topCount = countTop(articles);
                        if(articles.size() < 20)
                            articles.add(0,"載入中...");
                        else
                            articles.add(0,"載入更多文章");
                        listFragment = ArticleListFragment.newInstance(position, articles);
                        fragment = listFragment;
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, fragment)
                                .commit();
                        state = STATE_ARTICLE_LIST;
                        mTitle = mNavigationDrawerFragment.mTitle;
                        restoreActionBar();
                        if(articles.size() < 20)
                            new articleListThread(board_code, true).start();
                        break;
                    case 2:
                        articles.remove(articles.size()-1);
                        articles.addAll(nowList);
                        articles.add(0,"載入更多文章");
                        topCount = countTop(articles);
                        listFragment.updateList(articles);
                        break;
                    case 3:
                        listFragment = ArticleListFragment.newInstance(position, articles);
                        fragment = listFragment;
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, fragment)
                                .commit();
                        state = STATE_ARTICLE_LIST;
                        mTitle = mNavigationDrawerFragment.mTitle;
                        restoreActionBar();
                        break;
                    case 4:
                        articles.clear();
                        articles.addAll(nowList);
                        topCount = countTop(articles);
                        if(articles.size() < 20)
                            articles.add(0,"載入中...");
                        else
                            articles.add(0,"載入更多文章");
                        listFragment.refreshList(articles);
                        listFragment.getPullToRefreshLayout().setRefreshComplete();
                        listFragment.setListShown(true);
                        if(articles.size() < 20)
                            new articleListThread(board_code, true).start();
                        break;
                    default:
                        break;
                }
            }
        };

        private ArticleList topFix(ArticleList list) {
            for(int i=0;i<list.size();i++){
                if(list.get(i).getNum()<0)
                    list.get(i).setNum(-topCount+list.get(i).getNum());
            }
            return list;
        }

        private int countTop(ArticleList list) {
            int count = 0;
            for(int i=0;i<list.size();i++){
                if(list.get(i).getNum()<0)
                    count++;
                else
                    break;
            }
            return count;
        }
    }

    private TimerTask task = new TimerTask(){
        @Override
        public void run() {
            if (timerflag){
                stime++;
            }
        }
    };

    public void login(String user, String pw, boolean del){
        if(tt!=null)
            tt.interrupt();
        tt = new telnetThread();
        tt.start();
        new loginThread(user, pw, del).start();
    }
}
