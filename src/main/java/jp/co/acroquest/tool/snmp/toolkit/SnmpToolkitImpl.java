// SnmpToolkitImpl.java ----
// History: 2004/03/23 - Create
// 2009/08/15 - AgentService対応
package jp.co.acroquest.tool.snmp.toolkit;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import jp.co.acroquest.tool.snmp.toolkit.entity.SnmpConfigItem;
import jp.co.acroquest.tool.snmp.toolkit.entity.SnmpManager;
import jp.co.acroquest.tool.snmp.toolkit.entity.SnmpManagerList;
import jp.co.acroquest.tool.snmp.toolkit.entity.TrapData;
import jp.co.acroquest.tool.snmp.toolkit.loader.SnmpConfiguration;
import jp.co.acroquest.tool.snmp.toolkit.trap.TrapSender;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * SnmpToolkitのデフォルト実装。
 *
 * @author akiba
 * @version 1.0
 */
public class SnmpToolkitImpl extends UnicastRemoteObject implements SnmpToolkit
{
    private static final long     serialVersionUID    = 4675606205704697125L;

    /** バージョン表示を行うオプション指定文字列。 */
    private static final String   OPT_SHOW_VERSION    = "-v";

    /** 設定ファイルへのパスを指定するシステムプロパティのキー。 */
    private static final String   CONFIG_PATH         = "snmptoolkit.configPath";

    /** 設定ファイルへのデフォルトパス。 */
    private static final String   DEFAULT_CONFIG_PATH = "../conf/config.xml";

    /** Agentのライフサイクル管理を行うオブジェクト。 */
    private AgentLifecycleManager lcMgr_              = null;

    /** このプロセスが起動した時間。 */
    private static long           upTime__            = System.currentTimeMillis();

    /**
     * このプロセスが起動してから経過した時間を取得する。
     *
     * @return このプロセスが起動してから経過した時間(ミリ秒)。
     */
    public static long getSysUpTime()
    {
        long sysUpTime = System.currentTimeMillis() - upTime__;
        return sysUpTime;
    }

    /**
     * SnmpToolkitImplを初期化する。
     *
     * @throws RemoteException 初期化に失敗した場合。
     */
    public SnmpToolkitImpl(String agentDefFile) throws RemoteException
    {
        super();

        Log log = LogFactory.getLog(SnmpToolkitImpl.class);
        log.info("SnmpToolkit.<init>");

        try
        {
            this.lcMgr_ = AgentLifecycleManager.getInstance();
            SnmpConfiguration config = SnmpConfiguration.getInstance();
            SnmpConfigItem configItem = config.getSnmpConfigItem();
            String parentPath = configItem.getDataDir();
            this.lcMgr_.setDataDir(parentPath);
            try
            {
                this.lcMgr_.loadAgent(agentDefFile);
            }
            catch (IOException exception)
            {
                throw new SnmpToolkitException(exception);
            }
            this.lcMgr_.startAllAgents();
        }
        catch (SnmpToolkitException exception)
        {
            throw new RemoteException("An exception occured in initialize.", exception);
        }
    }

    /**
     * Trapを送信する。
     *
     * @param address Agentを指定するIPアドレス。
     * @param trapData 送信するTrapのデータ。
     * @throws RemoteException RMI呼び出しで発生した例外。
     */
    public void sendTrap(String address, TrapData trapData)
        throws RemoteException
    {
        try
        {
            TrapSender sender = this.lcMgr_.getTrapSender(address);
            if (sender != null)
            {
                sender.sendTrap(trapData);
            }
        }
        catch (SnmpToolkitException exception)
        {
            Log log = LogFactory.getLog(SnmpToolkitImpl.class);
            log.error("Failed to send trap(s).", exception);
            throw new RemoteException("An exception occured.", exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void reloadMIBData() throws RemoteException
    {
        try
        {
            this.lcMgr_.suspendAllServices();
            this.lcMgr_.reloadMIBData();
            this.lcMgr_.resumeAllServices();
        }
        catch (SnmpToolkitException exception)
        {
            Log log = LogFactory.getLog(SnmpToolkitImpl.class);
            log.error("Failed to reload MIB data.", exception);
            throw new RemoteException("Failed to reload MIB data.", exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void reloadMIBData(String ipAddress) throws RemoteException
    {
        try
        {
            this.lcMgr_.suspendService(ipAddress);
            this.lcMgr_.reloadMIBData(ipAddress);
            this.lcMgr_.resumeService(ipAddress);
        }
        catch (SnmpToolkitException exception)
        {
            Log log = LogFactory.getLog(SnmpToolkitImpl.class);
            log.error("Failed to reload MIB data.", exception);
            throw new RemoteException("Failed to reload MIB data.", exception);
        }
    }

    /**
     * プログラムエントリ。
     *
     * @param args コマンドライン引数。RMIポート番号を指定する。
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
        if (args.length < 1)
        {
            System.err.println("USAGE: SnmpToolkitImpl <data-file>");
            System.exit(1);
        }

        if (OPT_SHOW_VERSION.equals(args[0]) == true)
        {
            System.err.println("SNMPToolkit version " + Version.VERSION);
            System.exit(0);
        }

        // Agent定義データファイル
        String dataFile = args[0];

        Log log = LogFactory.getLog(SnmpToolkitImpl.class);
        String logoStr = "-- SNMP Toolkit version " + Version.VERSION + " --";
        String lineStr = getLineStr(logoStr);
        log.info(lineStr);
        log.info(logoStr);
        log.info(lineStr);

        try
        {
            // システムプロパティから設定ファイルのパスを取得する
            String configPath = System.getProperty(CONFIG_PATH, DEFAULT_CONFIG_PATH);
            log.debug("Configuration file path: " + configPath);

            // SnmpToolkitの初期化
            SnmpConfiguration.initialize(configPath);
            // コンフィグの内容をチェックする
            checkConfig();
            SnmpConfiguration config = SnmpConfiguration.getInstance();
            SnmpConfigItem configItem = config.getSnmpConfigItem();
            log.debug("SnmpConfiguration: " + configItem.toString());

            SnmpToolkit toolkit = new SnmpToolkitImpl(dataFile);
            log.info("SnmpToolkitImpl is created.");

            // レジストリの初期化
            int rmiPort = configItem.getRemotePort();
            Registry registry = LocateRegistry.createRegistry(rmiPort);
            log.debug("Created local registry in port " + rmiPort);

            // SnmpToolkitをレジストリにバインドする
            InetAddress local = InetAddress.getLocalHost();
            String localAddr = local.getHostName();
            registry.bind("SnmpToolkit", toolkit);

            // ログ＆コンソール出力
            String msg = "SnmpToolkit was started at [rmi://" + localAddr + ":" + rmiPort + "].";
            log.info(msg);
            System.out.println(msg);
        }
        catch (Exception exception)
        {
            log.error("Exception occured.", exception);
            System.err.println("ERROR: Failed to start SnmpToolkit. See log file for detail.");
            System.exit(1);
        }
    }

    /**
     * ロゴ表示用の区切り線文字列を生成する。
     *
     * @param logoStr ロゴの文字列。
     * @return 区切り線文字列。
     */
    private static String getLineStr(String logoStr)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < logoStr.length(); i++)
        {
            sb.append('-');
        }
        return sb.toString();
    }

    /**
     * コンフィグファイルの内容をチェックする。
     *
     * @throws SnmpToolkitException チェックでエラーが発生した場合。
     */
    private static void checkConfig()
        throws SnmpToolkitException
    {
        SnmpConfiguration config = SnmpConfiguration.getInstance();
        SnmpConfigItem configItem = config.getSnmpConfigItem();

        // SNMPマネージャのリストを走査し、アドレスが正しく認識されていないものがあればエラーとする
        SnmpManagerList mgrList = configItem.getSnmpManagerList();
        SnmpManager[] managers = mgrList.getSnmpManagers();
        for (SnmpManager manager : managers)
        {
            String mgrAddress = manager.getManagerAddress();
            if (mgrAddress == null)
            {
                throw new SnmpToolkitException("Manager address is null.");
            }
        }
    }
}
