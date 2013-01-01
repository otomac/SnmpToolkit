//SnmpManager.java ----
// History: 2009/08/18 - Create
package jp.co.acroquest.tool.snmp.toolkit.entity;

/**
 * SNMPのTrap送信先となるマネージャの設定を保持するエンティティ。
 *
 * @author akiba
 */
public class SnmpManager
{
    /** SNMPマネージャのアドレス指定。 */
    private String address_;

    /**
     * SNMPマネージャのアドレスを設定する。
     *
     * @param address SNMPマネージャのアドレス。
     */
    public void setManagerAddress(String address)
    {
        this.address_ = address;
    }

    /**
     * SNMPマネージャのアドレスを取得する。
     *
     * @return SNMPマネージャのアドレス。
     */
    public String getManagerAddress()
    {
        return this.address_;
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        String retStr = "SnmpManager{address=" + this.address_ + "}";
        return retStr;
    }
}
