package function.functionVisibilityAndDecarations;

import beforetest.ContractPrepareTest;
import network.platon.autotest.junit.annotations.DataSource;
import network.platon.autotest.junit.enums.DataSourceType;
import network.platon.contracts.Visibility;
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
 * @title 验证函数四种可见性external,public,internal,private
 * @description:
 * @author: liweic
 * @create: 2020/01/02 16:01
 **/

public class VisibilityTest extends ContractPrepareTest {

    @Before
    public void before() {
        this.prepare();
    }

    @Test
    @DataSource(type = DataSourceType.EXCEL, file = "test.xls", sheetName = "Sheet1",
            author = "liweic", showName = "function.VisibilityTest-函数可见性测试")
    public void getter() {
        try {
            Visibility visibility = Visibility.deploy(web3j, transactionManager, provider).send();

            String contractAddress = visibility.getContractAddress();
            TransactionReceipt tx = visibility.getTransactionReceipt().get();
            collector.logStepPass("Visibility deploy successfully.contractAddress:" + contractAddress + ", hash:" + tx.getTransactionHash());

            //验证public可见性
            BigInteger pubdata = visibility.fpub(new BigInteger("10")).send();
            collector.logStepPass("public可见函数返回值：" + pubdata);
            collector.assertEqual(new BigInteger("13"),pubdata);

            //验证external可见性
            BigInteger extdata = visibility.fe(new BigInteger("10")).send();
            collector.logStepPass("external可见函数返回值：" + extdata);
            collector.assertEqual(new BigInteger("12"),extdata);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


