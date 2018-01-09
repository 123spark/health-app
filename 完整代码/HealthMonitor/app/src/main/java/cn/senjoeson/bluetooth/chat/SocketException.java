package cn.senjoeson.bluetooth.chat;

import android.content.Context;

/**
 *
 * 描述：异常抛出处理
 */
public class SocketException extends Exception  {

    public SocketException(Context context, String detailMessage) {
        super(detailMessage);
//        Toast.makeText(context,detailMessage,Toast.LENGTH_SHORT).show();
    }

    public SocketException(Context context, String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
//        Toast.makeText(context,detailMessage,Toast.LENGTH_SHORT).show();
    }

    public SocketException(Context context, Throwable throwable) {
        super(throwable);
//        Toast.makeText(context,throwable.getMessage(),Toast.LENGTH_SHORT).show();
    }
}
