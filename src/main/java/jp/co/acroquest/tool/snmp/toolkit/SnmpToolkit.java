// SnmpToolkit.java ----
// History: 2004/03/23 - Create
// 2009/07/25 - MIBデータ再読み込みインタフェースを追加
package jp.co.acroquest.tool.snmp.toolkit;

import java.rmi.Remote;
import java.rmi.RemoteException;

import jp.co.acroquest.tool.snmp.toolkit.entity.TrapData;

/**
 * SNMP Toolkitのリモートインタフェース。
 * 
 * @author akiba
 * @version 1.0
 */
public interface SnmpToolkit extends Remote
{
    /**
     * Trapを送信する。
     * 
     * @param address Agentを指定するIPアドレス。
     * @param trapData 送信するTrapのデータ。
     * @throws RemoteException RMI呼び出しで発生した例外。
     */
    public void sendTrap(String address, TrapData trapData)
        throws RemoteException;

    /**
     * MIBデータの再読み込みを実行する。
     * 
     * @throws RemoteException RMI呼び出しで発生した例外。
     */
    public void reloadMIBData() throws RemoteException;

    /**
     * 特定IPアドレスのAgentについてMIBデータの再読み込みを実行する。
     * 
     * @param address Agentを指定するIPアドレス。
     * @throws RemoteException RMI呼び出しで発生した例外。
     */
    public void reloadMIBData(String address) throws RemoteException;
}
