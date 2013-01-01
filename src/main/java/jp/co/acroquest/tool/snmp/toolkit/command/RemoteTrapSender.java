// RemoteTrapSender.java ----
// History: 2004/03/23 - Create
// 2009/07/25 - パッケージ移動(trap→command)
package jp.co.acroquest.tool.snmp.toolkit.command;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import jp.co.acroquest.tool.snmp.toolkit.SnmpToolkit;
import jp.co.acroquest.tool.snmp.toolkit.entity.TrapData;
import jp.co.acroquest.tool.snmp.toolkit.entity.Traps;
import jp.co.acroquest.tool.snmp.toolkit.loader.TrapDataLoader;

/**
 * RMIでSnmpToolkitに接続し、Trapを送信するコマンド。
 * 
 * @author akiba
 * @version 1.0
 */
public class RemoteTrapSender
{
    private static final String DEFAULT_SRV_NAME = "SnmpToolkit";

    /**
     * プログラムエントリ。
     * 
     * @param args コマンドライン引数。[0]=RMI接続URL。[1]=AgentのIPアドレス。[2]=Trapデータファイル。[3]=Trap送信間隔(オプション)。
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
        if (args.length < 3)
        {
            System.err.println("USAGE: RemoteTrapSender <rmi-url> <address> <datafile> [<interval>]");
            System.exit(1);
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
            System.err.println("Failed connecting to [" + boundObjName + "]. Couldn't find object.");
            System.exit(1);
        }
        System.out.println("Succeeded connecting to [" + boundObjName + "].");
        
        // Trap送信インターバルの取得
        long interval = 0L;
        if (args.length > 3)
        {
            try
            {
                interval = Long.parseLong(args[3], 10);
                if (interval < 0)
                {
                    System.err.println("ERROR: invalid interval: " + args[3] + " < 0");
                    System.exit(1);
                }
                System.out.println("Interval: " + interval + " msec.");
            }
            catch (NumberFormatException exception)
            {
                System.err.println("ERROR: invalid interval: " + args[3]);
                System.exit(1);
            }
        }

        try
        {
            // 送信するTrapデータの読み込み
            TrapDataLoader loader = new TrapDataLoader();
            Traps traps = loader.load(args[2]);
            TrapData[] trapArray = traps.getAllTrapData();
            for (int index = 0; index < trapArray.length; index++)
            {
                // 取得した分のTrapを送信する
                TrapData trapData = trapArray[index];
                toolkit.sendTrap(args[1], trapData);
                System.out.println("Trap[" + index + "] was sent.");
                
                // ウェイトを入れる(1ms以上の場合)
                if (interval > 0)
                {
                    Thread.sleep(interval);
                }
            }
        }
        catch (RemoteException exception)
        {
            System.err.println("Failed to send trap(s).);");
            System.err.println(exception.getLocalizedMessage());
            System.err.println();
            System.err.println("See snmptoolkit.log for more detail.");
        }
    }
}
