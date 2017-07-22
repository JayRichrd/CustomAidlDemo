package com.jy.customaidldemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String DESCRIPTOR = "com.jy.customaidldemo.MyService";
    private static final int TRANSACTION_add = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    // 显示控件
    private TextView tvResult;
    private IBinder mPlusBinder;
    // 服务连接
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            /**
             * 当与服务绑定时调用，即回调Service的onBind函数
             * service就是Service的onBind函数返回的IBinder对象
             */
            mPlusBinder = service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mPlusBinder = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvResult = (TextView) findViewById(R.id.tv_result);
        // 绑定服务service
        // 5.0以后必须采用显示Intent的方式绑定
        Intent intent = new Intent(getApplicationContext(), MyService.class);
        bindService(intent, mConnection, BIND_AUTO_CREATE);
    }

    // 点击响应
    public void add(View view) {
        if (mPlusBinder != null) {
            // 定义输入、回应数据
            android.os.Parcel _data = android.os.Parcel.obtain();
            android.os.Parcel _reply = android.os.Parcel.obtain();
            int _result;
            _data.writeInterfaceToken(DESCRIPTOR);
            // 依次写入数据
            _data.writeInt(50);
            _data.writeInt(12);
            try {
                // 触发服务端，将回调服务端的onTransact函数
                mPlusBinder.transact(TRANSACTION_add, _data, _reply, 0);
                _reply.readException();
                // 读取服务端返回的结果
                _result = _reply.readInt();
                tvResult.setText("计算结果是：" + _result);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

}
