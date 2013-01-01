// SnmpConfigItem.java ----
// History: 2004/03/07 - Create
// 2009/05/20 - クラス名変更
package jp.co.acroquest.tool.snmp.toolkit.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * SnmpToolkitの設定データを表す。
 *
 * @author akiba
 * @version 1.0
 */
public class SnmpConfigItem
{
    /** 設定値を格納するマップ。 */
    private Map<String, Object> configMap_;

    /** SNMPマネージャのリスト。 */
    private SnmpManagerList     managerList_;

    /** Registryのデフォルトポート番号。 */
    private static final int    DEFAULT_PORT = 10000;

    /**
     * コンストラクタ。
     */
    public SnmpConfigItem()
    {
        super();

        this.configMap_ = new HashMap<String, Object>();
    }

    /**
     * SNMPマネージャのリストを設定する。
     *
     * @param mgrList SNMPマネージャのリスト。
     */
    public void setSnmpManagerList(SnmpManagerList mgrList)
    {
        this.managerList_ = mgrList;
    }

    /**
     * SNMPマネージャのリストを取得する。
     *
     * @return SNMPマネージャのリスト。
     */
    public SnmpManagerList getSnmpManagerList()
    {
        return this.managerList_;
    }

    /**
     * 設定値をマップに追加する。<br>
     * このメソッドは、Digesterから呼び出される。
     *
     * @param name 設定値の名称。
     * @param value 設定値。
     */
    public void setProperty(String name, Object value)
    {
        this.configMap_.put(name, value);
    }

    /**
     * データディレクトリ名を取得する。
     *
     * @return データディレクトリ名。
     */
    public String getDataDir()
    {
        String dataDir = (String) this.configMap_.get("data-dir");
        return dataDir;
    }

    /**
     * 送信先ポート番号を取得する。
     *
     * @return 送信先ポート番号。
     */
    public int getRemotePort()
    {
        int port = this.getIntValue("remote-port", DEFAULT_PORT);
        return port;
    }

    /**
     * 設定値を格納したマップから、指定した名称のパラメータをint値として取得する。
     *
     * @param name 取得するパラメータの名称。
     * @param defvalue パラメータが指定されていなかった場合のデフォルト値。
     * @return 取得したintパラメータ。
     */
    private int getIntValue(String name, int defvalue)
    {
        int value = defvalue;
        String valueObj = (String) this.configMap_.get(name);
        if (valueObj != null)
        {
            value = Integer.parseInt(valueObj);
        }

        return value;
    }

    /**
     * このオブジェクトの文字列表現を取得する。<br>
     * このメソッドは内部で保持するマップのtoString()メソッドに依存する。
     *
     * @return このオブジェクトの文字列表現。
     */
    public String toString()
    {
        return this.configMap_.toString();
    }
}
