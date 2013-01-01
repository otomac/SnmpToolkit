//TrapSenderFactory.java ----
// History: 2005/02/07 - Create
//          2009/05/07 - SnmpStackFactoryを導入
//          2009/08/15 - AgentService対応
package jp.co.acroquest.tool.snmp.toolkit.trap;

import jp.co.acroquest.tool.snmp.toolkit.SnmpToolkitException;
import jp.co.acroquest.tool.snmp.toolkit.entity.Agent;
import jp.co.acroquest.tool.snmp.toolkit.stack.SnmpStackFactory;

/**
 * TrapSenderインスタンスを初期化する為のファクトリクラス。<br>
 * 提供するメソッドはstaticであり、このクラス自体のインスタンス生成は不要である。
 *
 * @author akiba
 * @version 1.0
 */
public class TrapSenderFactory
{
    /**
     * TrapSenderインスタンスを初期化する。
     *
     * @param agent TrapSenderが処理するAgentのデータ。
     * @return 生成したTrapSenderインスタンス。
     * @throws SnmpToolkitException
     */
    public static TrapSender createTrapSender(Agent agent)
        throws SnmpToolkitException
    {
        TrapSender sender;
        try
        {
            SnmpStackFactory factory = SnmpStackFactory.getFactory();
            sender = factory.createTrapSender(agent);
        }
        catch (Exception exception)
        {
            throw new SnmpToolkitException(exception);
        }

        return sender;
    }

    /**
     * インスタンス化は不要の為、コンストラクタをprivate化する。
     */
    private TrapSenderFactory()
    {
    }
}
