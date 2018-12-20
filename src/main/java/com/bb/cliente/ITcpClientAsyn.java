package com.bb.cliente;


import org.apache.mina.core.session.IoSession;


public interface ITcpClientAsyn {
    public boolean conectar();
    public String writeToSocket(String mensaje, int readTimeout);
    public void desconectar();
   // public void conectarJMS() throws Exception;
    public boolean isConectado();
    public IoSession getSession();
}
