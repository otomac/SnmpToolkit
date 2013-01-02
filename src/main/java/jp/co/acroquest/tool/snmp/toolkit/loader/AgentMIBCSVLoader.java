//AgentMIBCSVLoader.java ----
// History: 2009/05/20 - Create
package jp.co.acroquest.tool.snmp.toolkit.loader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import jp.co.acroquest.tool.snmp.toolkit.entity.Agent;
import jp.co.acroquest.tool.snmp.toolkit.entity.SnmpVarbind;

import org.supercsv.cellprocessor.ConvertNullTo;
import org.supercsv.cellprocessor.constraint.Unique;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

/**
 * AgentのMIBデータCSVファイルを読み込むローダークラス。<br/>
 * CSVファイルは、Excelで読み込める形式で、１行目にヘッダ文字列を指定しておく必要がある。<br/>
 * １行目のヘッダ文字列は、AgentオブジェクトのGetterメソッド名に転用されるため、変更しないこと。<br/>
 * 列名：
 * <ol>
 *   <li>oid : MIBデータのOID。</li>
 *   <li>type : MIBデータの型。string, octets, integer, hex, ipaddress, timeticks, object-idのいずれか。</li>
 *   <li>value : MIBの値。</li>
 *   <li>accessibility : 参照可能性。READ-WRITE(default), READ-ONLY, NOT-ACCESSIBLEのいずれか。</li>
 * </ol>
 *
 * @author akiba
 */
public class AgentMIBCSVLoader
{
    /** CSV読み込み時の規則を規定するCellProcessor。 */
    private final CellProcessor[] PROCESSORS = new CellProcessor[] {
        // oid
        new Unique(),
        // type
        null,
        // value
        null,
        // accessibility
        new ConvertNullTo("READ-WRITE")
    };

    /**
     * デフォルトコンストラクタ。
     */
    public AgentMIBCSVLoader()
    {
    }

    /**
     * 指定したパスに配置されているCSVファイルを読み込み、Agentデータを生成する。
     *
     * @param parent CSVファイルが配置されているディレクトリ。
     * @param filename CSVファイル名。
     * @return 読み込みに成功した結果、生成されたAgentオブジェクト。
     * @throws IOException 読み込みに失敗した場合。
     */
    public Agent load(String parent, String filename)
        throws IOException
    {
        Agent agent = null;

        // ディレクトリとファイル名からFileオブジェクトを生成する
        File csvFile = new File(parent, filename);

        // CSVファイルリーダー
        ICsvBeanReader reader = null;
        try
        {
            // Excelの読み込み規則を使用してCSVファイルを読み込む
            reader = new CsvBeanReader(new FileReader(csvFile), CsvPreference.EXCEL_PREFERENCE);

            // ヘッダを取得する
            final String[] headers = reader.getHeader(true);

            // Agentを生成し、１行１個のVarbindを生成し格納する
            agent = new Agent();
            while(true)
            {
                SnmpVarbind varbind = reader.read(SnmpVarbind.class, headers, PROCESSORS);
                if (varbind == null)
                {
                    break;
                }

                agent.addVarbind(varbind);
            }
        }
        catch (SuperCsvException exception)
        {
            IOException ioex = new IOException(exception.getLocalizedMessage());
            throw ioex;
        }
        finally
        {
            // 開かれているReaderはクローズする
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (IOException ioex)
                {
                }
            }
        }

        return agent;
    }

    /**
     * テスト用のメインメソッド。
     *
     * @param args 読み込むデータファイル。
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
        AgentMIBCSVLoader loader = new AgentMIBCSVLoader();
        Agent agent = loader.load(".", args[0]);
        System.out.println(agent.toString());
    }
}
