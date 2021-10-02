package au.edu.anu.cecs.COMP6442GroupAssignment.util.State;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import au.edu.anu.cecs.COMP6442GroupAssignment.MainActivity;
import au.edu.anu.cecs.COMP6442GroupAssignment.R;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Adapter.PageAdapter;

public class SessionState implements UserState {
    private final MainActivity main;

    public SessionState(MainActivity main) {
        this.main = main;
    }

    @Override
    public void setContent() {
        main.setContentView(R.layout.activity_session);
    }

    @Override
    public void onCreate() {
        TabLayout tabLayout = (TabLayout) main.findViewById(R.id.tabLayout);
        final ViewPager viewPager = (ViewPager) main.findViewById(R.id.viewPager);
        viewPager.setAdapter(new PageAdapter(main.getSupportFragmentManager(),
                tabLayout.getTabCount(), main));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }
}
