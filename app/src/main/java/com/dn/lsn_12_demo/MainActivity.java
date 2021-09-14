package com.dn.lsn_12_demo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dn.lsn_12_demo.R;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (checkSelfPermission(perms[0]) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(perms, 200);
            }
        }
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                File src = new File(Environment.getExternalStorageDirectory(), "7-Zip");
                File out = new File(Environment.getExternalStorageDirectory(), "7-Zip.7z");
                int result = ZipCode.exec("7zr a " + out.getAbsolutePath() + " "
                        + src.getAbsolutePath() + " -mx=9");
                Log.e(TAG, "ZipCode.exec: "+result);
                return result;
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                Toast.makeText(MainActivity.this,"ZipCode.exec result:"+integer,Toast.LENGTH_SHORT).show();
            }
        }.execute();

    }

    /**
     * 将assets下的可执行文件拷贝到应用私有目录
     *
     * @param view
     */
    public void load(View view) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                boolean result = ZipHelper.loadBinary(MainActivity.this, "7zr");
                return result;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                Toast.makeText(MainActivity.this, "加载7zr结果：" + aBoolean, Toast.LENGTH_SHORT).show();
            }
        }.execute();


    }

    /**
     * 压缩
     * 7zr a  [输出文件] [待压缩文件/目录] -mx=9
     * 7zr a /sdcard/7-Zip.7z /sdcard/7-Zip -mx=9
     *
     * @param view
     */
    public void pack(View view) {
        File src = new File(Environment.getExternalStorageDirectory(), "7-Zip");
        File out = new File(Environment.getExternalStorageDirectory(), "7-Zip.7z");
        ZipHelper.execute(this, "7zr a " + out.getAbsolutePath() + " "
                + src.getAbsolutePath() + " -mx=9", new ZipHelper.OnResultListener() {
            @Override
            public void onSuccess(String msg) {
                Log.e(TAG, "执行成功");
                Toast.makeText(MainActivity.this,"pack 执行成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int errorno, String msg) {
                Toast.makeText(MainActivity.this,String.format("pack 执行失败 错误码：%s,错误信息：%s",errorno,msg),Toast.LENGTH_SHORT).show();
                Log.e(TAG, "执行失败");
                Log.e(TAG, "错误码："+errorno);
                Log.e(TAG, "错误信息："+msg);
            }

            @Override
            public void onProgress(String msg) {
                Log.e(TAG, "正在执行:" + msg);
            }
        });
    }

    /**
     * 解压
     * 7zr x [压缩文件]  -o[输出目录]
     *
     * @param view
     */
    public void unpack(View view) {
        File src = new File(Environment.getExternalStorageDirectory(), "7-Zip.7z");
        File out = new File(Environment.getExternalStorageDirectory(), "7-Zip-unpack");
        ZipHelper.execute(this, "7zr x " + src.getAbsolutePath() + " -o"
                + out.getAbsolutePath(), new ZipHelper.OnResultListener() {
            @Override
            public void onSuccess(String msg) {
                Toast.makeText(MainActivity.this,"unpack 执行成功",Toast.LENGTH_SHORT).show();
                Log.e(TAG, "执行成功");
            }

            @Override
            public void onFailure(int errorno, String msg) {
                Toast.makeText(MainActivity.this,
                        String.format("unpack 执行失败 错误码：%s,错误信息：%s",errorno,msg)
                        ,Toast.LENGTH_SHORT)
                        .show();

                Log.e(TAG, "执行失败");
                Log.e(TAG, "错误码："+errorno);
                Log.e(TAG, "错误信息："+msg);
            }

            @Override
            public void onProgress(String msg) {
                Log.e(TAG, "正在执行:" + msg);
            }
        });
    }
}
