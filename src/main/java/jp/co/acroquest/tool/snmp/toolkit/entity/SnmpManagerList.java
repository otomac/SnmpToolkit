//SnmpManagerList.java ----
// History: 2009/08/18 - Create
package jp.co.acroquest.tool.snmp.toolkit.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * SNMPマネージャのリストを保持するエンティティ。
 *
 * @author akiba
 */
public class SnmpManagerList
{
    /** SNMPマネージャのエンティティを保持するリスト。 */
    private List<SnmpManager> managerList_;

    /**
     * SNMPマネージャのリストを初期化する。
     */
    public SnmpManagerList()
    {
        this.managerList_ = new ArrayList<SnmpManager>();
    }

    /**
     * SNMPマネージャのエンティティを追加する。
     *
     * @param mgr SNMPマネージャのエンティティ。
     */
    public void addSnmpManager(SnmpManager mgr)
    {
        if (mgr == null)
        {
            return ;
        }

        this.managerList_.add(mgr);
    }

    /**
     * 全てのSNMPマネージャを配列で取得する。
     *
     * @return 全てのSNMPマネージャの配列。
     */
    public SnmpManager[] getSnmpManagers()
    {
        SnmpManager[] managers = new SnmpManager[this.managerList_.size()];
        managers = this.managerList_.toArray(managers);

        return managers;
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        buf.append("SnmpManagerList{");
        int index = 0;
        for (SnmpManager mgr : this.managerList_)
        {
            if (index > 0)
            {
                buf.append(',');
            }
            buf.append(mgr.toString());
            index++;
        }
        buf.append('}');
        return buf.toString();
    }
}
