// TrapData.java ----
// History: 2004/03/07 - Create
package jp.co.acroquest.tool.snmp.toolkit.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Trap情報。
 *
 * @author akiba
 * @version 1.0
 */
public class TrapData implements Serializable
{
    private static final long serialVersionUID = -2767281869930767183L;

    /** Trap固有OID。 */
    private String            trapOid_;

    /** Trap-Enterprise OID。 */
    private String            enterprise_;

    /** Trap内容のVarbindを格納するリスト。 */
    private List<SnmpVarbind> varbindList_;

    /** RequestIDが設定されているか。 */
    private boolean           hasReqId_;

    /** RequestID。 */
    private int               reqId_           = -1;

    /** SNMPTrap Version。 */
    private String            version_;

    /** V1TrapのGeneric値。 */
    private int               generic_         = -1;

    /** V1TrapのSpecific値。 */
    private int               specific_        = -1;

    /**
     * コンストラクタ。
     */
    public TrapData()
    {
        super();

        this.varbindList_ = new ArrayList<SnmpVarbind>();
    }

    /**
     * SNMPTrap versionを取得する。
     *
     * @return the version
     */
    public String getVersion()
    {
        return this.version_;
    }

    /**
     * SNMPTrap versionを設定する。
     *
     * @param version the version to set
     */
    public void setVersion(String version)
    {
        this.version_ = version;
    }

    /**
     * RequetIDの指定があるか。
     *
     * @return RequetIDが設定されていればtrue。
     */
    public boolean hasReqId()
    {
        return this.hasReqId_;
    }

    /**
     * RequestIDを取得する。
     *
     * @return RequestID。
     */
    public int getReqId()
    {
        return this.reqId_;
    }

    /**
     * RequestIDを設定する。
     *
     * @param reqId RequestID。
     * @throws IllegalArgumentException RequestIDが0未満の場合。
     */
    public void setReqId(int reqId)
    {
        // 範囲外のRequetIDは例外をスローする
        if (reqId < 0)
        {
            throw new IllegalArgumentException("RequetID is out of range: " + reqId);
        }

        this.reqId_ = reqId;
        // RequestIDが設定されていることをマークする
        this.hasReqId_ = true;
    }

    /**
     * Trap-OIDを設定する。
     *
     * @param oid Trap-OID。
     */
    public void setTrapOid(String oid)
    {
        this.trapOid_ = oid;
    }

    /**
     * Trap-OIDを取得する。
     *
     * @return Trap-OID。
     */
    public String getTrapOid()
    {
        return this.trapOid_;
    }

    /**
     * V1TrapのGeneric値を取得する。
     *
     * @return the generic
     */
    public int getGeneric()
    {
        return this.generic_;
    }

    /**
     * V1TrapのGeneric値を設定する。
     *
     * @param generic the generic to set
     */
    public void setGeneric(int generic)
    {
        this.generic_ = generic;
    }

    /**
     * V1TrapのSpecific値を取得する。
     *
     * @return the specific
     */
    public int getSpecific()
    {
        return this.specific_;
    }

    /**
     * V1TrapのSpecific値を設定する。
     *
     * @param specific the specific to set
     */
    public void setSpecific(int specific)
    {
        this.specific_ = specific;
    }

    /**
     * Trap-Enterpriseを設定する。
     *
     * @param enterprise Trap-Enterprise。
     */
    public void setEnterprise(String enterprise)
    {
        this.enterprise_ = enterprise;
    }

    /**
     * Trap-Enterpriseを取得する。
     *
     * @return Trap-Enterprise。
     */
    public String getEnterprise()
    {
        return this.enterprise_;
    }

    /**
     * Varbindを追加する。
     *
     * @param varbind Varbind。
     */
    public void addVarbind(SnmpVarbind varbind)
    {
        this.varbindList_.add(varbind);
    }

    /**
     * 追加されているVarbindを配列として取得する。
     *
     * @return Varbindの配列。
     */
    public SnmpVarbind[] getVarbinds()
    {
        SnmpVarbind[] varbinds = new SnmpVarbind[this.varbindList_.size()];
        varbinds = (SnmpVarbind[]) this.varbindList_.toArray(varbinds);

        return varbinds;
    }

    /**
     * デバッグ用に、このTrapデータの内容を文字列化する。
     *
     * @return TrapDataオブジェクトの文字列表現。
     */
    public String toString()
    {
        StringBuffer buf = new StringBuffer(512);

        buf.append(this.getClass().getName());
        buf.append("{");
        if ("v1".equals(this.version_) == true)
        {
            buf.append("version=");
            buf.append(this.version_);
            buf.append(",generic=");
            buf.append(this.generic_);
            buf.append(",specific=");
            buf.append(this.specific_);
            buf.append(",enterprise=");
            buf.append(this.enterprise_);
        }
        else if ("v2c".equals(this.version_) == true)
        {
            buf.append("version=");
            buf.append(this.version_);
            buf.append(", trap-oid=");
            buf.append(this.trapOid_);
            buf.append(",enterprise=");
            buf.append(this.enterprise_);
        }
        else
        {
            buf.append("**invalid snmp version : ");
            buf.append(this.version_);
            buf.append('}');
            return buf.toString();
        }

        buf.append(",varbind{");
        SnmpVarbind[] array = this.getVarbinds();
        for (int index = 0; index < array.length; index++)
        {
            if (index > 0)
            {
                buf.append(",");
            }
            buf.append("[<");
            buf.append(index);
            buf.append(">");
            buf.append(array[index].toString());
            buf.append("]");
        }
        buf.append("}");

        return buf.toString();
    }
}
