package function.functionVisibilityAndDecarations;

import beforetest.ContractPrepareTest;
import network.platon.autotest.junit.annotations.DataSource;
import network.platon.autotest.junit.enums.DataSourceType;
import network.platon.contracts.Getter;
import network.platon.utils.DataChangeUtil;
import org.junit.Before;
import org.junit.Test;
import org.web3j.abi.datatypes.Array;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @title 验证getter函数,状态变量所创建的访问器函数，与变量同名。以internal访问时，按状态变量的方式使用，若以external的方式访问时，则需要通过访问器函数
 * @description:
 * @author: liweic
 * @create: 2020/01/02 15:01
 **/

public class GetterTest extends ContractPrepareTest {

    @Before
    public void before() {
        this.prepare();
    }

    @Test
    @DataSource(type = DataSourceType.EXCEL, file = "test.xls", sheetName = "Sheet1",
            author = "liweic", showName = "function.GetterTest-getter函数测试")
    public void getter() {
        try {
            Getter getter = Getter.deploy(web3j, transactionManager, provider).send();

            String contractAddress = getter.getContractAddress();
            TransactionReceipt tx = getter.getTransactionReceipt().get();
            collector.logStepPass("getter deploy successfully.contractAddress:" + contractAddress + ", hash:" + tx.getTransactionHash());

            //验证编译器创建的getter函数
            BigInteger data = getter.data().send();
            collector.logStepPass("getter函数返回值：" + data);
            collector.assertEqual(new BigInteger("10"),data);

            Tuple2 data2 = getter.f().send();
            //以internal访问时，按状态变量的方式使用
            collector.logStepPass("interal访问data的返回值：" + data2.getValue1());
            collector.assertEqual(new BigInteger("10"),data2.getValue1());
            //以external的方式访问时，则需要通过访问器函数
            collector.logStepPass("external访问data的返回值：" + data2.getValue2());
            collector.assertEqual(new BigInteger("10"),data2.getValue2());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

