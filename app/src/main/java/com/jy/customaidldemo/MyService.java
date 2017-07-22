package com.jy.customaidldemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

public class MyService extends Service {
    private MyBinder mBinder = new MyBinder();

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // 返回服务端的Binder
        return mBinder;
    }

    /**
     * 自定义Binder类
     */
    private static class MyBinder extends Binder {
        static final String DESCRIPTOR = "com.jy.customaidldemo.MyService";
        static final int TRANSACTION_add = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);

        /**
         * 覆写了Binder类的onTransact函数
         * 当在客户端调用transact函数后，会回调这个onTransact函数
         * 在这个函数中实现我们自己的逻辑
         *
         * @param code  是一个整形的唯一标识，用于区分执行哪个方法，客户端会传递此参数，告诉服务端执行哪个方法
         * @param data  客户端传递过来的参数
         * @param reply 服务端将返回回去的值
         * @param flags 标明是否有返回值，0为有（双向），1为没有（单向）
         * @return 返回是否被成功回调
         * @throws RemoteException
         */
        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case TRANSACTION_add: { // 下面定义我们自己的逻辑
                    data.enforceInterface(DESCRIPTOR);

                    int _arg0;
                    int _arg1;
                    // 依次读取数据
                    // 与客户端依次写入数据相对应
                    _arg0 = data.readInt();
                    _arg1 = data.readInt();
                    // 实现两个数相加
                    int _result = _arg0 + _arg1;
                    reply.writeNoException();
                    // 向客户端写回结果
                    reply.writeInt(_result);
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }
    }
}
