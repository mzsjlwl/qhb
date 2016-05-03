package tab.com.handsome.handsome;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.handsome.qhb.utils.LogUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dalvik.annotation.TestTarget;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }


    @Before
    public void init(){
        System.out.print("testbefore=========");
        LogUtils.e("log","testbefore=========");
    }

    @Test
    public void testAdd() throws Exception{
            System.out.print("testadd=======");
        LogUtils.e("log", "testadd=========");
    }

    @After
    public void destory(){
        System.out.print("testDestory========");
        LogUtils.e("log", "testDestory=========");

    }

}