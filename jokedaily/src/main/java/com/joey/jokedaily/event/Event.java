package com.joey.jokedaily.event;

/**
 * 事件
 */
public class Event {
    public static final int TO_REGISTER=1;
    public static final int TO_FIND_BACK=2;
    public static final int LOGIN_SUCCESS =3;
    public static final int FIND_SUCCESS = 4;
    public static final int REGISTER_SUSSES = 5;


    private int what;
    private int arg1;
    private int arg2;
    private Object obj;

    public Event(int what, int arg1, int arg2, Object obj) {
        this.what = what;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.obj = obj;
    }

    public Event(int what, int arg1, int arg2) {
        this(what,arg1,arg2,null);
    }

    public Event(int what, int arg1) {
        this(what,arg1,0);
    }

    public Event(int what) {
        this(what,0);
    }

    public int getWhat() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
    }

    public int getArg1() {
        return arg1;
    }

    public void setArg1(int arg1) {
        this.arg1 = arg1;
    }

    public int getArg2() {
        return arg2;
    }

    public void setArg2(int arg2) {
        this.arg2 = arg2;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    @Override
    public String toString() {
        String strObj=(obj==null)?"null":obj.toString();
        return "Event{" +
                "what=" + what +
                ", arg1=" + arg1 +
                ", arg2=" + arg2 +
                ", obj=" + strObj+
                '}';
    }
}
