package com.ccns.beta.modreamlandbeta;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class ArticleActivity extends ActionBarActivity implements ArticleFragment.OnFragmentInteractionListener {

    private Telnet telnet;
    private Screen screen;
    private int stime;

    private Article a;
    private int article_num;
    private String board_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        telnet = Dreamland.telnet;
        screen = Dreamland.screen;
        stime = Dreamland.stime;
        Intent i = getIntent();
        Bundle b = i.getExtras();
        a = (Article) b.getSerializable("article");
        board_code = b.getString("boardCode");
        article_num = a.getNum();
        if (savedInstanceState == null) {
            new articleThread(0).start();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_article, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        telnet.sentcmd(telnet.controlSet.get("^_L"));
        super.onBackPressed();
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

    // prev/next button press
    @Override
    public void onArticleFragmentInteraction(int move) {
        new articleThread(move).start();
    }

    public void restoreActionBar(String title) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(title);
    }

    public class articleThread extends Thread {
        int move = 0;
        Article article;
        public articleThread(int mov) {
            move = mov;
            article = new Article();
        }
        @Override
        public void run() {
            if(move == 0){ // first load
                if (screen.isArticle()) {
                    telnet.sentcmd(telnet.controlSet.get("^_L"));
                    while(stime < Dreamland.WAIT);
                }
                while (true) {
                    if (screen.isArticleList()) {
                        Log.i("ArticleList", "Article list loaded.");
                        if (article_num < 0)
                            telnet.topArticle(article_num);
                        else
                            telnet.article(String.valueOf(article_num));
                        while(stime < Dreamland.WAIT);
                        break;
                    }
                }
                while (true) {
                    if (screen.isArticle()) {
                        Log.i("Article", "Article loaded. Idle "+String.valueOf(stime)+" s.");
                        Message msg = new Message();
                        getArticle(true);
                        msg.what = 1;
                        articleHandler.sendMessage(msg);
                        break;
                    }
                }
            } else { // change page
                int[] page = screen.getPage();
                while(stime < Dreamland.WAIT);
                while(!screen.isArticle());
                switch(move) {
                    case 1:
                        if (page[0] != page[1])
                            telnet.sentcmd(telnet.controlSet.get("^_PGDN"));
                        break;
                    case -1:
                        if (page[0] != 1)
                            telnet.sentcmd(telnet.controlSet.get("^_PGUP"));
                        break;
                }
                while(stime < Dreamland.WAIT);
                while (true) {
                    int[] page1;
                    if (screen.isArticle()) {
                        // see if page complete loaded
                        if(page[0] == (page1=screen.getPage())[0])
                            continue;
                        Log.i("Article", "Article loaded. Idle "+String.valueOf(Dreamland.WAIT)+" s.");
                        Message msg = new Message();
                        if(page1[0]==1)
                            getArticle(true);
                        else
                            getArticle(false);
                        msg.what = 1;
                        articleHandler.sendMessage(msg);
                        break;
                    }
                }
            }
        }

        Handler articleHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int[] page = screen.getPage();
                switch (msg.what) {
                    case 1:
                        Bundle b = new Bundle();
                        b.putSerializable("article", article);
                        b.putIntArray("page", page);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, ArticleFragment.newInstance(b))
                                .commit();
                        if(article.getTitle() != null)
                            restoreActionBar(board_code + " - " + article.getTitle());
                        break;
                    case 2:
                        break;
                    default:
                        break;
                }
            }
        };

        public void getArticle(boolean first) {
            if(first) {
                article.setAuthor(screen.getAuthor());
                article.setBoard(board_code);
                article.setTitle(screen.getTitle());
                article.setTime(screen.getTime());
                article.addContent(screen.getContent(3));
            } else {
                article.addContent(screen.getContent(0));
            }
        }
    }
}
