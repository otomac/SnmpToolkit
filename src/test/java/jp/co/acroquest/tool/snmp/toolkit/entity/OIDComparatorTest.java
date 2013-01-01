//OIDComparatorTest.java ----
// History: 2009/11/22 - Create
package jp.co.acroquest.tool.snmp.toolkit.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;

/**
 * OIDComparatorクラスのテストケース。
 * 
 * @author akiba
 */
public class OIDComparatorTest
{
    @Test
    public void testCompare_001()
    {
        OIDComparator comp = new OIDComparator();

        // 長さ0の配列
        String[] array = new String[0];
        Arrays.sort(array, comp);
        assertEquals(array.length, 0);
    }

    @Test
    public void testCompare_002()
    {
        OIDComparator comp = new OIDComparator();

        // 長さ1の配列
        String[] array = new String[] {
                "1.2.3.4.5"
        };
        Arrays.sort(array, comp);
        assertEquals(array.length, 1);
        assertEquals(array[0], "1.2.3.4.5");
    }

    @Test
    public void testCompare_003()
    {
        OIDComparator comp = new OIDComparator();

        // 長さ2の配列（昇順ソート１）
        String[] array = new String[] {
                "1.2.3.4.5",
                "1.2.3.4.6"
        };
        Arrays.sort(array, comp);
        assertEquals(array.length, 2);
        assertEquals(array[0], "1.2.3.4.5");
        assertEquals(array[1], "1.2.3.4.6");
    }

    @Test
    public void testCompare_004()
    {
        OIDComparator comp = new OIDComparator();

        // 長さ2の配列（昇順ソート２）
        String[] array = new String[] {
                "1.2.3.4.6",
                "1.2.3.4.5"
        };
        Arrays.sort(array, comp);
        assertEquals(array.length, 2);
        assertEquals(array[0], "1.2.3.4.5");
        assertEquals(array[1], "1.2.3.4.6");
    }

    @Test
    public void testCompare_005()
    {
        OIDComparator comp = new OIDComparator();

        // 長さ2の配列（null同士）
        String[] array = new String[] {
                null,
                null
        };
        Arrays.sort(array, comp);
        assertEquals(array.length, 2);
        assertEquals(array[0], null);
        assertEquals(array[1], null);
    }

    @Test
    public void testCompare_006()
    {
        OIDComparator comp = new OIDComparator();

        // 長さ2の配列（右側だけnull）
        String[] array = new String[] {
                null,
                "1.2.3.4.5"
        };
        Arrays.sort(array, comp);
        assertEquals(array.length, 2);
        assertEquals(array[0], null);
        assertEquals(array[1], "1.2.3.4.5");
    }

    @Test
    public void testCompare_007()
    {
        OIDComparator comp = new OIDComparator();

        // 長さ2の配列（左側だけnull）
        String[] array = new String[] {
                "1.2.3.4.5",
                null
        };
        Arrays.sort(array, comp);
        assertEquals(array.length, 2);
        assertEquals(array[0], null);
        assertEquals(array[1], "1.2.3.4.5");
    }

    @Test
    public void testCompare_008()
    {
        OIDComparator comp = new OIDComparator();

        // 長さ2の配列（長さが異なる）
        String[] array = new String[] {
                "1.2.3.4.5",
                "1.2.3.4"
        };
        Arrays.sort(array, comp);
        assertEquals(array.length, 2);
        assertEquals(array[0], "1.2.3.4");
        assertEquals(array[1], "1.2.3.4.5");
    }

    @Test
    public void testCompare_009()
    {
        OIDComparator comp = new OIDComparator();

        // 長さ2の配列（中途半端なOID1）
        String[] array = new String[] {
                "1.2.3.4.5",
                "1.2.3.4."
        };
        
        try
        {
            Arrays.sort(array, comp);
            fail("NumberFormatException wasn't thrown.");
        }
        catch (NumberFormatException ex)
        {
        }
    }

    @Test
    public void testCompare_010()
    {
        OIDComparator comp = new OIDComparator();

        // 長さ2の配列（先頭がピリオドで始まるOID）
        String[] array = new String[] {
                "1.2.3.4",
                ".1.2.3.4.5"
        };
        
        try
        {
            Arrays.sort(array, comp);
            fail("NumberFormatException wasn't thrown.");
        }
        catch (NumberFormatException ex)
        {
        }
    }

    @Test
    public void testCompare_011()
    {
        OIDComparator comp = new OIDComparator();

        // 長さ2の配列（先頭がピリオドで始まるOID）
        String[] array = new String[] {
                "1.2.3.4.5.6",
                ".1.2.3.4.5"
        };
        
        try
        {
            Arrays.sort(array, comp);
            fail("NumberFormatException wasn't thrown.");
        }
        catch (NumberFormatException ex)
        {
        }
    }

    @Test
    public void testCompare_012()
    {
        OIDComparator comp = new OIDComparator();

        // 長さ2の配列（要素が1つしかないOID同士の比較１）
        String[] array = new String[] {
                "2",
                "1"
        };
        
        Arrays.sort(array, comp);
        assertEquals(array.length, 2);
        assertEquals(array[0], "1");
        assertEquals(array[1], "2");
    }

    @Test
    public void testCompare_013()
    {
        OIDComparator comp = new OIDComparator();

        // 長さ2の配列（要素が1つしかないOID同士の比較２）
        String[] array = new String[] {
                "10",
                "1"
        };
        
        Arrays.sort(array, comp);
        assertEquals(array.length, 2);
        assertEquals(array[0], "1");
        assertEquals(array[1], "10");
    }

    @Test
    public void testCompare_014()
    {
        OIDComparator comp = new OIDComparator();

        // 長さ2の配列（要素が1つしかないOID同士の比較３）
        String[] array = new String[] {
                ".10",
                ".1"
        };
        
        try
        {
            Arrays.sort(array, comp);
            fail("NumberFormatException wasn't thrown.");
        }
        catch (NumberFormatException ex)
        {
        }
    }

    @Test
    public void testCompare_015()
    {
        OIDComparator comp = new OIDComparator();

        // 長さ2の配列（digitの桁数が異なる）
        String[] array = new String[] {
                "1.10",
                "1.1"
        };
        
        Arrays.sort(array, comp);
        assertEquals(array.length, 2);
        assertEquals(array[0], "1.1");
        assertEquals(array[1], "1.10");
    }

    @Test
    public void testCompare_016()
    {
        OIDComparator comp = new OIDComparator();

        // 長さ2の配列（途中のdigitが異なる１）
        String[] array = new String[] {
                "1.2.1",
                "1.1.1"
        };
        
        Arrays.sort(array, comp);
        assertEquals(array.length, 2);
        assertEquals(array[0], "1.1.1");
        assertEquals(array[1], "1.2.1");
    }

    @Test
    public void testCompare_017()
    {
        OIDComparator comp = new OIDComparator();

        // 長さ2の配列（途中のdigitが異なる２）
        String[] array = new String[] {
                "1.10.1",
                "1.1.1"
        };
        
        Arrays.sort(array, comp);
        assertEquals(array.length, 2);
        assertEquals(array[0], "1.1.1");
        assertEquals(array[1], "1.10.1");
    }

    @Test
    public void testCompare_018()
    {
        OIDComparator comp = new OIDComparator();

        // 長さ2の配列（まったく同じ要素同士）
        String[] array = new String[] {
                "1.2.3",
                "1.2.3"
        };
        
        Arrays.sort(array, comp);
        assertEquals(array.length, 2);
        assertEquals(array[0], "1.2.3");
        assertEquals(array[1], "1.2.3");
    }

    @Test
    public void testCompare_019()
    {
        OIDComparator comp = new OIDComparator();

        // 長さ2の配列（解析不能１）
        String[] array = new String[] {
                "1.2.3.a.5",
                "1.2.3.4.10"
        };
        
        try
        {
            Arrays.sort(array, comp);
            fail("NumberFormatException wasn't thrown.");
        }
        catch (NumberFormatException ex)
        {
        }
    }

    @Test
    public void testCompare_020()
    {
        OIDComparator comp = new OIDComparator();

        // 長さ2の配列（解析不能２）
        String[] array = new String[] {
                "1.2.3..5",
                "1.2.3.4.10"
        };
        
        try
        {
            Arrays.sort(array, comp);
            fail("NumberFormatException wasn't thrown.");
        }
        catch (NumberFormatException ex)
        {
        }
    }

    @Test
    public void testCompare_021()
    {
        OIDComparator comp = new OIDComparator();

        // 長さ3の配列
        String[] array = new String[] {
                "1.2.33.4.5",
                "1.2.33.4.10",
                "1.2.33.4.6"
        };
        
        Arrays.sort(array, comp);
        assertEquals(array.length, 3);
        assertEquals(array[0], "1.2.33.4.5");
        assertEquals(array[1], "1.2.33.4.6");
        assertEquals(array[2], "1.2.33.4.10");
    }

    @Test
    public void testCompare_022()
    {
        OIDComparator comp = new OIDComparator();

        // TreeSetを利用するケース
        String[] array = new String[] {
                "1.2.3.4.10",
                "1.2.3.4.9"
        };
        Set<String> set = new TreeSet<String>(comp);
        set.add(array[0]);
        set.add(array[1]);
        
        // 配列をソートしたものと同じ順序で取り出されることを確認する
        Arrays.sort(array, comp);
        
        assertEquals(set.size(), 2);
        Iterator<String> ite = set.iterator();
        int index = 0;
        while (ite.hasNext())
        {
            String str = ite.next();
            assertEquals(str, array[index]);
            index++;
        }
    }

    @Test
    public void testCompare_023()
    {
        OIDComparator comp = new OIDComparator();

        // TreeSetを利用するケース（要素の削除）
        String[] array = new String[] {
                "1.2.3.4.4",
                "1.2.3.4.6",
                "1.2.3.4.5"
        };
        Set<String> set = new TreeSet<String>(comp);
        set.add(array[0]);
        set.add(array[1]);
        set.add(array[2]);
        set.remove(array[2]);
        
        // 配列をソートしたものと同じ順序で取り出されることを確認する
        String[] tmpArray = new String[2];
        System.arraycopy(array, 0, tmpArray, 0, 2);
        Arrays.sort(tmpArray, comp);
        
        assertEquals(set.size(), 2);
        Iterator<String> ite = set.iterator();
        int index = 0;
        while (ite.hasNext())
        {
            String str = ite.next();
            System.out.println("index=" + index + ": str=" + str + " ? array=" + tmpArray[index]);
            assertEquals(str, tmpArray[index]);
            index++;
        }
    }

    @Test
    public void testCompare_024()
    {
        OIDComparator comp = new OIDComparator();

        // TreeSetを利用するケース（要素の削除）
        String[] array = new String[] {
                "1.2.3.4.6",
                "1.2.3.4.10",
                "1.2.3.4.9"
        };
        Set<String> set = new TreeSet<String>(comp);
        set.add(array[0]);
        set.add(array[1]);
        set.add(array[2]);
        set.remove(array[2]);
        
        // 配列をソートしたものと同じ順序で取り出されることを確認する
        String[] tmpArray = new String[2];
        System.arraycopy(array, 0, tmpArray, 0, 2);
        Arrays.sort(tmpArray, comp);
        
        assertEquals(set.size(), 2);
        Iterator<String> ite = set.iterator();
        int index = 0;
        while (ite.hasNext())
        {
            String str = ite.next();
            System.out.println("index=" + index + ": str=" + str + " ? array=" + tmpArray[index]);
            assertEquals(str, tmpArray[index]);
            index++;
        }
    }

    @Test
    public void testCompare_024a()
    {
        OIDComparator comp = new OIDComparator();

        // TreeMapを利用するケース（要素の削除）
        String[] array = new String[] {
                "1.2.3.4.1.1",
                "1.2.3.4.1.14",
                "1.2.3.4.1.11",
                "1.2.3.4.1.9"
        };
        TreeMap<String, String> map = new TreeMap<String, String>(comp);
        map.put(array[0], array[0]);
        map.put(array[1], array[1]);
        map.put(array[2], array[2]);
        map.put(array[3], array[3]);
        
        Map<String, String> subMap = map.tailMap("1.2.3.4.1.10");
        
        // 配列と同じ順序で取り出されることを確認する
        String[] expected = new String[] { "1.2.3.4.1.11", "1.2.3.4.1.14" };
        
        assertEquals(subMap.size(), 2);
        Set<String> keySet = subMap.keySet();
        String[] keyArray = keySet.toArray(new String[2]);
        int index = 0;
        for (String key : keyArray)
        {
            System.out.println("index=" + index + ": key=" + key + " ? array=" + expected[index]);
            assertEquals(key, expected[index]);
            index++;
        }
    }

    @Test
    public void testCompare_024b()
    {
        OIDComparator comp = new OIDComparator();

        // TreeMapを利用するケース
        String[] array = new String[] {
                "1.3.6.1.4.1.4550.1.1.4.4.1.0.1",
                "1.3.6.1.4.1.4550.1.1.4.4.1.2.1",
                "1.3.6.1.4.1.4550.1.1.4.4.1.3.1",
                "1.3.6.1.4.1.4550.1.1.4.4.1.4.1",
                "1.3.6.1.4.1.4550.1.1.4.4.1.5.1",
                "1.3.6.1.4.1.4550.1.1.4.4.1.6.1",
                "1.3.6.1.4.1.4550.1.1.4.4.1.7.1",
                "1.3.6.1.4.1.4550.1.1.4.4.1.8.1",
                "1.3.6.1.4.1.4550.1.1.4.4.1.9.1",
                "1.3.6.1.4.1.4550.1.1.4.4.1.10.1",
                "1.3.6.1.4.1.4550.1.1.4.4.1.11.1",
                "1.3.6.1.4.1.4550.1.1.4.4.1.12.1",
                "1.3.6.1.4.1.4550.1.1.4.4.1.13.1",
                "1.3.6.1.4.1.4550.1.1.4.4.1.14.1",
                "1.3.6.1.4.1.4550.1.1.4.4.1.15.1",
                "1.3.6.1.4.1.4550.1.1.4.4.1.16.1",
                "1.3.6.1.4.1.4550.1.1.4.4.1.17.1",
                "1.3.6.1.4.1.4550.1.1.4.4.1.20.1"
        };
        TreeMap<String, String> map = new TreeMap<String, String>(comp);
        for (int cnt = 0; cnt < array.length; cnt++)
        {
            map.put(array[cnt], array[cnt]);
        }
        
        Map<String, String> subMap = map.tailMap("1.3.6.1.4.1.4550.1.1.4.4.1.10");
        
        // 配列と同じ順序で取り出されることを確認する
        String[] expected = new String[] { "1.3.6.1.4.1.4550.1.1.4.4.1.10.1",
                "1.3.6.1.4.1.4550.1.1.4.4.1.11.1",
                "1.3.6.1.4.1.4550.1.1.4.4.1.12.1",
                "1.3.6.1.4.1.4550.1.1.4.4.1.13.1",
                "1.3.6.1.4.1.4550.1.1.4.4.1.14.1",
                "1.3.6.1.4.1.4550.1.1.4.4.1.15.1",
                "1.3.6.1.4.1.4550.1.1.4.4.1.16.1",
                "1.3.6.1.4.1.4550.1.1.4.4.1.17.1",
                "1.3.6.1.4.1.4550.1.1.4.4.1.20.1"
        };
        
        assertEquals(subMap.size(), expected.length);
        Set<String> keySet = subMap.keySet();
        String[] keyArray = keySet.toArray(new String[expected.length]);
        int index = 0;
        for (String key : keyArray)
        {
            System.out.println("index=" + index + ": key=" + key + " ? array=" + expected[index]);
            assertEquals(key, expected[index]);
            index++;
        }
    }

    @Test
    public void testCompare_025()
    {
        OIDComparator comp = new OIDComparator();

        // 性能確認(4階層、100,000データ）
        String[] array = new String[100000];
        for (int index = 0; index < 100000; index++)
        {
            array[index] = "1.2.3.4." + (100000 - index);
        }
        
        // (1) Comparatorを使用しない場合
        Set<String> set_no_comparator = new TreeSet<String>();
        long start_no_comp = System.currentTimeMillis();
        for (int index = 0; index < array.length; index++)
        {
            set_no_comparator.add(array[index]);
        }
        long end_no_comp = System.currentTimeMillis();

        // (2) Comparatorを使用する場合
        long start_with_comp = System.currentTimeMillis();
        Set<String> set_with_comparator = new TreeSet<String>(comp);
        for (int index = 0; index < array.length; index++)
        {
            set_with_comparator.add(array[index]);
        }
        long end_with_comp = System.currentTimeMillis();
        
        long time_no_comp = end_no_comp - start_no_comp;
        long time_with_comp = end_with_comp - start_with_comp;
        
        double ratio = (double)time_with_comp / time_no_comp;
        if (ratio >= 100.0d)
        {
            fail("performance is low. ratio=" + ratio + " (no-comp=" + time_no_comp + ", with-comp=" + time_with_comp + ")");
        }
    }

    @Test
    public void testCompare_026()
    {
        OIDComparator comp = new OIDComparator();

        // 性能確認(10階層、100,000データ）
        String[] array = new String[100000];
        for (int index = 0; index < 100000; index++)
        {
            array[index] = "1.2.3.4.5.6.7.8.9." + (100000 - index);
        }
        
        // (1) Comparatorを使用しない場合
        Set<String> set_no_comparator = new TreeSet<String>();
        long start_no_comp = System.currentTimeMillis();
        for (int index = 0; index < array.length; index++)
        {
            set_no_comparator.add(array[index]);
        }
        long end_no_comp = System.currentTimeMillis();

        // (2) Comparatorを使用する場合
        long start_with_comp = System.currentTimeMillis();
        Set<String> set_with_comparator = new TreeSet<String>(comp);
        for (int index = 0; index < array.length; index++)
        {
            set_with_comparator.add(array[index]);
        }
        long end_with_comp = System.currentTimeMillis();
        
        long time_no_comp = end_no_comp - start_no_comp;
        long time_with_comp = end_with_comp - start_with_comp;
        
        double ratio = (double)time_with_comp / time_no_comp;
        if (ratio >= 100.0d)
        {
            fail("performance is low. ratio=" + ratio + " (no-comp=" + time_no_comp + ", with-comp=" + time_with_comp + ")");
        }
    }

    @Test
    public void testCompare_027()
    {
        OIDComparator comp = new OIDComparator();

        // 性能確認(10階層、10,000データ）
        String[] array = new String[10000];
        for (int index = 0; index < 10000; index++)
        {
            array[index] = "1.2.3.4.5.6.7.8.9." + (10000 - index);
        }
        
        // (1) Comparatorを使用しない場合
        Set<String> set_no_comparator = new TreeSet<String>();
        long start_no_comp = System.currentTimeMillis();
        for (int index = 0; index < array.length; index++)
        {
            set_no_comparator.add(array[index]);
        }
        long end_no_comp = System.currentTimeMillis();

        // (2) Comparatorを使用する場合
        long start_with_comp = System.currentTimeMillis();
        Set<String> set_with_comparator = new TreeSet<String>(comp);
        for (int index = 0; index < array.length; index++)
        {
            set_with_comparator.add(array[index]);
        }
        long end_with_comp = System.currentTimeMillis();
        
        long time_no_comp = end_no_comp - start_no_comp;
        long time_with_comp = end_with_comp - start_with_comp;
        
        double ratio = (double)time_with_comp / time_no_comp;
        if ((time_no_comp == 0 && time_with_comp >= 1000.0d) || ratio >= 100.0d)
        {
            fail("performance is low. ratio=" + ratio + " (no-comp=" + time_no_comp + ", with-comp=" + time_with_comp + ")");
        }
    }
}

