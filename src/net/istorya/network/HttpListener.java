package net.istorya.network;

public interface HttpListener {

    public void onHttpError(int error);

    public void onHttpFinish(String url, byte[] data, String tag);

    public void onHttpStart();
}
