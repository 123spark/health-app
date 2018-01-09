package com.example.administrator.healthmonitor;

/**
 * Created by Administrator on 2017/12/5/005.
 */

public class BufferArray {
    public double[] data=new double[600];
    public int id;
    public boolean isUsed=false;    //缓存区是否可用的标志
    public void ClearData(){        //清理缓存区的数据，并设置为可用状态
        this.data=new double[600];
        this.isUsed=false;
    }
    public void SetId(int i){       //初始化缓存区的id
        this.id=i;
    }
    public boolean isUsed(){        //判断该缓存数组是否被使用
        return isUsed;
    }
}
