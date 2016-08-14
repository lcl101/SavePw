package com.aoki.savepw;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.aoki.savepw.data.Data;
import com.aoki.savepw.data.Password;
import com.aoki.savepw.excepton.BaseExceptoin;
import com.aoki.savepw.utils.FileUtil;
import com.aoki.savepw.utils.NetUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aoki on 15-12-22.
 */
public class ViewPwdActivity extends Activity {
    private Button btSave;
    private Button btAdd;
    private Button btBackup;
    private List<Map<String, Object>> listData;
    private ListView listView;
    private SimpleAdapter adapter;
    private long lastTouch;
    private int checkMark = 0;

    private Handler uiHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_view_pwd);

        btSave = (Button)findViewById(R.id.btViewSave);
        btSave.setOnClickListener(new BtSaveClick());

        btAdd = (Button)findViewById(R.id.btViewAdd);
        btAdd.setOnClickListener(new BtAddClick());

        btBackup = (Button)findViewById(R.id.btBackup);
        btBackup.setOnClickListener(new BtBackup());

        FileUtil.readData();
        addListView();
        lastTouch = System.currentTimeMillis();

        //message
        uiHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case 1:
                        Toast.makeText(ViewPwdActivity.this,"备份成功", Toast.LENGTH_LONG).show();
                        break;
                    case -1:
                        Toast.makeText(ViewPwdActivity.this,"备份失败！", Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        addListView();
                        adapter.notifyDataSetChanged();
                        Toast.makeText(ViewPwdActivity.this,"恢复成功", Toast.LENGTH_LONG).show();
                        break;
                    case -2:
                        Toast.makeText(ViewPwdActivity.this,"恢复失败", Toast.LENGTH_LONG).show();
                        break;

                }
            }
        };
    }

    private void addListView() {
        listView = (ListView) this.findViewById(R.id.lvViewList);
//        listView.removeAllViews();
        listData = new ArrayList<Map<String, Object>>();
        for (Password p1 : Data.getPwds()){
            Map<String,Object> m = new HashMap<String, java.lang.Object>();
            listData.add(m);
            m.put("desc",p1.getDesc());
        }
        adapter = new SimpleAdapter(this,listData,R.layout.view_pwd_item,
                new String[]{"desc"},
                new int[]{R.id.tvViewItemDesc});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new LvItemClick());
        listView.setOnItemLongClickListener(new LvItemlongClick());
    }

    ///////////////////////////
    public boolean dispatchTouchEvent(MotionEvent ev) {
        long ct = System.currentTimeMillis();
        if (ct < lastTouch+ Config.TOUCH_TIME){
            lastTouch = ct;
        }
        //这里事件开始交给Activity所附属的Window进行派发，如果返回true，整个事件循环就结束了
        //返回false意味着事件没人处理，所有人的onTouchEvent都返回了false，那么Activity就要来做最后的收场。
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        //这里，Activity来收场了，Activity的onTouchEvent被调用
        return onTouchEvent(ev);
    }

    public boolean onTouchEvent(MotionEvent ev) {
//        Toast.makeText(ViewPwdActivity.this, "yyyy "+ev.getAction(), Toast.LENGTH_LONG).show();
        //遮盖事件没有地方处理，交给此处处理
        return true;
    }
    //////////////////////////提示
    public boolean dispatchKeyEvent(KeyEvent event) {
//        Toast.makeText(ViewPwdActivity.this, "xxx"+event.getKeyCode(), Toast.LENGTH_LONG).show();
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getRepeatCount() == 0) {
                this.confirmExit();// 这是自定义的代码
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
    public void confirmExit(){
        if (Data.modify){
            AlertDialog ad = new  AlertDialog.Builder(this)
                    .setTitle("保存？" )
                    .setIcon(R.mipmap.ic_launcher)
                    .setMessage("是否需要保存吗？" )
                    .setPositiveButton("是" ,  new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                FileUtil.writeData();
                                Toast.makeText(ViewPwdActivity.this, "恭喜，保存成功了", Toast.LENGTH_LONG).show();
                            }
                            catch (BaseExceptoin e){
                                Toast.makeText(ViewPwdActivity.this, "难过了，保存失败\n"+e.getErrorCode()+"-"+e.getErrorMsg(), Toast.LENGTH_LONG).show();
                            }
                        }
                    } )
                    .setNegativeButton("否" , null)
                    .show();
        }
    }

    //////////////////////////////////add button////////////////////
    private class BtAddClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if (!checkEnKey()){
                return;
            }
            showAddDialog(view);
        }
    }
    protected void showAddDialog(final View view) {
        showContextDialog(view,-1);
    }

    protected void showContextDialog(final View view, final int dataIndex) {
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.view_pwd_input_dialog, null);
        final EditText dialogDesc = (EditText) textEntryView.findViewById(R.id.itDialogDesc);
        final EditText dialogContext = (EditText)textEntryView.findViewById(R.id.itDialogContext);
        Password p = null;
        if (dataIndex > -1) {
            p = Data.get(dataIndex);
            dialogDesc.setText(p.getDesc());
            dialogContext.setText(p.getContext());
        } else {
            p = new Password();
        }
        final Password fp = p;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("增加密码:");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setView(textEntryView);
        builder.setNegativeButton("关闭", null);
        builder.setPositiveButton("确定",null);
        final AlertDialog ad = builder.create();

        ad.setCanceledOnTouchOutside(false);
        ad.setCancelable(false);
        ad.show();

        ad.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String desc = dialogDesc.getText().toString();
                if (null == desc || desc.trim().length()<1){
                    Toast.makeText(view.getContext(), "主题不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                String context = dialogContext.getText().toString();
                if (null == context || context.trim().length()<1){
                    Toast.makeText(view.getContext(), "内容不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                fp.setContext(context);
                fp.setDesc(desc);
                if (dataIndex == -1) {
                    Data.add(fp);
                    Map<String,Object> m = new HashMap<String, Object>(1);
                    m.put("desc",desc);
                    listData.add(m);
                } else {
                    listData.get(dataIndex).put("desc", desc);
                }
                adapter.notifyDataSetChanged();
                Toast.makeText(view.getContext(), dialogDesc.getText().toString(), Toast.LENGTH_LONG).show();
                ad.dismiss();
            }
        });
    }

    //////////////////////////list view/////////////////////////////
    private class LvItemlongClick implements AdapterView.OnItemLongClickListener{
        @Override
        public boolean onItemLongClick(final AdapterView<?> adapterView, final View view, final int index, long l) {
            if (!checkEnKey()){
                return false;
            }
            AlertDialog.Builder bud = new AlertDialog.Builder(view.getContext());
            bud.setIcon(R.mipmap.ic_launcher);
            bud.setTitle("编辑密码");
            bud.setMessage("编辑["+Data.get(index).getDesc()+"]密码？" );
            bud.setNegativeButton("编辑" ,  new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    showContextDialog(view,index);
                }
            } );
            bud.setNeutralButton("删除", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    listData.remove(index);
                    Data.remove(index);
                    adapter.notifyDataSetChanged();
                }
            } );
            bud.setPositiveButton("关闭" , null);
            bud.create().show();

            return true;
        }
    }

    private boolean checkEnKey(){
        checkMark = 0;
        if (System.currentTimeMillis()>lastTouch+Config.TOUCH_TIME){
            final EditText et = new EditText(this);
            et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("请输入密码");
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setView(et);
            builder.setNegativeButton("关闭", null);
            builder.setPositiveButton("确定",null);
            final AlertDialog ad = builder.create();

            ad.setCanceledOnTouchOutside(false);
            ad.setCancelable(false);
            ad.show();
            ad.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkMark = 2;
                }
            });
            ad.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String pw = et.getText().toString();
                    if (null == pw || "".equals(pw)){
                        Toast.makeText(view.getContext(), "请输入密码", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (pw.equals(Config.dataBean.getPwd())){
                        checkMark = 1;
                        lastTouch = System.currentTimeMillis();
                        ad.dismiss();
                    }
                    else {
                        Toast.makeText(view.getContext(), "密码错误", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            });
            return checkMark == 1;
        }
        else {
            checkMark = 1;
            return true;
        }
    }

    private class LvItemClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //Toast.makeText(view.getContext(), "click:"+i+"===="+l, Toast.LENGTH_LONG).show();
            if (!checkEnKey()){
//                Toast.makeText(view.getContext(), "false==========", Toast.LENGTH_LONG).show();
                return;

            }
//            Toast.makeText(view.getContext(), "true==========", Toast.LENGTH_LONG).show();
            new  AlertDialog.Builder(view.getContext())
                    .setTitle("密码--"+ Data.get(i).getDesc())
                    .setIcon(R.mipmap.ic_launcher)
                    .setMessage(Data.get(i).getContext())
                    .setPositiveButton("确定" ,  null )
                    .show();
        }
    }

    private class BtSaveClick implements View.OnClickListener {
        public void onClick(View v) {
            if (!Data.checkEnKey()){
                return;
            }
            if (!checkEnKey()){
                return;
            }
            try {
                FileUtil.writeData();
                Toast.makeText(v.getContext(), "恭喜，保存成功了", Toast.LENGTH_LONG).show();
            }
            catch (BaseExceptoin e){
                Toast.makeText(v.getContext(), "难过了，保存失败\n"+e.getErrorCode()+"-"+e.getErrorMsg(), Toast.LENGTH_LONG).show();
            }
        }
    }

    protected void showIpDailog(final View view) {
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.view_server_ip_input, null);
        final EditText serverIp = (EditText) textEntryView.findViewById(R.id.itDialogIp);
        serverIp.setText(Config.serverIp);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("同步数据:");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setView(textEntryView);
        builder.setNegativeButton("恢复", null);
        builder.setPositiveButton("备份",null);
        final AlertDialog ad = builder.create();

        ad.setCanceledOnTouchOutside(false);
        ad.setCancelable(false);
        ad.show();

        ad.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String ip = serverIp.getText().toString();
                if (null == ip || ip.trim().length()<1){
                    Toast.makeText(view.getContext(), "服务器ip不能为空！", Toast.LENGTH_LONG).show();
                    return;
                }
                Config.serverIp = ip;
                new Thread(){
                    @Override
                    public void run()
                    {
                        boolean b = NetUtil.backup(Config.serverIp,Config.PORT);
                        Message message=new Message();
                        if (b) {
                            message.what = 1;
                        } else {
                            message.what = -1;
                        }
                        uiHandler.sendMessage(message);
                    }
                }.start();
                ad.dismiss();
            }
        });

        ad.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String ip = serverIp.getText().toString();
                if (null == ip || ip.trim().length()<1){
                    Toast.makeText(view.getContext(), "服务器ip不能为空！", Toast.LENGTH_LONG).show();
                    return;
                }
                Config.serverIp = ip;
                new Thread(){
                    @Override
                    public void run()
                    {
                        boolean b = NetUtil.read(Config.serverIp,Config.PORT);
                        Message message=new Message();
                        if (b) {
                            message.what = 2;
                        } else {
                            message.what = -2;
                        }
                        uiHandler.sendMessage(message);
                    }
                }.start();
                ad.dismiss();
            }
        });
    }

    private class BtBackup implements View.OnClickListener {
        public void onClick(View v) {
            showIpDailog(v);
        }
    }

}
