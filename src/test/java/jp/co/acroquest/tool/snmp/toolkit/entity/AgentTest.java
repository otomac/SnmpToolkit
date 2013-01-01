// AgentTest.java ----
// History: Mar.03.2012 - Create
package jp.co.acroquest.tool.snmp.toolkit.entity;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Agentクラスのテストケース。
 * 
 * @author akiba
 */
public class AgentTest {

	/**
	 * Test method for {@link jp.co.acroquest.tool.snmp.toolkit.entity.Agent#findObject(java.lang.String, boolean)}.
	 */
	@Test
	public void testFindObject_001() {
		Agent agent = new Agent();
		agent.addVarbind(createVarbind("1.2.3.1", "integer", "1"));
		agent.addVarbind(createVarbind("1.2.3.2", "integer", "2"));
		agent.addVarbind(createVarbind("1.2.3.3", "integer", "3"));
		agent.addVarbind(createVarbind("1.2.4",   "integer", "10"));
		
		SnmpVarbind retVarbind = agent.findObject("1.2.3", false);
		assertEquals(createVarbind("1.2.3.1", "integer", "1"), retVarbind);
	}
	
	/**
	 * Test method for {@link jp.co.acroquest.tool.snmp.toolkit.entity.Agent#findObject(java.lang.String, boolean)}.
	 */
	@Test
	public void testFindObject_002() {
		Agent agent = new Agent();
		agent.addVarbind(createVarbind("1.2.3.1", "integer", "1"));
		agent.addVarbind(createVarbind("1.2.3.2", "integer", "2"));
		agent.addVarbind(createVarbind("1.2.3.3", "integer", "3"));
		agent.addVarbind(createVarbind("1.2.4",   "integer", "10"));
		
		SnmpVarbind retVarbind = agent.findObject("1.2.3.1", false);
		assertEquals(createVarbind("1.2.3.2", "integer", "2"), retVarbind);
	}

	/**
	 * Test method for {@link jp.co.acroquest.tool.snmp.toolkit.entity.Agent#findObject(java.lang.String, boolean)}.
	 */
	@Test
	public void testFindObject_003() {
		Agent agent = new Agent();
		agent.addVarbind(createVarbind("1.2.3.1", "integer", "1"));
		agent.addVarbind(createVarbind("1.2.3.2", "integer", "2"));
		agent.addVarbind(createVarbind("1.2.3.3", "integer", "3"));
		agent.addVarbind(createVarbind("1.2.4",   "integer", "10"));
		
		SnmpVarbind retVarbind = agent.findObject("1.2.3.3", false);
		assertEquals(createVarbind("1.2.4", "integer", "10"), retVarbind);
	}

	/**
	 * Test method for {@link jp.co.acroquest.tool.snmp.toolkit.entity.Agent#findObject(java.lang.String, boolean)}.
	 */
	@Test
	public void testFindObject_004() {
		Agent agent = new Agent();
		agent.addVarbind(createVarbind("1.2.3.1", "integer", "1"));
		agent.addVarbind(createVarbind("1.2.3.2", "integer", "2"));
		agent.addVarbind(createVarbind("1.2.3.3", "integer", "3"));
		agent.addVarbind(createVarbind("1.2.4",   "integer", "10"));
		
		SnmpVarbind retVarbind = agent.findObject("1.2.4", false);
		assertEquals(null, retVarbind);
	}

	/**
	 * Test method for {@link jp.co.acroquest.tool.snmp.toolkit.entity.Agent#findObject(java.lang.String, boolean)}.
	 */
	@Test
	public void testFindObject_005() {
		Agent agent = new Agent();
		agent.addVarbind(createVarbind("1.2.3.1", "integer", "1"));
		agent.addVarbind(createVarbind("1.2.3.2", "integer", "2"));
		agent.addVarbind(createVarbind("1.2.3.3", "integer", "3"));
		agent.addVarbind(createVarbind("1.2.4",   "integer", "10"));
		
		SnmpVarbind retVarbind = agent.findObject("1.2.3.4", false);
		assertEquals(null, retVarbind);
	}

	/**
	 * Test method for {@link jp.co.acroquest.tool.snmp.toolkit.entity.Agent#findObject(java.lang.String, boolean)}.
	 */
	@Test
	public void testFindObject_006() {
		Agent agent = new Agent();
		agent.addVarbind(createVarbind("1.2.3.3", "integer", "3"));
		agent.addVarbind(createVarbind("1.2.4",   "integer", "10"));
		agent.addVarbind(createVarbind("1.2.3.2", "integer", "2"));
		agent.addVarbind(createVarbind("1.2.3.1", "integer", "1"));
		
		SnmpVarbind retVarbind = agent.findObject("1.2.3", false);
		assertEquals(createVarbind("1.2.3.1", "integer", "1"), retVarbind);
	}
	
	/**
	 * Test method for {@link jp.co.acroquest.tool.snmp.toolkit.entity.Agent#findObject(java.lang.String, boolean)}.
	 */
	@Test
	public void testFindObject_007() {
		Agent agent = new Agent();
		agent.addVarbind(createVarbind("1.2.3.3", "integer", "3"));
		agent.addVarbind(createVarbind("1.2.4",   "integer", "10"));
		agent.addVarbind(createVarbind("1.2.3.2", "integer", "2"));
		agent.addVarbind(createVarbind("1.2.3.1", "integer", "1"));
		
		SnmpVarbind retVarbind = agent.findObject("1.2.3.1", false);
		assertEquals(createVarbind("1.2.3.2", "integer", "2"), retVarbind);
	}

	/**
	 * Test method for {@link jp.co.acroquest.tool.snmp.toolkit.entity.Agent#findObject(java.lang.String, boolean)}.
	 */
	@Test
	public void testFindObject_008() {
		Agent agent = new Agent();
		agent.addVarbind(createVarbind("1.2.3.3", "integer", "3"));
		agent.addVarbind(createVarbind("1.2.4",   "integer", "10"));
		agent.addVarbind(createVarbind("1.2.3.2", "integer", "2"));
		agent.addVarbind(createVarbind("1.2.3.1", "integer", "1"));
		
		SnmpVarbind retVarbind = agent.findObject("1.2.3.3", false);
		assertEquals(createVarbind("1.2.4", "integer", "10"), retVarbind);
	}

	/**
	 * Test method for {@link jp.co.acroquest.tool.snmp.toolkit.entity.Agent#findObject(java.lang.String, boolean)}.
	 */
	@Test
	public void testFindObject_009() {
		Agent agent = new Agent();
		agent.addVarbind(createVarbind("1.2.3.3", "integer", "3"));
		agent.addVarbind(createVarbind("1.2.4",   "integer", "10"));
		agent.addVarbind(createVarbind("1.2.3.2", "integer", "2"));
		agent.addVarbind(createVarbind("1.2.3.1", "integer", "1"));
		
		SnmpVarbind retVarbind = agent.findObject("1.2.4", false);
		assertEquals(null, retVarbind);
	}

	/**
	 * Test method for {@link jp.co.acroquest.tool.snmp.toolkit.entity.Agent#findObject(java.lang.String, boolean)}.
	 */
	@Test
	public void testFindObject_010() {
		Agent agent = new Agent();
		agent.addVarbind(createVarbind("1.2.3.3", "integer", "3"));
		agent.addVarbind(createVarbind("1.2.4",   "integer", "10"));
		agent.addVarbind(createVarbind("1.2.3.2", "integer", "2"));
		agent.addVarbind(createVarbind("1.2.3.1", "integer", "1"));
		
		SnmpVarbind retVarbind = agent.findObject("1.2.3.4", false);
		assertEquals(null, retVarbind);
	}
	
	/**
	 * Test method for {@link jp.co.acroquest.tool.snmp.toolkit.entity.Agent#findObject(java.lang.String, boolean)}.
	 */
	@Test
	public void testFindObject_011() {
		Agent agent = new Agent();
		agent.addVarbind(createVarbind("1.2.3.1", "integer", "1"));
		agent.addVarbind(createVarbind("1.2.3.2", "integer", "2"));
		agent.addVarbind(createVarbind("1.2.3.3", "integer", "3"));
		agent.addVarbind(createVarbind("1.2.4",   "integer", "10"));
		
		SnmpVarbind retVarbind = agent.findObject("1.2.3.1", true);
		assertEquals(createVarbind("1.2.3.1", "integer", "1"), retVarbind);
	}
	
	/**
	 * Test method for {@link jp.co.acroquest.tool.snmp.toolkit.entity.Agent#findObject(java.lang.String, boolean)}.
	 */
	@Test
	public void testFindObject_012() {
		Agent agent = new Agent();
		agent.addVarbind(createVarbind("1.2.3.1", "integer", "1"));
		agent.addVarbind(createVarbind("1.2.3.2", "integer", "2"));
		agent.addVarbind(createVarbind("1.2.3.3", "integer", "3"));
		agent.addVarbind(createVarbind("1.2.4",   "integer", "10"));
		
		SnmpVarbind retVarbind = agent.findObject("1.2.3.3", true);
		assertEquals(createVarbind("1.2.3.3", "integer", "3"), retVarbind);
	}
	
	/**
	 * Test method for {@link jp.co.acroquest.tool.snmp.toolkit.entity.Agent#findObject(java.lang.String, boolean)}.
	 */
	@Test
	public void testFindObject_013() {
		Agent agent = new Agent();
		agent.addVarbind(createVarbind("1.2.3.1", "integer", "1"));
		agent.addVarbind(createVarbind("1.2.3.2", "integer", "2"));
		agent.addVarbind(createVarbind("1.2.3.3", "integer", "3"));
		agent.addVarbind(createVarbind("1.2.4",   "integer", "10"));
		
		SnmpVarbind retVarbind = agent.findObject("1.2.4", true);
		assertEquals(createVarbind("1.2.4", "integer", "10"), retVarbind);
	}
	
	/**
	 * Test method for {@link jp.co.acroquest.tool.snmp.toolkit.entity.Agent#findObject(java.lang.String, boolean)}.
	 */
	@Test
	public void testFindObject_014() {
		Agent agent = new Agent();
		agent.addVarbind(createVarbind("1.2.3.1", "integer", "1"));
		agent.addVarbind(createVarbind("1.2.3.2", "integer", "2"));
		agent.addVarbind(createVarbind("1.2.3.3", "integer", "3"));
		agent.addVarbind(createVarbind("1.2.4",   "integer", "10"));
		
		SnmpVarbind retVarbind = agent.findObject("1", false);
		assertEquals(createVarbind("1.2.3.1", "integer", "1"), retVarbind);
	}
	
	/**
	 * Test method for {@link jp.co.acroquest.tool.snmp.toolkit.entity.Agent#findObject(java.lang.String, boolean)}.
	 */
	@Test
	public void testFindObject_015() {
		Agent agent = new Agent();
		agent.addVarbind(createVarbind("1.2.3.1", "integer", "1"));
		agent.addVarbind(createVarbind("1.2.3.2", "integer", "2"));
		agent.addVarbind(createVarbind("1.2.3.3", "integer", "3"));
		agent.addVarbind(createVarbind("1.2.4",   "integer", "10"));
		
		SnmpVarbind retVarbind = agent.findObject("1", true);
		assertEquals(null, retVarbind);
	}
	
	private SnmpVarbind createVarbind(String oid, String type, String value)
	{
		SnmpVarbind varbind = new SnmpVarbind();
		varbind.setOid(oid);
		varbind.setType(type);
		varbind.setValue(value);
		return varbind;
	}
}
