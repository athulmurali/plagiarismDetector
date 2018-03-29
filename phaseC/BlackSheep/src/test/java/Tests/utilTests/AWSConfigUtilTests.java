package Tests.utilTests;
import com.blacksheep.util.AWSConfigUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AWSConfigUtilTests {

    AWSConfigUtil awsConfigUtil = new AWSConfigUtil();

    @Test
    public void TestAWSUTIL(){

        awsConfigUtil.setAwsAccessKey("key");
        awsConfigUtil.setAwsSecretKey("secret");
        awsConfigUtil.setAwsBucketName("Bucket");

        assertEquals("key",awsConfigUtil.getAwsAccessKey());
        assertEquals("Bucket",awsConfigUtil.getAwsBucketName());
        assertEquals("secret",awsConfigUtil.getAwsSecretKey());


    }
}
