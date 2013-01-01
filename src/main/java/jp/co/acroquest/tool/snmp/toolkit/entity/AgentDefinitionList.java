//AgentDefinitionList.java ----
// History: 2009/05/07 - Create
package jp.co.acroquest.tool.snmp.toolkit.entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * AgentDefinitionを保持するリスト。AgentDefinitionLoaderから生成される。
 *
 * @author akiba
 */
public class AgentDefinitionList
{
    /** AgentDefinitionを保持するマップ。 */
    private Map<String, AgentDefinition> agentDefinitionList_;

    /**
     * AgentDefinitionListを生成する。
     */
    public AgentDefinitionList()
    {
        this.agentDefinitionList_ = new HashMap<String, AgentDefinition>();
    }

    /**
     * AgentDefinitionを追加する。
     *
     * @param AgentDefinition 追加対象のAgentDefinition。
     */
    public void addAgentDefinition(AgentDefinition AgentDefinition)
    {
        if (AgentDefinition != null)
        {
            String address = AgentDefinition.getAddress();
            AgentDefinition preInfo = this.agentDefinitionList_.get(address);
            if (preInfo == null)
            {
                this.agentDefinitionList_.put(address, AgentDefinition);
            }
        }
    }

    /**
     * 指定されたIPアドレスに対応するAgentDefinitionを取得する。
     *
     * @param address 取得対象AgentDefinitionのIPアドレス。
     * @return 対応するAgentDefinition。存在しない場合はnullを返す。
     */
    public AgentDefinition getAgentDefinition(String address)
    {
        AgentDefinition retAgentDefinition = this.agentDefinitionList_.get(address);
        return retAgentDefinition;
    }

    /**
     * このAgentDefinitionListが保持する全てのAgentDefinitionを配列で取得する。
     *
     * @return AgentDefinitionの配列。
     */
    public AgentDefinition[] getAgentDefinitionArray()
    {
        Collection<AgentDefinition> infoSet = this.agentDefinitionList_.values();
        AgentDefinition[] infoArray = new AgentDefinition[infoSet.size()];
        infoArray = infoSet.toArray(infoArray);

        return infoArray;
    }
}
