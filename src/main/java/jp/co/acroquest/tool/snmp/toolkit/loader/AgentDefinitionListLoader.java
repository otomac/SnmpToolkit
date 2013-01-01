//AgentDefinitionListLoader.java ----
// History: 2009/04/26 - Create
//          2009/08/15 - クラス名変更
package jp.co.acroquest.tool.snmp.toolkit.loader;

import java.io.File;
import java.io.IOException;

import jp.co.acroquest.tool.snmp.toolkit.entity.AgentDefinition;
import jp.co.acroquest.tool.snmp.toolkit.entity.AgentDefinitionList;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

/**
 * Agentリスト定義データXMLを読み込むローダークラス。
 *
 * @author akiba
 */
public class AgentDefinitionListLoader
{
    /** Agentリスト定義データを記述したXMLを読み込む為のDigester。 */
    private Digester digester_;

    /**
     * コンストラクタ。<br/>
     * Digesterを初期化し、読み込みルールを設定する。
     */
    public AgentDefinitionListLoader()
    {
        super();

        this.digester_ = new Digester();

        // 1. agent要素は、Agentクラスを生成する
        this.digester_.addObjectCreate("agents", AgentDefinitionList.class);

        //--------------------------------------------------------------------
        // <traps/trap-data/varbind>の追加指定
        //--------------------------------------------------------------------
        // 1. traps/trap-data/varbind 要素は、SnmpVarbindクラスを生成する
        this.digester_.addObjectCreate("agents/agent", AgentDefinition.class);
        // 2. 生成した AgentDefinition は、addAgentInfo()メソッドを呼び出してTrapDataに追加する
        this.digester_.addSetNext("agents/agent", "addAgentDefinition", AgentDefinition.class.getName());
        // 3. agents/agent 要素は、setOid()メソッドを呼び出す
        this.digester_.addCallMethod("agents/agent", "setAgentMIBFile", 0);
        // 4. agents/agent@address 要素は、setType()メソッドを呼び出す
        this.digester_.addSetProperties("agents/agent");

    }

    /**
     * 指定されたファイルを読み込み、AgentDefinitionListオブジェクトを生成する。<br/>
     * 生成されたAgentDefinitionListオブジェクトは、内部にAgentDefinitionオブジェクトを保持する。
     *
     * @param filename Agent定義ファイルパス。
     * @return 読み込みに成功した結果生成されたAgentDefinitionListオブジェクト。
     * @throws IOException 読み込みに失敗した場合。
     */
    public AgentDefinitionList load(String filename)
        throws IOException
    {
        AgentDefinitionList agentList = null;
        try
        {
            // Digesterを使用してAgentリスト定義データをロードする
            agentList = (AgentDefinitionList) this.digester_.parse(new File(filename));
        }
        catch (SAXException exception)
        {
            throw new IOException("SAXException occured.\n" +  exception.toString());
        }

        return agentList;
    }

    /**
     * テスト用のメインメソッド。
     *
     * @param args 読み込むデータファイル。
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
        AgentDefinitionListLoader loader = new AgentDefinitionListLoader();
        AgentDefinitionList agentList = loader.load(args[0]);
        for (AgentDefinition agent : agentList.getAgentDefinitionArray())
        {
            System.out.println(agent.toString());
        }
    }
}
