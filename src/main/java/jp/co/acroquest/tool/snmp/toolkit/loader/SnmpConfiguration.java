// SnmpConfiguration.java ----
// History: 2004/03/07 - Create
// 2009/07/25 - URI指定を不要とするための修正
package jp.co.acroquest.tool.snmp.toolkit.loader;

import java.io.File;
import java.io.IOException;

import jp.co.acroquest.tool.snmp.toolkit.entity.SnmpConfigItem;
import jp.co.acroquest.tool.snmp.toolkit.entity.SnmpManager;
import jp.co.acroquest.tool.snmp.toolkit.entity.SnmpManagerList;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

/**
 * SNMP設定を保持するクラス。
 * 
 * @author akiba
 * @version 1.0
 */
public class SnmpConfiguration
{
    /** 設定アイテム。 */
    private SnmpConfigItem           item_     = null;

    /** このクラスのインスタンス。 */
    private static SnmpConfiguration config__  = null;

    /** ルートノード名。 */
    private static final String      ROOT_NODE = "config";

    /**
     * このクラスのインスタンスを初期化する。<br>
     * getInstance()メソッドを使用してインスタンスを取得する前に、必ず実行すること。<br>
     * このメソッドの呼び出しは初回のみ有効であり、２回目以降の呼び出しは何もしない。
     * 
     * @param path 読み込む設定ファイル(XML)へのパス。
     * @throws IOException
     * @throws SAXException
     */
    public static void initialize(String path) throws IOException, SAXException
    {
        if (config__ != null)
        {
            return;
        }

        config__ = new SnmpConfiguration(path);
    }

    /**
     * このクラスの生成済みインスタンスを取得する。<br>
     * ただし、initialize()メソッドを呼び出す前は常にnullを返す。
     * 
     * @return このクラスの生成済みインスタンス。初期化されていない場合は常にnull。
     */
    public static SnmpConfiguration getInstance()
    {
        return config__;
    }

    /**
     * 設定クラスを初期化する。
     */
    private SnmpConfiguration(String path) throws IOException, SAXException
    {
        super();
        loadConfiguration(path);
    }

    /**
     * コンストラクタから呼び出され、XMLファイルから設定値を取得する。
     * 
     * @param path 設定ファイルへのパス。
     * @throws IOException
     * @throws SAXException
     */
    private void loadConfiguration(String path) throws IOException, SAXException
    {
        Digester digester = new Digester();
        digester.setValidating(false);
        digester.push(this);

        digester.addObjectCreate(ROOT_NODE, SnmpConfigItem.class.getName());
        digester.addSetNext(ROOT_NODE, "setSnmpConfigItem", SnmpConfigItem.class.getName());
        digester.addCallMethod(ROOT_NODE + "/property", "setProperty", 2);
        digester.addCallParam(ROOT_NODE + "/property", 0, "name");
        digester.addCallParam(ROOT_NODE + "/property", 1, "value");

        digester.addObjectCreate(ROOT_NODE + "/managers", SnmpManagerList.class.getName());
        digester.addSetNext(ROOT_NODE + "/managers", "setSnmpManagerList", SnmpManagerList.class
                .getName());
        digester.addObjectCreate(ROOT_NODE + "/managers/manager", SnmpManager.class.getName());
        digester.addSetNext(ROOT_NODE + "/managers/manager", "addSnmpManager", SnmpManager.class
                .getName());
        digester.addCallMethod(ROOT_NODE + "/managers/manager", "setManagerAddress", 0);

        // URI指定を不要とするためにFileオブジェクトを通す
        digester.parse(new File(path));
    }

    /**
     * Snmp設定を保存する。
     * 
     * @param item Snmp設定。
     */
    public void setSnmpConfigItem(SnmpConfigItem item)
    {
        this.item_ = item;
    }

    /**
     * Snmp設定を取得する。
     * 
     * @return Snmp設定。
     */
    public SnmpConfigItem getSnmpConfigItem()
    {
        return this.item_;
    }

    /**
     * Object#toString()のオーバーライド。
     * 
     * @return このオブジェクトの文字列表現。
     */
    public String toString()
    {
        String str = "SnmpConfiguration={" + this.item_.toString() + "}";
        return str;
    }

    public static void main(String[] args) throws Exception
    {
        SnmpConfiguration.initialize(args[0]);
        SnmpConfiguration config = SnmpConfiguration.getInstance();
        SnmpManagerList mgrList = config.getSnmpConfigItem().getSnmpManagerList();
        for (SnmpManager mgr : mgrList.getSnmpManagers())
        {
            System.out.println(mgr);
        }
    }
}
