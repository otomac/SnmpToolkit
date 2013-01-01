//Agent.java ----
// History: 2009/05/07 - Create
//          2009/05/21 - GETNEXT対応
//          2009/08/15 - AgentService対応
package jp.co.acroquest.tool.snmp.toolkit.entity;

import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 単一のSNMPAgentオブジェクト。
 *
 * @author akiba
 */
public class Agent
{
    /** Agentの種類を表すType名称。 */
    private String type_;

    /** Agentの対応するSNMPバージョン。v1, v2cが指定可能。 */
    private String version_;

    /** AgentのIPアドレス。 */
    private String address_;

    /** 読み込み専用コミュニティ名。 */
    private String roCommunity_;

    /** 書き込み対応コミュニティ名。 */
    private String rwCommunity_;

    /** Trapコミュニティ名。 */
    private String trapCommunity_;

    /** GET／SET Request受付ポート番号。 */
    private int snmpPort_;

    /** Trap送信ポート番号。 */
    private int trapPort_;

    /** SNMPのVariableBindingを格納するMap。 */
    private SortedMap<String, SnmpVarbind> objectMap_;

    /** OIDの比較を行うためのComparator。 */
    private OIDComparator comp_;

    /**
     * Agentを生成する。
     */
    public Agent()
    {
        this.comp_ = new OIDComparator();
        this.objectMap_ = new TreeMap<String, SnmpVarbind>(this.comp_);
    }

    /**
     * AgentにVarbindを追加する。
     *
     * @param varbind 追加するVarbind。
     */
    public void addVarbind(SnmpVarbind varbind)
    {
        if (varbind == null)
        {
            return;
        }

        String oid = varbind.getOid();
        if (oid == null)
        {
            return;
        }
        this.objectMap_.put(oid, varbind);
    }

    /**
     * 指定したOIDに対応するVarbindを取得する。
     *
     * @param oid 検索対象OID。
     * @param exact 指定したOIDに対して厳密に一致するVarbindを検索するかどうか。
     * @return ヒットしたVarbind。
     */
    public SnmpVarbind findObject(String oid, boolean exact)
    {
        if (oid == null)
        {
            return null;
        }
        Log log = LogFactory.getLog(Agent.class);

        SnmpVarbind object = null;
        if (exact == true)
        {
            // 完全一致指定の場合は、そのままのOIDでVarbindを検索する
            object = this.objectMap_.get(oid);
        }
        else
        {
            // 指定されたOID以降のサブマップから、条件に合致する最も若いVarbindを検索する
            SortedMap<String, SnmpVarbind> subMap = this.objectMap_.tailMap(oid);
            if (subMap.size() == 0)
            {
                // これ以上Varbindがない場合
                log.info("END of MIB.");
                return null;
            }

            Set<String> keys = subMap.keySet();
            String[] keyArray = keys.toArray(new String[keys.size()]);
            if (keyArray[0].equals(oid) == true)
            {
                // GETNEXTで完全に合致するOIDが指定された場合は、次の要素を検索する
                if (keyArray.length > 1)
                {
                    object = this.objectMap_.get(keyArray[1]);
                }
                else
                {
                    // これ以上Varbindがない場合
                    log.info("END of MIB.");
                    return null;
                }
            }
            else if (keyArray[0].contains(oid) == true)
            {
                // GETNEXTで完全に合致しないOIDが指定された場合は、
                // 次に持つ最も若いVarbindを検索する
                object = this.objectMap_.get(keyArray[0]);
            }
        }
        return object;
    }

    /**
     * @return the version
     */
    public String getVersion()
    {
        return this.version_;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version)
    {
        this.version_ = version;
    }

    /**
     * @return the address
     */
    public String getAddress()
    {
        return this.address_;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address)
    {
        this.address_ = address;
    }

    /**
     * @return the roCommunity
     */
    public String getRoCommunity()
    {
        return this.roCommunity_;
    }

    /**
     * @param roCommunity the roCommunity to set
     */
    public void setRoCommunity(String roCommunity)
    {
        this.roCommunity_ = roCommunity;
    }

    /**
     * @return the rwCommunity
     */
    public String getRwCommunity()
    {
        return this.rwCommunity_;
    }

    /**
     * @param rwCommunity the rwCommunity to set
     */
    public void setRwCommunity(String rwCommunity)
    {
        this.rwCommunity_ = rwCommunity;
    }

    /**
     * @return the trapCommunity
     */
    public String getTrapCommunity()
    {
        return this.trapCommunity_;
    }

    /**
     * @param trapCommunity the trapCommunity to set
     */
    public void setTrapCommunity(String trapCommunity)
    {
        this.trapCommunity_ = trapCommunity;
    }

    /**
     * @return the snmpPort
     */
    public int getSnmpPort()
    {
        return this.snmpPort_;
    }

    /**
     * @param snmpPort the snmpPort to set
     */
    public void setSnmpPort(int snmpPort)
    {
        this.snmpPort_ = snmpPort;
    }

    /**
     * @return the trapPort
     */
    public int getTrapPort()
    {
        return this.trapPort_;
    }

    /**
     * @param trapPort the trapPort to set
     */
    public void setTrapPort(int trapPort)
    {
        this.trapPort_ = trapPort;
    }

    /**
     * @return the type
     */
    public String getType()
    {
        return this.type_;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type)
    {
        this.type_ = type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        StringBuffer buf = new StringBuffer();

        int cnt = 0;
        for (SnmpVarbind obj : this.objectMap_.values())
        {
            if (cnt > 0)
            {
                buf.append(",\n");
            }
            buf.append("varbind:");
            buf.append(obj.toString());
            cnt++;
        }

        return buf.toString();
    }
}
