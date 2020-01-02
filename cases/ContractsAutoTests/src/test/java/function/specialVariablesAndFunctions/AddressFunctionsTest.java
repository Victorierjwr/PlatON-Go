package function.specialVariablesAndFunctions;

import beforetest.ContractPrepareTest;
import network.platon.autotest.junit.annotations.DataSource;
import network.platon.autotest.junit.enums.DataSourceType;
import network.platon.contracts.AddressFunctions;
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
 * @title 验证地址相关函数
 * @description:
 * @author: liweic
 * @create: 2020/01/02 11:30
 **/

public class AddressFunctionsTest extends ContractPrepareTest {

    @Before
    public void before() {
        this.prepare();
    }

    @Test
    @DataSource(type = DataSourceType.EXCEL, file = "test.xls", sheetName = "Sheet1",
            author = "liweic", showName = "function.AddressFunctionsTest-地址相关函数测试")
    public void Addressfunctions() {
        try {
            AddressFunctions addressfunctions = AddressFunctions.deploy(web3j, transactionManager, provider).send();

            String contractAddress = addressfunctions.getContractAddress();
            TransactionReceipt tx = addressfunctions.getTransactionReceipt().get();
            collector.logStepPass("Addressfunctions deploy successfully.contractAddress:" + contractAddress + ", hash:" + tx.getTransactionHash());

            //验证balance(地址账户)函数
            BigInteger money = addressfunctions.getBalance("0x03f0e0a226f081a5daecfda222cafc959ed7b800").send();
            collector.logStepPass("地址账户getBalance函数返回值：" + money);
            int num = money.toString().length();
            collector.assertEqual(78,num);

            //验证balance(合约账户)函数
            BigInteger contractmoney = addressfunctions.getBalanceOf().send();
            collector.logStepPass("合约账户getBalance函数返回值：" + contractmoney);
            int num2 = contractmoney.toString().length();
            collector.assertEqual(1,num2);

            //验证transfer函数
            BigInteger addresspremoney = addressfunctions.getBalance("0x8a9B36694F1eeeb500c84A19bB34137B05162EC4").send();
            collector.logStepPass("转账前余额：" + addresspremoney);
            TransactionReceipt result = addressfunctions.transfer("0x8a9B36694F1eeeb500c84A19bB34137B05162EC4",new BigInteger("100")).send();
            BigInteger addressaftermoney = addressfunctions.getBalance("0x8a9B36694F1eeeb500c84A19bB34137B05162EC4").send();
            collector.logStepPass("转账后余额：" + addressaftermoney);
            int a = Integer.valueOf(addressaftermoney.toString());
            int b = Integer.valueOf(addresspremoney.toString());
            int transfercounts = a - b;
            collector.assertEqual(100,transfercounts);

            //验证send函数
            BigInteger sendbefore = addressfunctions.getBalance("0x8a9B36694F1eeeb500c84A19bB34137B05162EC4").send();
            collector.logStepPass("转账前余额：" + sendbefore);
            TransactionReceipt result2 = addressfunctions.send("0x8a9B36694F1eeeb500c84A19bB34137B05162EC4",new BigInteger("10000")).send();
            BigInteger addressaftersend = addressfunctions.getBalance("0x8a9B36694F1eeeb500c84A19bB34137B05162EC4").send();
            collector.logStepPass("转账后余额：" + addressaftersend);
            int c = Integer.valueOf(addressaftersend.toString());
            int d = Integer.valueOf(sendbefore.toString());
            int sendcount = c - d;
            collector.assertEqual(1,sendcount);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
