//OIDComparator.java ----
// History: 2009/11/22 - Create
package jp.co.acroquest.tool.snmp.toolkit.entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * OID順にソートを行うコンパレータ。
 * 
 * @author akiba
 */
public class OIDComparator implements Comparator<String>
{
    /** 左側が大きいと判定した場合。 */
    private static final int LEFT   = 1;

    /** 右側が大きいと判定した場合。 */
    private static final int RIGHT  = -1;

    /** 双方が同じであると判定した場合。 */
    private static final int EQUALS = 0;

    /** OIDを分割するデリミタ文字。 */
    private static final int OID_DELIM = '.';

    /**
     * {@inheritDoc}
     */
    public int compare(String str1, String str2)
    {
        if (str1 == null)
        {
            if (str2 == null)
            {
                // 双方ともnullの場合は等値と判定する
                return EQUALS;
            }
            else
            {
                // 左側だけがnullの場合は、右側を大と判定する
                return RIGHT;
            }
        }

        // 右側だけがnullの場合は、左側を大と判定する
        if (str2 == null)
        {
            return LEFT;
        }

        // 以下は双方ともnullでない場合

        // OID文字列をdigit毎に分割する
        Integer[] leftArray = this.splitOid(str1);
        Integer[] rightArray = this.splitOid(str2);

        for (int cnt = 0; cnt < leftArray.length; cnt++)
        {
            // 途中まで同じで、左側の方が長い場合
            if (cnt >= rightArray.length)
            {
                return LEFT;
            }

            // 途中の階層が異なる値になっている場合は、その階層の差で順序を決める
            if (leftArray[cnt] < rightArray[cnt])
            {
                return RIGHT;
            }
            
            if (rightArray[cnt] < leftArray[cnt])
            {
                return LEFT;
            }
        }
        
        // 途中まで同じで、右側の方が長い場合
        if (leftArray.length < rightArray.length)
        {
            return RIGHT;
        }

        // 全ての階層で同じ値だった場合は等値とみなす
        return EQUALS;
    }
    
    /**
     * OID文字列をInteger配列に分割する。<br/>
     * このメソッドは、<code>String#split()</code>が持つパフォーマンスの問題を解消するために導入された。<br/>
     * 
     * @param str nullではないOID文字列。<br/>
     *            空のdigitが抽出されるようなOIDを与えるとNumberFormatExceptionが発生する。
     *            具体的には、先頭がピリオドで始まる／末尾がピリオドで終わる／途中にピリオドが連続するものはNG。
     * @return 分割されたInteger配列。
     */
    private Integer[] splitOid(String str)
    {
        List<Integer> list = new ArrayList<Integer>();
        
        int subPos = 0;
        while (true)
        {
            int delimPos = str.indexOf(OID_DELIM, subPos);
            if (delimPos < 0)
            {
                String subStr = str.substring(subPos);
                list.add(Integer.parseInt(subStr));
                break;
            }
            
            String subStr = str.substring(subPos, delimPos);
            list.add(Integer.parseInt(subStr));
            subPos = delimPos + 1;
        }
        
        Integer[] array = new Integer[list.size()];
        array = list.toArray(array);
        return array;
    }
}
