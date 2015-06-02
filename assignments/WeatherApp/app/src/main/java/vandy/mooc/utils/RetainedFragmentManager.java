package vandy.mooc.utils;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;

public class RetainedFragmentManager {

    private final WeakReference<FragmentManager> mFragmentManager;
    private final String mRetainedFragmentTag;
    private RetainedFragment mRetainedFragment;

    public RetainedFragmentManager(FragmentManager fragmentManager, String retainedFragmentTag) {

        mFragmentManager = new WeakReference<>(fragmentManager);
        mRetainedFragmentTag = retainedFragmentTag;
    }

    public boolean firstTimeIn() {
        try {
            // Find the RetainedFragment on Activity restarts
            mRetainedFragment = (RetainedFragment) mFragmentManager.get()
                .findFragmentByTag(mRetainedFragmentTag);

            // Do nothing if we're already set up
            if (mRetainedFragment != null) {
                return false;
            }

            // Create a new RetainedFragment.
            mRetainedFragment = new RetainedFragment();

            // Commit this RetainedFragment to the FragmentManager.
            mFragmentManager.get().beginTransaction().add(mRetainedFragment, mRetainedFragmentTag)
                .commit();

            return true;

        } catch (NullPointerException npe) {
            return false;
        }
    }

    public void put(String key, Object object) {mRetainedFragment.put(key, object); }
    public void put(Object object) {
        put(object.getClass().getName(), object);
    }

    public <T> T get(String key) {
        return (T) mRetainedFragment.get(key);
    }

    public Activity getActivity() {
        return mRetainedFragment.getActivity();
    }


    public static class RetainedFragment extends Fragment {

        private final HashMap<String, Object> mData = new HashMap<>();

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }

        public void put(String key, Object object) { mData.put(key, object); }
        public void put(Object object) { put(object.getClass().getName(), object); }

        public <T> T get(String key) {return (T) mData.get(key);}
    }
}
