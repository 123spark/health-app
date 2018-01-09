package cn.senjoeson.bluetooth.model;

/**

 *
 * 描述：eventbus-共用实体类
 */
public class EventBusEntity {

    private String msg;
    private String data;
    private String name;
    private String address;
    private boolean result;
    private int ostate;
    private int nstate;

    public int getOstate() {
        return ostate;
    }

    public void setOstate(int ostate) {
        this.ostate = ostate;
    }

    public int getNstate() {
        return nstate;
    }

    public void setNstate(int nstate) {
        this.nstate = nstate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
