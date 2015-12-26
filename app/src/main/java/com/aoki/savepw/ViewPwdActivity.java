package com.aoki.savepw;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import android.support.v7.app.ActionBarActivity;

/**
 * Created by aoki on 15-12-22.
 */
public class ViewPwdActivity extends Activity {
    private Button btSave;
    private Button btAdd;
    private List<Map<String, Object>> listData;
    private ListView listView;
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pwd);

        btSave = (Button)findViewById(R.id.btViewSave);
        btSave.setOnClickListener(new BtSaveClick());

        btAdd = (Button)findViewById(R.id.btViewAdd);
        btAdd.setOnClickListener(new BtAddClick());

        FileUtil.readData();
        listView = (ListView) this.findViewById(R.id.lvViewList);
        listData = new ArrayList<Map<String, Object>>();
        for (Password p1 : Data.getPwds()){
            Map<String,Object> m = new HashMap<String, java.lang.Object>();
            listData.add(m);
            m.put("desc",p1.getDeDesc());
        }
        adapter = new SimpleAdapter(this,listData,R.layout.view_pwd_item,
                new String[]{"desc"},
                new int[]{R.id.tvViewItemDesc});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new LvItemClick());
        listView.setOnItemLongClickListener(new LvItemlongClick());
    }
    //////////////////////////提示
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getRepeatCount() == 0) {
//                this.confirmExit();// 这是自定义的代码
                confirmExit();
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
            showAddDialog(view);
        }
    }
    protected void showAddDialog(final View view) {
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.view_pwd_input_dialog, null);
        final EditText dialogDesc = (EditText) textEntryView.findViewById(R.id.itDialogDesc);
        final EditText dialogContext = (EditText)textEntryView.findViewById(R.id.itDialogContext);
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
                Password p = new Password();
                p.setContext(Config.encryBean.en(context));
                p.setDeDesc(desc);
                Data.add(p);
                Map<String,Object> m = new HashMap<String, Object>(1);
                m.put("desc",desc);
                listData.add(m);
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
            //Toast.makeText(view.getContext(), "long click:"+i+"===="+l, Toast.LENGTH_LONG).show();
            AlertDialog ad = new  AlertDialog.Builder(view.getContext())
                    .setTitle("删除确认" )
                    .setIcon(R.mipmap.ic_launcher)
                    .setMessage("确定要删除"+Data.get(index).getDeDesc()+"吗？" )
                    .setPositiveButton("是" ,  new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            listData.remove(index);
                            Data.remove(index);
                            adapter.notifyDataSetChanged();
                        }
                    } )
                    .setNegativeButton("否" , null)
                    .show();
            return true;
        }
    }

    private class LvItemClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //Toast.makeText(view.getContext(), "click:"+i+"===="+l, Toast.LENGTH_LONG).show();
            new  AlertDialog.Builder(view.getContext())
                    .setTitle("密码--"+ Data.get(i).getDeDesc())
                    .setIcon(R.mipmap.ic_launcher)
                    .setMessage(Config.encryBean.de(Data.get(i).getContext()))
                    .setPositiveButton("确定" ,  null )
                    .show();
        }
    }

    private class BtSaveClick implements View.OnClickListener {
        public void onClick(View v) {
            try {
                FileUtil.writeData();
                Toast.makeText(v.getContext(), "恭喜，保存成功了", Toast.LENGTH_LONG).show();
            }
            catch (BaseExceptoin e){
                Toast.makeText(v.getContext(), "难过了，保存失败\n"+e.getErrorCode()+"-"+e.getErrorMsg(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
