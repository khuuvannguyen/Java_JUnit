package com.nguyen.app;

import org.apache.commons.lang3.RandomStringUtils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

class MockitoAnswer implements Answer {

    @Override
    public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
        return 1;
    }
}

class MyClass {
    private List<String> list;

    public MyClass(List<String> list) {
        this.list = list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public List<String> getList() {
        return list;
    }
}

public class MockAnswer {
    @Test
    public void test() {
        List list = mock(List.class, new MockitoAnswer());
        int actual = list.size();
        verify(list).size();
        assertThat(actual).isEqualTo(1);
    }

    @Test
    public void test2() {
        List list = mock(List.class);
        when(list.get(anyInt())).thenAnswer(
                new Answer<Object>() {
                    @Override
                    public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                        return "abc";
                    }
                }
        );
        assertThat(list.get(anyInt())).isEqualTo("abc");
    }

    @Test
    public void test3() {
        MockSettings mockSettings = withSettings().defaultAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                return false;
            }
        });
        List list = mock(List.class, mockSettings);
        boolean actual = list.add(RandomStringUtils.randomAlphabetic(6));
        verify(list).add(anyString());
        assertThat(actual).isFalse();
    }

    @Test
    public void test4() {
        MyClass myClass = mock(MyClass.class);
        when(myClass.getList()).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                return new ArrayList<>(Arrays.asList("a", "b", "c"));
            }
        });
        List<String> actual = myClass.getList();
        verify(myClass).getList();
        List<String> expected = new ArrayList<>(Arrays.asList("a", "b", "c"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void test5() {
        List list = mock(List.class, new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                return new ArrayList<>(Arrays.asList("a", "b", "c"));
            }
        });
        Object actual = list.get(anyInt());
        verify(list).get(anyInt());
        assertThat(actual).isEqualTo(Arrays.asList("a", "b", "c"));
    }
}
