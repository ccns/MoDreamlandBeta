package com.ccns.beta.modreamlandbeta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    Button btn;
    TextView tv;

    String user;
    String pw;
    boolean del;
    boolean auto;
    boolean remember;

    EditText userET;
    EditText pwET;
    CheckBox delCB;
    CheckBox autoCB;
    CheckBox rememberCB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.login);
        tv = (TextView) findViewById(R.id.tv);
        delCB = (CheckBox) findViewById(R.id.del);
        autoCB = (CheckBox) findViewById(R.id.auto);
        rememberCB = (CheckBox) findViewById(R.id.remember);
        userET = (EditText) findViewById(R.id.user);
        pwET = (EditText) findViewById(R.id.pw);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        user = sp.getString("user", "");
        pw = sp.getString("pw", "");
        del = sp.getBoolean("del", false);
        auto = sp.getBoolean("auto", false);
        remember = sp.getBoolean("remember", false);

        userET.setText(user);
        pwET.setText(pw);
        delCB.setChecked(del);
        autoCB.setChecked(auto);
        rememberCB.setChecked(remember);

        if(auto) {
            remember = true;
            rememberCB.setChecked(remember);
            rememberCB.setEnabled(false);
        }

        if(auto) {
            user = userET.getText().toString();
            pw = pwET.getText().toString();
            del = delCB.isChecked();
            auto = autoCB.isChecked();
            remember = rememberCB.isChecked();
            Intent i = new Intent(MainActivity.this,Dreamland.class);
            Bundle b = new Bundle();
            b.putBoolean("del",del);
            b.putString("user",user);
            b.putString("pw",pw);
            i.putExtras(b);
            startActivityForResult(i,123);
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = userET.getText().toString();
                pw = pwET.getText().toString();
                del = delCB.isChecked();
                auto = autoCB.isChecked();
                remember = rememberCB.isChecked();
                Intent i = new Intent(MainActivity.this,Dreamland.class);
                Bundle b = new Bundle();
                b.putBoolean("del",del);
                b.putString("user",user);
                b.putString("pw",pw);
                i.putExtras(b);
                startActivityForResult(i,123);
            }
        });

        autoCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    remember = true;
                    rememberCB.setChecked(remember);
                    rememberCB.setEnabled(false);
                } else {
                    rememberCB.setEnabled(true);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        switch(resultCode){
            case 123:
                int state = data.getExtras().getInt("state");
                switch(state){
                    case 0:
                        MainActivity.this.finish();
                        break;
                    case 1:
                        tv.setText("錯誤的使用者代號");
                        break;
                    case 2:
                        tv.setText("密碼輸入錯誤");
                        break;
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if(remember)
            sp.edit().putString("user",userET.getText().toString())
                    .putString("pw",pwET.getText().toString())
                    .apply();
        sp.edit().putBoolean("del",delCB.isChecked())
                .putBoolean("auto",autoCB.isChecked())
                .putBoolean("remember",rememberCB.isChecked())
                .apply();
    }
}
