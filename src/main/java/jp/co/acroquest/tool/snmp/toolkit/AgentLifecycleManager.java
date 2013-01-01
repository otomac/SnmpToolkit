//AgentLifecycleManager.java ----
// History: 2009/05/05 - Create
//          2009/08/15 - AgentService対応
package jp.co.acroquest.tool.snmp.toolkit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jp.co.acroquest.tool.snmp.toolkit.entity.Agent;
import jp.co.acroquest.tool.snmp.toolkit.entity.AgentDefinition;
import jp.co.acroquest.tool.snmp.toolkit.entity.AgentDefinitionList;
import jp.co.acroquest.tool.snmp.toolkit.loader.AgentMIBCSVLoader;
import jp.co.acroquest.tool.snmp.toolkit.loader.AgentDefinitionListLoader;
import jp.co.acroquest.tool.snmp.toolkit.trap.TrapSender;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Agentの起動・終了のライフサイクルを管理するクラス。
 *
 * @author akiba
 */
public class AgentLifecycleManager
{
    /** Agent定義データ配置ディレクトリのデフォルト値。 */
    private static final String DEFAULT_DATA_DIR = "data";

    /** このオブジェクトの共通インスタンス。 */
    private static AgentLifecycleManager instance__;

    /** AgentInfoのリスト。 */
    private AgentDefinitionList agentDefList_;

    /** AgentServiceを保持するMapオブジェクト。 */
    private Map<String, AgentService> agentServiceMap_;

    /** Agent定義データ配置ディレクトリ。 */
    private String dataDir_;

    /**
     * AgentLifecycleManagerのインスタンスを取得する。
     *
     * @return AgentLifecycleManagerインスタンス。
     */
    public static AgentLifecycleManager getInstance()
    {
        if (instance__ == null)
        {
            instance__ = new AgentLifecycleManager();
        }

        return instance__;
    }

    /**
     * コンストラクタ。
     */
    private AgentLifecycleManager()
    {
        this.dataDir_ = DEFAULT_DATA_DIR;
    }

    /**
     * Agentリスト定義を読み込み、全てのAgentをロードする。
     *
     * @param defFile Agent定義ファイルのパス。
     * @throws IOException Agent定義ファイルの読み込みに失敗した場合。
     */
    public void loadAgent(String defFile) throws IOException
    {
        Log log = LogFactory.getLog(AgentLifecycleManager.class);
        log.info("Start loading agents. defFile=" + defFile);

        AgentDefinitionListLoader listLoader = new AgentDefinitionListLoader();
        this.agentDefList_ = listLoader.load(defFile);

        this.agentServiceMap_ = new HashMap<String, AgentService>();
        for (AgentDefinition agentDef : this.agentDefList_.getAgentDefinitionArray())
        {
            // Agent定義ファイルからAgentオブジェクトを生成する
            String agentAddress = agentDef.getAddress();
            String agentMibFile = agentDef.getAgentMIBFile();
            AgentMIBCSVLoader defLoader = new AgentMIBCSVLoader();
            Agent agent = defLoader.load(this.dataDir_, agentMibFile);

            // Agentオブジェクトに属性を設定する
            agent.setAddress(agentDef.getAddress());
            agent.setSnmpPort(agentDef.getSnmpPort());
            agent.setTrapPort(agentDef.getTrapPort());
            agent.setRoCommunity(agentDef.getRoCommunity());
            agent.setRwCommunity(agentDef.getRwCommunity());
            agent.setTrapCommunity(agentDef.getTrapCommunity());

            // Agentをマップに格納する
            AgentService service = new AgentService(agent);
            this.agentServiceMap_.put(agentAddress, service);
            if (log.isDebugEnabled())
            {
                log.debug("loaded agent: address=" + agentAddress);
            }
        }

        log.info("Finished loading agents.");
    }

    /**
     * MIB定義データの再読み込みを実施する。
     *
     * @throws SnmpToolkitException
     */
    public void reloadMIBData()
        throws SnmpToolkitException
    {
        Log log = LogFactory.getLog(AgentLifecycleManager.class);
        log.info("Start reloading all agents.");

        synchronized(this.agentServiceMap_)
        {
            Set<String> agentAddressSet = this.agentServiceMap_.keySet();
            for (String agentAddress : agentAddressSet)
            {
                reloadMIBData(agentAddress);
            }
        }

        log.info("Finished reloading all agents.");
    }

    /**
     * 特定IPアドレスのAgentについてMIB定義データの再読み込みを実施する。
     *
     * @param ipAddress
     * @throws IOException
     * @throws SnmpToolkitException
     */
    public void reloadMIBData(String ipAddress)
        throws SnmpToolkitException
    {
        Log log = LogFactory.getLog(AgentLifecycleManager.class);
        log.info("Start reloading agents. address=" + ipAddress);

        synchronized(this.agentServiceMap_)
        {
            try
            {
                AgentService service = this.agentServiceMap_.get(ipAddress);
                Agent oldAgent = service.getAgent();

                // Agent定義ファイルからAgentオブジェクトを生成する
                AgentDefinition agentInfo = this.agentDefList_.getAgentDefinition(ipAddress);
                String agentDefFile = agentInfo.getAgentMIBFile();
                AgentMIBCSVLoader defLoader = new AgentMIBCSVLoader();
                Agent agent = defLoader.load(this.dataDir_, agentDefFile);
                log.debug("new agent loaded. " + agent.toString());

                // Agentオブジェクトに属性を設定する
                agent.setAddress(oldAgent.getAddress());
                agent.setSnmpPort(oldAgent.getSnmpPort());
                agent.setTrapPort(oldAgent.getTrapPort());
                agent.setRoCommunity(oldAgent.getRoCommunity());
                agent.setRwCommunity(oldAgent.getRwCommunity());
                agent.setTrapCommunity(oldAgent.getTrapCommunity());

                // Agentを更新する
                service.reloadAgent(agent);
            }
            catch (IOException exception)
            {
                throw new SnmpToolkitException(exception);
            }
        }

        log.info("Finished reloading agents. address=" + ipAddress);
    }

    /**
     * 指定されたアドレスでAgentを起動する。
     *
     * @param address Agentを起動するアドレス。
     * @throws SnmpToolkitException Agentの起動に失敗した場合。
     */
    public void startAgent(String address) throws SnmpToolkitException
    {
        Log log = LogFactory.getLog(AgentLifecycleManager.class);

        AgentService service = this.agentServiceMap_.get(address);
        if (service == null)
        {
            log.warn("cannot find agent: address=" + address);
            return;
        }

        service.startService();
        log.info("started agent: address=" + address);
    }

    /**
     * 登録されている全てのAgentを起動する。
     *
     * @throws SnmpToolkitException Agentの起動に失敗した場合。
     */
    public void startAllAgents() throws SnmpToolkitException
    {
        if (this.agentServiceMap_ == null)
        {
            return;
        }

        Set<String> keys = this.agentServiceMap_.keySet();
        for (String address : keys)
        {
            startAgent(address);
        }
    }

    /**
     * 指定されたIPアドレスのAgentがもつTrapSenderを取得する。
     *
     * @param ipAddress 取得するAgentのIPアドレス。
     * @return TrapSenderオブジェクト。
     */
    public TrapSender getTrapSender(String ipAddress)
    {
        TrapSender sender = null;

        AgentService  service = this.agentServiceMap_.get(ipAddress);
        if (service != null)
        {
            sender = service.getTrapSender();
        }

        return sender;
    }

    /**
     * Agent定義ファイルの配置ディレクトリを設定する。
     *
     * @param dataDir Agent定義ファイルの配置ディレクトリ。
     */
    public void setDataDir(String dataDir)
    {
        this.dataDir_ = dataDir;
    }

    /**
     *
     */
    public void suspendAllServices()
    {
        // TODO Auto-generated method stub

    }

    /**
     *
     */
    public void resumeAllServices()
    {
        // TODO Auto-generated method stub

    }

    /**
     * @param ipAddress
     */
    public void suspendService(String ipAddress)
    {
        // TODO Auto-generated method stub

    }

    /**
     * @param ipAddress
     */
    public void resumeService(String ipAddress)
    {
        // TODO Auto-generated method stub

    }
}
