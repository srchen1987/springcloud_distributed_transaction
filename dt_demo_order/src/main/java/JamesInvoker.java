import org.aopalliance.intercept.MethodInvocation;

import com.pttl.distributed.transaction.aspetct.TransactionInterceptInvoker;
import com.pttl.distributed.transaction.context.DistributedTransactionContext;

public class JamesInvoker implements TransactionInterceptInvoker{

	@Override
	public Object invoke(MethodInvocation invocation, DistributedTransactionContext tc) throws Throwable {
		// TODO Auto-generated method stub
		Object obj = invocation.proceed();
		if(obj instanceof JamesResponseMessage) {
			JamesResponseMessage jm = (JamesResponseMessage) obj;
			if(	jm.getCode()==500) {
				tc.setCancel(true);
			}
		}
		return obj;
	}

}
