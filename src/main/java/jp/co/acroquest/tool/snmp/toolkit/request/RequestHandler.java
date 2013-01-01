//RequestHandler.java ----
// History: 2005/02/07 - Create
//          2009/05/07 - initHandler()を追加
//          2009/08/15 - AgentService対応
package jp.co.acroquest.tool.snmp.toolkit.request;

import jp.co.acroquest.tool.snmp.toolkit.AgentService;
import jp.co.acroquest.tool.snmp.toolkit.SnmpToolkitException;

/**
 * SNMP-GETリクエストを受け付ける処理クラスのインタフェース。
 * 
 * @author akiba
 */
public interface RequestHandler
{
    /** デフォルトのRequest受信ポート番号。 */
    static final int    DEFAULT_SNMP_PORT      = 161;
    
    /** デフォルトの読み込み専用コミュニティ名。 */
    static final String DEFAULT_RO_COMMUNITY   = "public";
    
    /** デフォルトの書き込み可能コミュニティ名。 */
    static final String DEFAULT_RW_COMMUNITY   = "public";
    
    /** デフォルトのTrap送信コミュニティ名。 */
    static final String DEFAULT_TRAP_COMMUNITY = "public";
    
    /**
     * RequestHandlerに初期化パラメータを与える。
     * 
     * @param agentService RequestHandlerが処理するAgentデータのサービス。
     * @throws SnmpToolkitException RequestHandlerの初期化に失敗した場合。
     */
    void initHandler(AgentService agentService)
        throws SnmpToolkitException;
    
    /**
     * リスニングを開始する。
     * 
     * @throws SnmpToolkitException リスニングの開始に失敗した場合。
     */
    void startListening() throws SnmpToolkitException;
    
    /**
     * RequestHandlerを停止する。
     * 
     * @throws SnmpToolkitException リスニングの停止に異常が発生した場合。
     */
    void stopListening() throws SnmpToolkitException;

    ///**
    // * 新しいAgentオブジェクトを設定する。
    // * 
    // * @param agent 新しいAgentオブジェクト。
    // */
    //void setAgent(Agent agent);
    //
    ///**
    // * 現在設定されているAgentオブジェクトを取得する。
    // * 
    // * @return Agentオブジェクト。
    // */
    //Agent getAgent();
}
