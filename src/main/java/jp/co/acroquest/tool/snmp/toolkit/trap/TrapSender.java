//TrapSender.java ----
// History: 2004/11/22 - Create
package jp.co.acroquest.tool.snmp.toolkit.trap;

import jp.co.acroquest.tool.snmp.toolkit.SnmpToolkitException;
import jp.co.acroquest.tool.snmp.toolkit.entity.TrapData;

/**
 * Trap送信を行うクラスの基底インタフェース。
 *
 * @author akiba
 * @version 1.0
 */
public interface TrapSender
{
	/**
     * Trapを送信する時のコミュニティ名を設定する。
     *
     * @param comm Trapコミュニティ名。
     */
    public void setCommunity(String comm);

    /**
     * 指定したTrapDataを使用してTrapを送信する。
     *
     * @param trapData 送信するTrapData。
     * @throws SnmpToolkitException Trap送信時に発生した例外。
     */
    public void sendTrap(TrapData trapData)
    	throws SnmpToolkitException;
}
