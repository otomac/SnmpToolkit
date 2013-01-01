// SnmpVarbind.java ----
// History: 2004/03/07 - Create
package jp.co.acroquest.tool.snmp.toolkit.entity;

import java.io.Serializable;

/**
 * Varbindオブジェクト。
 *
 * @author akiba
 */
public class SnmpVarbind implements Serializable
{
    /** シリアルUID。 */
    private static final long  serialVersionUID             = 2664529399476520952L;

    /** READ-WRITE */
    public static final String ACCESSIBILITY_READ_WRITE     = "READ-WRITE";

    /** READ-ONLY */
    public static final String ACCESSIBILITY_READ_ONLY      = "READ-ONLY";

    /** NOT-ACCESSIBLE */
    public static final String ACCESSIBILITY_NOT_ACCESSIBLE = "NOT-ACCESSIBLE";

    /** VarbindのObjectID。 */
    private String             oid_;

    /** Varbindの値。 */
    private String             value_;

    /** Varbindの値の型。 */
    private String             type_;

    /** Access許可を表す値。 */
    private String             accessibility_ = ACCESSIBILITY_READ_WRITE;

    /**
     * コンストラクタ。
     */
    public SnmpVarbind()
    {
        super();
    }

    /**
     * VarbindのObjectIDを取得する。
     *
     * @return VarbindのObjectID。
     */
    public String getOid()
    {
        return this.oid_;
    }

    /**
     * Varbindの値を取得する。
     *
     * @return Varbindの値。
     */
    public String getValue()
    {
        return this.value_;
    }

    /**
     * VarbindのObjectIDを設定する。
     *
     * @param oid ObjectID。
     */
    public void setOid(String oid)
    {
        this.oid_ = oid;
    }

    /**
     * Varbindの値を取得する。
     *
     * @param value Varbindの値。
     */
    public void setValue(String value)
    {
        this.value_ = value;
    }

    /**
     * Varbindの値の型を取得する。
     *
     * @return Varbindの値の型。
     */
    public String getType()
    {
        return this.type_;
    }

    /**
     * Varbindの値の型を設定する。
     *
     * @param type Varbindの値の型。
     */
    public void setType(String type)
    {
        this.type_ = type;
    }

    /**
     * Access許可を表す値を取得する。
     *
     * @return Access許可を表す値。
     */
    public String getAccessibility()
    {
        return this.accessibility_;
    }

    /**
     * Access許可を表す値を設定する。
     *
     * @param ccessibility Access許可を表す値。
     */
    public void setAccessibility(String accessibility)
    {
        if (accessibility == null || accessibility.trim().equals(""))
        {
            this.accessibility_ = ACCESSIBILITY_READ_WRITE;
        }
        else
        {
            this.accessibility_ = accessibility;
        }
    }

    /**
     * 指定されたオブジェクトが自身と等しいかについて検証する。
     *
     * @return 指定されたオブジェクトがnullでなく、SnmpVarbindクラスのオブジェクトであり、 かつ全ての属性が等しい場合にtrueを返す。
     */
    public boolean equals(Object obj)
    {
        boolean isEqual = false;
        if (obj != null && obj instanceof SnmpVarbind)
        {
            SnmpVarbind given = (SnmpVarbind) obj;

            boolean oidEquals = given.getOid().equals(this.oid_);
            boolean typeEquals = given.getType().equals(this.type_);
            boolean valueEquals = given.getValue().equals(this.value_);
            boolean accessEquals = given.getAccessibility().equals(this.accessibility_);
            isEqual = (oidEquals && typeEquals && valueEquals && accessEquals);
        }

        return isEqual;
    }

    /**
     * このオブジェクトの文字列表現を取得する。
     *
     * @return このオブジェクトの文字列表現。
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        StringBuilder buf = new StringBuilder();

        buf.append("{oid=");
        buf.append(this.oid_);
        buf.append(",access=");
        buf.append(this.accessibility_);
        buf.append(",value=<");
        buf.append(this.type_);
        buf.append(">");
        buf.append(this.value_);
        buf.append("}");

        return buf.toString();
    }
}
