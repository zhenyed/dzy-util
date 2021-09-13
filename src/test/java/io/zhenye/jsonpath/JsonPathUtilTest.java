package io.zhenye.jsonpath;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonPathUtilTest {

    @Test
    void parseObjectTest() throws JsonProcessingException {
        SomeThingObj obj = new SomeThingObj().setStr("abc").setNum(1);

        assertEquals(obj.getStr(), JsonPathUtil.parse(obj, "$.str"));
        assertEquals(obj.getNum(), JsonPathUtil.parse(obj, "$.num"));
        assertNull(JsonPathUtil.parse(obj, "$.aaa"));
    }

    @Test
    void parseList() throws JsonProcessingException {
        SomeThingObj obj1 = new SomeThingObj().setStr("aaa").setNum(1);
        SomeThingObj obj2 = new SomeThingObj().setStr(null).setNum(null);
        SomeThingObj obj3 = new SomeThingObj().setStr("ccc").setNum(3);
        List<SomeThingObj> source = List.of(obj1, obj2, obj3);

        List<String> strResult = (List) JsonPathUtil.parse(source, "$.[*].str");
        assertEquals(Arrays.asList("aaa", null, "ccc"), strResult);

        List<Integer> numResult = (List) JsonPathUtil.parse(source, "$.[*].num");
        assertEquals(Arrays.asList(1, null, 3), numResult);
    }

    @Test
    void parseSpecialList() throws JsonProcessingException {
        SomeThingObj obj1 = new SomeThingObj().setStr("aaa").setNum(1);
        SomeThingObj obj2 = new SomeThingObj().setStr("ccc").setNum(3);
        SomeThingObj obj3 = new SomeThingObj().setStr("ddd").setNum(1);
        List<SomeThingObj> source = List.of(obj1, obj2, obj3);

        assertEquals(List.of("aaa", "ddd"), JsonPathUtil.parse(source, "$[?(@.num==1)].str"));
        assertEquals(List.of(), JsonPathUtil.parse(source, "$[?(@.num==2)].str"));
        assertEquals(List.of("ccc"), JsonPathUtil.parse(source, "$[?(@.num==3)].str"));
        assertEquals("aaa", JsonPathUtil.parse(source, "$[0].str"));
    }

    @Test
    void parseListInner() throws JsonProcessingException {
        SomeThingObj obj1 = new SomeThingObj().setNum(1).setListStc(
                List.of("aaa", "", "ccc")
        );
        assertEquals(List.of("aaa", "", "ccc"), JsonPathUtil.parse(obj1, "$.listStc[*]"));

        SomeThingObj obj2 = new SomeThingObj().setListObj(
                List.of(
                        new SomeThingObj().setStr("aaa"),
                        new SomeThingObj().setStr("bbb")
                ));
        assertEquals(List.of("aaa", "bbb"), JsonPathUtil.parse(obj2, "$.listObj[*].str"));
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    static class SomeThingObj {
        private String str;
        private Integer num;
        private List<String> listStc;
        private List<SomeThingObj> listObj;
    }
}