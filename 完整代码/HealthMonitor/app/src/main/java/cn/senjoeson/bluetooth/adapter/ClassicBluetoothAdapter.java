package cn.senjoeson.bluetooth.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 *
 *：扫描蓝牙适配器
 */
public class ClassicBluetoothAdapter extends BaseAdapter {
    private ArrayList<BluetoothDevice> mainList;
    Context context;
    SignListener sl;
    public ClassicBluetoothAdapter(Context context, ArrayList<BluetoothDevice> mainList, SignListener sl) {
        this.context = context;
        this.mainList = mainList;
        this.sl = sl;
    }

    @Override
    public int getCount() {
        return mainList.size();
    }

    @Override
    public Object getItem(int position) {
        return mainList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = convertView.inflate(context, com.example.administrator.healthmonitor.R.layout.item_classic, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(com.example.administrator.healthmonitor.R.id.title);
            holder.item_btn1 = (TextView) convertView.findViewById(com.example.administrator.healthmonitor.R.id.item_btn1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final BluetoothDevice device = mainList.get(position);
        holder.title.setText("设备名：\t\r   " + device.getName() + "\nMAC地址：" + device.getAddress() + getState(device.getBondState()));
        if(device.getBondState()==BluetoothDevice.BOND_NONE){
            holder.item_btn1.setText("建立配对");
        }else if(device.getBondState()==BluetoothDevice.BOND_BONDING){
            holder.item_btn1.setText("取消配对");
        }else if(device.getBondState()==BluetoothDevice.BOND_BONDED){
            holder.item_btn1.setText("解除配对");
        }else{
            holder.item_btn1.setText("");
            holder.item_btn1.setVisibility(View.GONE);
        }
        holder.item_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sl.blueOperation(device);
            }
        });
        return convertView;
    }

    private String getState(int state) {
        switch (state) {
            case BluetoothDevice.BOND_NONE:

                return "\n设备状态：未配对";
            case BluetoothDevice.BOND_BONDING:
                return "\n设备状态：配对中";
            case BluetoothDevice.BOND_BONDED:
                return "\n设备状态：已配对";
            default:
                return "\n设备状态：未知";
        }
    }

    class ViewHolder {
        TextView title;
        TextView item_btn1;
    }

    public interface SignListener{

        void blueOperation(BluetoothDevice device);

    }
}
