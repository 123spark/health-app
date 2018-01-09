package cn.senjoeson.bluetooth.chat;

/**

 *
 * 描述：蓝牙会话监听
 */
public interface BluetoothChatListener {
    /**
     * 设备连接中
     *
     * @param name
     * @param address
     */
    void onDeviceConnected(String name, String address);

    /**
     * 设备断开连接
     */
    void onDeviceDisconnected();

    /**
     * 设备连接失败
     */
    void onDeviceConnectionFailed();

    /**
     * 聊天状态
     *
     * @param state
     */
    void onServiceStateChanged(String state);

    /**
     * 接收消息
     *
     * @param data
     * @param message
     */
    void onDataReceived(byte[] data, String message);
}
