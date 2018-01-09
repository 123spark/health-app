package cn.senjoeson.bluetooth.receiver;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import cn.senjoeson.bluetooth.model.EventBusEntity;
import cn.senjoeson.bluetooth.utils.ClsUtils;

import de.greenrobot.event.EventBus;

/**


 * 描述：蓝牙配对广播
 */
public class BuletoothPairingReceiver extends BroadcastReceiver {
    String strPsw = "0000";
    final String ACTION_PAIRING_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_PAIRING_REQUEST)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                try {
                    abortBroadcast();//如果没有将广播终止，则会出现一个一闪而过的配对框。
                    //确认配对
                    ClsUtils.setPairingConfirmation(device.getClass(), device, true);
                    //调用setPin方法进行配对...
                    boolean ret = ClsUtils.setPin(device.getClass(), device, strPsw);
                    //终止有序广播
                    Log.i("order...", "isOrderedBroadcast:" + isOrderedBroadcast() + ",isInitialStickyBroadcast:" + isInitialStickyBroadcast());
                    EventBusEntity ebe = new EventBusEntity();
                    ebe.setMsg("blue_success");
                    ebe.setResult(ret);
                    ebe.setName(device.getName());
                    ebe.setAddress(device.getAddress());
                    EventBus.getDefault().post(ebe);
//                    Toast.makeText(context, "配对信息" + device.getName()+"=="+ret, Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    Toast.makeText(context, "请求连接错误...", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
