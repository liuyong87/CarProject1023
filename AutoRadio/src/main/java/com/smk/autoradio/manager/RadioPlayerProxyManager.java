package com.smk.autoradio.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.smk.autoradio.aidl.IRadioPlayer;
import com.smk.autoradio.aidl.IRadioStatusChangeListener;
import com.smk.autoradio.constants.RadioConst;
import com.smk.autoradio.utils.Logutil;

import java.util.ArrayList;
import java.util.List;

public class RadioPlayerProxyManager extends IRadioPlayer.Stub {
    private static final String TAG = Logutil.makeTagLog(RadioPlayerProxyManager.class);
    private static RadioPlayerProxyManager _INSTANCE;
    private IRadioPlayer mService;
    private Context mCtx;
    private List<OnServieConnectStateListener> mCallbacks;

    public interface OnServieConnectStateListener {
        void onServiceConnected();

        void onServiceDisconnected();
    }

    private RadioPlayerProxyManager() {
    }

    public static RadioPlayerProxyManager getInstance() {
        if (null == _INSTANCE) {
            synchronized (RadioPlayerProxyManager.class) {
                if (null == _INSTANCE) {
                    _INSTANCE = new RadioPlayerProxyManager();
                }
            }
        }
        return _INSTANCE;
    }

    public RadioPlayerProxyManager init(Context ctx) {
        this.mCtx = ctx;
        return this;
    }


    /**
     * 注册服务连接状态变化监听
     *
     * @param l
     */
    public void registerOnServieConnectStateListener(OnServieConnectStateListener l) {
        if (null == mCallbacks) {
            this.mCallbacks = new ArrayList<OnServieConnectStateListener>();
        }
        if (!mCallbacks.contains(l)) {
            mCallbacks.add(l);
        }
        Logutil.i(TAG, "registerOnServieConnectStateListener() ..." + (null != mCallbacks ? mCallbacks.size() : 0));
    }

    /**
     * 反注册服务连接状态变化监听
     *
     * @param l
     */
    public void unregisterOnServieConnectStateListener(OnServieConnectStateListener l) {
        if (null == mCallbacks) {
            return;
        }
        if (mCallbacks.contains(l)) {
            Logutil.i(TAG, "unregisterOnServieConnectStateListener() ..." + (null != l ? l.getClass().getName() : "null"));
            mCallbacks.remove(l);
        }
    }

    private void notifyServiceConnected() {
        if (null != mCallbacks && mCallbacks.size() > 0) {
            for (OnServieConnectStateListener callback : mCallbacks) {
                callback.onServiceConnected();
            }
        }
    }

    private void notifyServiceDisconnected() {
        if (null != mCallbacks && mCallbacks.size() > 0) {
            for (OnServieConnectStateListener callback : mCallbacks) {
                callback.onServiceDisconnected();
            }
        }
    }

    /**
     * 检查服务连接状态<br>
     * 使用场景:<br>
     * 1.在Activity onCreate生命周期使用
     */
    public void reqCheckConnectStatus() {
        if (isConnected()) {
            notifyServiceConnected();
            return;
        }
        bindRadioService();
    }

    /**
     * 绑定服务
     */
    public void bindRadioService() {
        if (isConnected()) {
            Logutil.i(TAG, "RADIO SERVICE IS CONNECTED !!!");
            return;
        }
        if (null == mCtx) {
            new Throwable("Context is Null !!!");
            return;
        }
        mCtx.bindService(new Intent("com.semisky.service.RADIO_START"), mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Logutil.e(TAG, "onServiceConnected() ..." + (null != name ? name : "null"));
            mService = IRadioPlayer.Stub.asInterface(service);
            notifyServiceConnected();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Logutil.e(TAG, "onServiceDisconnected() ..." + (null != name ? name : "null"));
            mService = null;
            notifyServiceDisconnected();
        }
    };

    public boolean isConnected() {
        return (null != mService);
    }

    @Override
    public void registerOnRadioStatusChangeListener(IRadioStatusChangeListener l) {
        if (isConnected()) {
            try {
                this.mService.registerOnRadioStatusChangeListener(l);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void unregisterOnRadioStatusChangeListener(IRadioStatusChangeListener l) {
        if (isConnected()) {
            try {
                this.mService.unregisterOnRadioStatusChangeListener(l);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void reqRestoreChannelPlay() {
        if (isConnected()) {
            try {
                this.mService.reqRestoreChannelPlay();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getChannelType() {
        if (isConnected()) {
            try {
                return this.mService.getChannelType();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return RadioConst.CHANNEL_TYPE_FM;
    }

    @Override
    public int getChannel() {
        if (isConnected()) {
            try {
                return this.mService.getChannel();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return RadioConst.CHANNEL_INVALID;
    }

    @Override
    public void reqSwitchBand() {
        if (isConnected()) {
            try {
                this.mService.reqSwitchBand();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void reqPrevStrongChannel() {
        Logutil.i(TAG, "reqPrevStrongChannel() ... isConnected " + isConnected());
        if (isConnected()) {
            try {
                this.mService.reqPrevStrongChannel();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void reqNextStroneChannel() {
        Logutil.i(TAG, "reqNextStroneChannel() ... isConnected " + isConnected());
        if (isConnected()) {
            try {
                this.mService.reqNextStroneChannel();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void reqStepIncreaseChannel() {
        if (isConnected()) {
            try {
                this.mService.reqStepIncreaseChannel();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void reqStepDecreaseChannel() {
        if (isConnected()) {
            try {
                this.mService.reqStepDecreaseChannel();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void reqPlayChannel(int channel) {
        if (isConnected()) {
            try {
                this.mService.reqPlayChannel(channel);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void reqFullSearch() {
        if (isConnected()) {
            try {
                this.mService.reqFullSearch();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void reqSettingEQ() {
        if (isConnected()) {
            try {
                this.mService.reqSettingEQ();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void reqAddCurrentChannelToFavorite() {
        if (isConnected()) {
            try {
                this.mService.reqAddCurrentChannelToFavorite();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void reqGetFavoriteList() {
        if (isConnected()) {
            try {
                this.mService.reqGetFavoriteList();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void reqGetSearchList() {
        if (isConnected()) {
            try {
                this.mService.reqGetSearchList();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void reqSwitchDxOrLoc() {
        if (isConnected()) {
            try {
                this.mService.reqSwitchDxOrLoc();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getSoundtrackType() {
        if (isConnected()) {
            try {
                return this.mService.getSoundtrackType();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return RadioConst.SOUNDTRACK_TYPE_INVALID;
    }
}
