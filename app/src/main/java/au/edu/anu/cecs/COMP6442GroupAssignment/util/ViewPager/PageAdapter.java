package au.edu.anu.cecs.COMP6442GroupAssignment.util.ViewPager;

import android.app.Activity;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

import au.edu.anu.cecs.COMP6442GroupAssignment.R;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.UserPostDAO;

public class PageAdapter extends FragmentPagerAdapter {

    private int num;
    private HashMap<Integer, Fragment> mFragmentHashMap = new HashMap<>();
    private AppCompatActivity currAct;

    public PageAdapter(FragmentManager fm, int num, AppCompatActivity currAct) {
        super(fm);
        this.num = num;
        this.currAct = currAct;
    }

    @Override
    public Fragment getItem(int position) {

        return createFragment(position);
    }

    @Override
    public int getCount() {
        return num;
    }

    private Fragment createFragment(int pos) {
        Fragment fragment = mFragmentHashMap.get(pos);

        if (fragment == null) {
            switch (pos) {
                case 0:
                    fragment = new PostFragment();
                    Log.i("fragment", "fragment1");
                    break;
                case 1:
                    fragment = new PostFragment();
                    Log.i("fragment", "fragment2");
                    break;
                case 2:
                    fragment = new PostFragment();
                    Log.i("fragment", "fragment3");
                    break;
                case 3:
                    fragment = new ProfileFragment();
                    Log.i("fragment", "fragment4");
                    break;
            }
            mFragmentHashMap.put(pos, fragment);
        }
        return fragment;
    }
}

