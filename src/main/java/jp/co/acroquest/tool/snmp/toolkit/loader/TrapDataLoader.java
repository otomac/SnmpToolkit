// TrapDataLoader.java ----
// History: 2004/03/07 - Create
// 2009/07/25 - URI指定を不要とするための修正
package jp.co.acroquest.tool.snmp.toolkit.loader;

import java.io.File;
import java.io.IOException;

import jp.co.acroquest.tool.snmp.toolkit.entity.SnmpVarbind;
import jp.co.acroquest.tool.snmp.toolkit.entity.TrapData;
import jp.co.acroquest.tool.snmp.toolkit.entity.Traps;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

/**
 * Digesterを使用して、XMLファイルからTrapデータを読み込むローダ。
 *
 * @author akiba
 * @version 1.0
 */
public class TrapDataLoader
{
    /** Trapデータを記述したXMLを読み込む為のDigester。 */
    private Digester digester_;

    /**
     * コンストラクタ。
     */
    public TrapDataLoader()
    {
        super();

        this.digester_ = new Digester();

        // --------------------------------------------------------------------
        // <traps>要素の追加指定
        // --------------------------------------------------------------------
        // 1. traps 要素は、Trapsクラスを生成する
        this.digester_.addObjectCreate("traps", Traps.class);

        // --------------------------------------------------------------------
        // <traps/trap-data>要素の追加指定
        // --------------------------------------------------------------------
        // 1. traps/trap-data 要素は、TrapDataクラスを生成する
        this.digester_.addObjectCreate("traps/trap-data", TrapData.class);
        // 2. 生成した TrapDataオブジェクトは、TrapsクラスのaddTrapData()メソッドで追加する
        this.digester_.addSetNext("traps/trap-data", "addTrapData", TrapData.class.getName());
        // 3. traps/trap-data/trap-oid 要素は、setTrapOid()メソッドを呼び出す
        this.digester_.addBeanPropertySetter("traps/trap-data/trap-oid", "trapOid");
        // 4. traps/trap-data/enterprise 要素は、setEnterprise()メソッドを呼び出す
        this.digester_.addBeanPropertySetter("traps/trap-data/enterprise");

        this.digester_.addBeanPropertySetter("traps/trap-data/generic");

        this.digester_.addBeanPropertySetter("traps/trap-data/specific");
        // 5. traps/trap-data@* 要素は、set*()メソッドを呼び出す
        this.digester_.addSetProperties("traps/trap-data");

        // --------------------------------------------------------------------
        // <traps/trap-data/varbind>の追加指定
        // --------------------------------------------------------------------
        // 1. traps/trap-data/varbind 要素は、SnmpVarbindクラスを生成する
        this.digester_.addObjectCreate("traps/trap-data/varbind", SnmpVarbind.class);
        // 2. 生成したSnmpVarbindオブジェクトは、addVarbind()メソッドを呼び出してTrapDataに追加する
        this.digester_.addSetNext("traps/trap-data/varbind", "addVarbind");
        // 3. traps/trap-data/varbind/oid 要素は、setOid()メソッドを呼び出す
        this.digester_.addBeanPropertySetter("traps/trap-data/varbind/oid");
        // 4. traps/trap-data/varbind/value 要素は、setValue()メソッドを呼び出す
        this.digester_.addBeanPropertySetter("traps/trap-data/varbind/value");
        // 5. traps/trap-data/varbind/value@type 要素は、setType()メソッドを呼び出す
        this.digester_.addSetProperties("traps/trap-data/varbind/value", "type", "type");
    }

    /**
     * Trapデータをロードする。
     *
     * @param path Trapデータを記述したXMLファイルへのパス。
     * @return Trapデータ。
     * @throws IOException 読み込みに失敗した時。
     */
    public Traps load(String path) throws IOException
    {
        Traps traps;
        try
        {
            // URI指定を不要とするためにFileクラスを通す
            File datafile = new File(path);

            // Digesterを使用してTrapデータをロードする
            traps = (Traps) this.digester_.parse(datafile);
        }
        catch (SAXException e)
        {
            throw new IOException("SAXException occured.\n" + e.toString());
        }

        return traps;
    }

    /**
     * テスト用のメインメソッド。
     *
     * @param args 読み込むデータファイル。
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
        TrapDataLoader loader = new TrapDataLoader();
        Traps traps = loader.load(args[0]);

        TrapData[] datas = traps.getAllTrapData();
        for (int index = 0; index < datas.length; index++)
        {
            System.out.println("[" + index + "] --------------------------");
            System.out.println(datas[index].toString());
        }
    }
}
