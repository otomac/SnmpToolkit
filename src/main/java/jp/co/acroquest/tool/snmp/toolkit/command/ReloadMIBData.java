//ReloadMIBData.java ----
// History: 2009/07/25 - Create
package jp.co.acroquest.tool.snmp.toolkit.command;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import jp.co.acroquest.tool.snmp.toolkit.SnmpToolkit;

/**
 * 特定または全てのAgentについてMIBデータの再読み込みを行うコマンド。
 * 
 * @author akiba
 */
public class ReloadMIBData
{
    private static final String DEFAULT_SRV_NAME = "SnmpToolkit";

    /**
     * プログラムエントリ。
     * 
     * @param args コマンドライン引数。[0]=RMI接続URL。[1]=AgentのIPアドレス。
     * @throws Exception
     */
    public static void main(String[] args)
        throws Exception
    {
        if (args.length < 1)
        {
            System.err.println("USAGE: RemoteTrapSender <rmi-url> [<address>]");
            System.exit(1);
        }
        
        String targetAddress = null;
        if (args.length > 1)
        {
            targetAddress = args[1];
        }
        
        String boundObjName = args[0] + "/" + DEFAULT_SRV_NAME;
        System.out.println("Connecting to " + boundObjName + " ...");
        SnmpToolkit toolkit = null;
        try
        {
            toolkit = (SnmpToolkit) Naming.lookup(boundObjName);
        }
        catch (NotBoundException exception)
        {
            System.out.println("Failed connecting to [" + boundObjName + "]. Couldn't find object.");
            System.exit(1);
        }
        System.out.println("Succeeded connecting to [" + boundObjName + "].");
        
        try
        {
            if (targetAddress == null)
            {
                System.out.println("Reloading MIB data for all agents.");
                toolkit.reloadMIBData();
            }
            else
            {
                System.out.println("Reloading MIB data for agent[" + targetAddress + "].");
                toolkit.reloadMIBData(targetAddress);
            }
            System.out.println("complete.");
        }
        catch (RemoteException exception)
        {
            System.err.println("Failed to reload MIB data.");
            System.err.println(exception.getLocalizedMessage());
            System.err.println();
            System.err.println("See snmptoolkit.log for more detail.");
        }
    }
}
