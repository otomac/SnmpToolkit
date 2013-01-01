// Traps.java ----
// History: 2004/03/08 - Create
package jp.co.acroquest.tool.snmp.toolkit.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * TrapDataをリストで管理するクラス。
 * 
 * @author akiba
 * @version 1.0
 */
public class Traps
{
    /** 追加されているTrapDataを管理するリスト。 */
    private List<TrapData> trapList_;

    /**
     * コンストラクタ。
     */
    public Traps()
    {
        super();

        this.trapList_ = new ArrayList<TrapData>();
    }

    /**
     * TrapDataを追加する。
     * 
     * @param trapData TrapData。
     */
    public void addTrapData(TrapData trapData)
    {
        this.trapList_.add(trapData);
    }

    /**
     * 追加されている全てのTrapDataを配列で取得する。
     * 
     * @return 追加されている全てのTrapData。
     */
    public TrapData[] getAllTrapData()
    {
        TrapData[] trapDataArray = new TrapData[this.trapList_.size()];
        trapDataArray = (TrapData[]) this.trapList_.toArray(trapDataArray);

        return trapDataArray;
    }
}
