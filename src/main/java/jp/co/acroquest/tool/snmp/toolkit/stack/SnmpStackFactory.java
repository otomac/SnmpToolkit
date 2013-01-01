//SnmpStackFactory.java ----
// History: 2009/05/07 - Create
//          2009/08/15 - AgentService対応
package jp.co.acroquest.tool.snmp.toolkit.stack;

import jp.co.acroquest.tool.snmp.toolkit.AgentService;
import jp.co.acroquest.tool.snmp.toolkit.SnmpToolkitException;
import jp.co.acroquest.tool.snmp.toolkit.entity.Agent;
import jp.co.acroquest.tool.snmp.toolkit.request.RequestHandler;
import jp.co.acroquest.tool.snmp.toolkit.trap.TrapSender;

/**
 * SNMPスタックに共通する処理オブジェクト生成ファクトリ。
 * 
 * @author akiba
 */
public abstract class SnmpStackFactory
{
    /** このファクトリのインスタンス。 */
    private static SnmpStackFactory factory__;
    
    /** ファクトリクラス名を指定するプロパティ名。 */
    private static final String PROP_STACK_FACTORY = "snmptoolkit.stackFactory";
    
    /** デフォルトのファクトリクラス名。 */
    private static final String DEF_STACK_FACTORY  = "jp.co.acroquest.tool.snmp.toolkit.stack.Snmp4jFactory";
    
    /** デフォルトのTrapポート番号。 */
    protected static final int  DEFAULT_TRAP_PORT  = 162;
    
    /**
     * ファクトリを生成する。
     */
    protected SnmpStackFactory()
    {
    }
    
    /**
     * ファクトリオブジェクトを取得する。<br/>
     * ファクトリクラスは、プロパティ<code>snmptoolkit.stackFactory</code>で設定可能。
     * 
     * @return 生成したファクトリオブジェクト。
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    public static final SnmpStackFactory getFactory()
        throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        if (factory__ == null)
        {
            String factoryClassName = System.getProperty(PROP_STACK_FACTORY, DEF_STACK_FACTORY);
            Class<?> factoryClass = Class.forName(factoryClassName);
            factory__ = (SnmpStackFactory) factoryClass.newInstance();
        }
        
        return factory__;
    }
    
    /**
     * TrapSenderオブジェクトを生成する。
     * 
     * @param agent TrapSenderが処理するAgentのデータ。
     * @return TrapSenderオブジェクト。
     * @throws SnmpToolkitException
     */
    public abstract TrapSender createTrapSender(Agent agent)
        throws SnmpToolkitException;
    
    /**
     * RequestHandlerオブジェクトを生成する。
     * 
     * @param agent RequestHandlerが処理するAgentを持つサービス。
     * @return RequestHandlerオブジェクト。
     * @throws SnmpToolkitException
     */
    public abstract RequestHandler createRequestHandler(AgentService agentService)
        throws SnmpToolkitException;
}
