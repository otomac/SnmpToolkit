//AgentService.java ----
// History: 2009/08/15 - Create
package jp.co.acroquest.tool.snmp.toolkit;

import jp.co.acroquest.tool.snmp.toolkit.entity.Agent;
import jp.co.acroquest.tool.snmp.toolkit.request.RequestHandler;
import jp.co.acroquest.tool.snmp.toolkit.stack.SnmpStackFactory;
import jp.co.acroquest.tool.snmp.toolkit.trap.TrapSender;

/**
 * 単一のAgentの起動・終了を管理する。
 *
 * @author akiba
 */
public class AgentService
{
    /** このAgentServiceが管理するAgentオブジェクト。 */
    private Agent agent_;

    /** このAgentServiceが扱うRequestHandler。 */
    private RequestHandler reqHandler_;

    /** このAgentServiceが利用するTrapSender。 */
    private TrapSender trapSender_;

    /**
     * AgentServiceを初期化する。
     *
     * @param agent このAgentServiceが扱うAgentオブジェクト。
     */
    public AgentService(Agent agent)
    {
        this.agent_ = agent;
    }

    /**
     * AgentServiceを開始する。
     *
     * @throws SnmpToolkitException AgentServiceの開始に失敗した場合。
     */
    public void startService() throws SnmpToolkitException
    {
        try
        {
            SnmpStackFactory factory = SnmpStackFactory.getFactory();
            this.reqHandler_ = factory.createRequestHandler(this);
            this.trapSender_ = factory.createTrapSender(this.agent_);

            this.reqHandler_.startListening();
        }
        catch (ClassNotFoundException exception)
        {
            throw new SnmpToolkitException(exception);
        }
        catch (InstantiationException exception)
        {
            throw new SnmpToolkitException(exception);
        }
        catch (IllegalAccessException exception)
        {
            throw new SnmpToolkitException(exception);
        }
    }

    /**
     * AgentServiceを停止する。
     *
     * @throws SnmpToolkitException AgentServiceの停止に失敗した場合。
     */
    public void stopService() throws SnmpToolkitException
    {
        this.reqHandler_.stopListening();
    }

    /**
     * AgentServiceの動作を一時的に停止する。
     *
     * @throws SnmpToolkitException AgentServiceの一時停止に失敗した場合。
     */
    public void suspendService() throws SnmpToolkitException
    {
        //this.reqHandler_.suspend();
    }

    /**
     * 一時停止中のAgentServiceを再開する。
     *
     * @throws SnmpToolkitException AgentServiceの再開に失敗した場合。
     */
    public void resumeService() throws SnmpToolkitException
    {
        //this.reqHandler_.resume();
    }

    /**
     * 新しいAgentオブジェクトを設定し、MIBデータを更新する。
     *
     * @param agent 新しいAgentオブジェクト。
     */
    public void reloadAgent(Agent agent)
    {
        this.agent_ = agent;
        //this.reqHandler_.setAgent(agent);
    }

    /**
     * このAgentServiceが扱うAgentオブジェクトを取得する。
     *
     * @return Agentオブジェクト。
     */
    public Agent getAgent()
    {
        return this.agent_;
    }

    /**
     * このAgentServiceが使用するTrapSenderを取得する。
     *
     * @return TrapSenderオブジェクト。
     */
    public TrapSender getTrapSender()
    {
        return this.trapSender_;
    }
}
