package com.aoki.savepw;

import android.app.Activity;
import android.content.Intent;
//import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aoki.savepw.utils.FileUtil;
//import android.widget.Toast;

public class MainActivity extends Activity {
    private Button btOk = null;
    private EditText itPw = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btOk = (Button)findViewById(R.id.btLoginOk);
        itPw = (EditText)findViewById(R.id.itLoginPw);

        btOk.setOnClickListener(new BtOkClick());
    }

    private class BtOkClick implements View.OnClickListener {

        public void onClick(View v) {
            Toast.makeText(v.getContext(), FileUtil.getSDPath(), Toast.LENGTH_LONG).show();
            String pwd= itPw.getText().toString();
            if (null == pwd || pwd.trim().length()<1){
                Toast.makeText(v.getContext(), "密码不能为空！", Toast.LENGTH_LONG).show();
                return;
            }
            pwd = pwd.trim();
            if (pwd.length()>8){
                pwd = pwd.substring(0,8);
            }
            else if (pwd.length()<8) {
                for (int i = 0; i < 8 - pwd.length(); i++){
                    pwd = pwd+Config.DEFAULT_PWD.charAt(i);
                }
            }
            Config.dataBean.setPwd(pwd);
            Config.encryBean.makeKey(pwd);
            itPw.getText().clear();
            Intent viewpwd = new Intent();
            viewpwd.setClass(MainActivity.this,ViewPwdActivity.class);
            startActivity(viewpwd);
        }
    }
}
