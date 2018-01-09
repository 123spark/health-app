package com.example.administrator.healthmonitor;

import android.bluetooth.BluetoothDevice;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.senjoeson.bluetooth.adapter.BluetoothChatAdapter;
import cn.senjoeson.bluetooth.base.BaseActivity;
import cn.senjoeson.bluetooth.chat.BluetoothChatListener;
import cn.senjoeson.bluetooth.chat.ChatConnectControl;
import cn.senjoeson.bluetooth.chat.ChatState;
import cn.senjoeson.bluetooth.model.ChatItemBean;
import cn.senjoeson.bluetooth.utils.ClsUtils;


public class BlueToothChatActivity extends BaseActivity implements View.OnClickListener, BluetoothChatListener {
    private final static String TAG = BlueToothChatActivity.class.getSimpleName();
    ChatConnectControl bt;
    private ListView mListView;
    String mEditTextContent = "";
    private BluetoothChatAdapter mAdapter;
    private List<ChatItemBean> mdata = new ArrayList<ChatItemBean>();
    ;
    private ChatItemBean chatOut;
    private ChatItemBean chatIn;
    private BluetoothDevice device;
    private TextView title;
    private TextView right_tv;
    private RelativeLayout right;
    private RelativeLayout left;
    private TextView left_tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_chat);
        initView();
        initData();
        initListener();
    }

    @Override
    public void onDeviceConnected(String name, String address) {
        Log.e(TAG, name + "-----" + address);
    }

    @Override
    public void onDeviceDisconnected() {
        Log.e("initListener", "onDeviceDisconnected");
    }

    @Override
    public void onDeviceConnectionFailed() {
        Log.e(TAG, "onDeviceConnectionFailed");
        Toast.makeText(BlueToothChatActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onServiceStateChanged(String state) {
        Log.e(TAG, state);
        if (state.equals("1->2")) {
            Toast.makeText(BlueToothChatActivity.this, "正在连接", Toast.LENGTH_SHORT).show();
        }
        if (state.equals("2->3")) {
            Toast.makeText(BlueToothChatActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
        }
        if (state.equals("1->3")) {
            Toast.makeText(BlueToothChatActivity.this, "对方与你连接", Toast.LENGTH_SHORT).show();
        }
        if (state.equals("3->1")) {
            Toast.makeText(BlueToothChatActivity.this, "对方断开连接", Toast.LENGTH_SHORT).show();
        }
        if (state.equals("3->0")) {
            Toast.makeText(BlueToothChatActivity.this, "主动断开连接", Toast.LENGTH_SHORT).show();
        }
        if (state.equals("1->0")) {
            Toast.makeText(BlueToothChatActivity.this, "退出连接", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 接受对方消息
     *
     * @param data
     * @param message
     */
    @Override
    public void onDataReceived(byte[] data, String message) {

        if (!message.equals("")) {
            chatIn = new ChatItemBean();
            chatIn.setType(0);
            chatIn.setIcon(BitmapFactory.decodeResource(getResources(),
                    R.mipmap.cc_img));
            chatIn.setText(message);
            mdata.add(chatIn);
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(mdata.size() - 1);
        }

    }

    /**
     * 初始化监听事件
     */
    private void initListener() {
        left.setOnClickListener(this);
        right.setOnClickListener(this);
        bt.setBluetoothChatListener(this);

    }

    /**
     * 初始化数据
     */
    private void initData() {
        device = getIntent().getParcelableExtra("device");
        bt = new ChatConnectControl(this, adapter);
        title.setText("服务端");//device.getName()
        right_tv.setText("手动连接");
        left_tv.setText("返回");
        mAdapter = new BluetoothChatAdapter(this, mdata);
        mListView.setAdapter(mAdapter);
        mListView.setSelection(mdata.size() - 1);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        title = (TextView) findViewById(R.id.base_title);
        right = (RelativeLayout) findViewById(R.id.base_right_rl);
        right_tv = (TextView) findViewById(R.id.base_right_tv);
        left = (RelativeLayout) findViewById(R.id.base_left_rl);
        left_tv = (TextView) findViewById(R.id.base_left_tv);
        mListView = (ListView) findViewById(R.id.listView_chat);

    }

    /**
     * 连接通信
     */
    private void connect() {
        if (BluetoothDevice.BOND_BONDED == device.getBondState()) {//已配对，未对接上，重新连接
            bt.connect(device.getAddress());
        } else {
            try {
                ClsUtils.createBond(device.getClass(), device);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "与" + title.getText().toString().trim() + "设备连接失败", Toast.LENGTH_SHORT).show();
            }
        }

    }



    @Override
    protected void onStart() {
        super.onStart();
        if (!bt.isServiceAvailable()) {
            bt.setupService();
            bt.startService();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bt.stopService();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.base_left_rl:
                finish();
                break;
            case R.id.base_right_rl:
                if (!openBluetooth()) {
                    return;
                }
                if (bt.getServiceState() == ChatState.STATE_CONNECTED) {
                    Toast.makeText(this, "已连接上", Toast.LENGTH_SHORT).show();
                    return;
                }
                connect();
                break;
        }
    }


}
