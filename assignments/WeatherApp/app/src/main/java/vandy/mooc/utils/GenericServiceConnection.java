package vandy.mooc.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.IInterface;
import android.util.Log;

public class GenericServiceConnection<AIDLInterface extends IInterface> implements ServiceConnection {

    private static final String STUB = "Stub";
    private static final String AS_INTERFACE = "asInterface";
    private static final Class<?>[] AI_PARAMS = {IBinder.class};

    private AIDLInterface mInterface;
    private final Class<?> mStub;
    private final Method mAsInterface;

    public AIDLInterface getInterface() {
        return mInterface;
    }

    public GenericServiceConnection(final Class<AIDLInterface> aidl) {
        Class<?> stub = null;
        Method method = null;
        for (final Class<?> c : aidl.getDeclaredClasses()) {
            if (c.getSimpleName().equals(STUB)) {
                try {
                    stub = c;
                    method = stub.getMethod(AS_INTERFACE,
                            AI_PARAMS);
                    break;
                } catch (final NoSuchMethodException e) { // Should not be possible
                    e.printStackTrace();
                }
            }
        }
        mStub = stub;
        mAsInterface = method;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        System.out.println("Service connection");
        try {
            mInterface = (AIDLInterface)mAsInterface.invoke(mStub, new Object[]{service});
            System.out.println("Connected!");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mInterface = null;
    }
}
