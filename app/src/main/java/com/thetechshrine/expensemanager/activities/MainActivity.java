package com.thetechshrine.expensemanager.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import com.thetechshrine.expensemanager.R;
import com.thetechshrine.expensemanager.components.BottomMenu;
import com.thetechshrine.expensemanager.database.SessionManager;
import com.thetechshrine.expensemanager.fragments.main.CategoriesFragment;
import com.thetechshrine.expensemanager.fragments.main.DiscoverFragment;
import com.thetechshrine.expensemanager.fragments.main.ProfileFragment;
import com.thetechshrine.expensemanager.fragments.main.StatsFragment;
import com.thetechshrine.expensemanager.utils.viewpager.ZoomOutTransformation;

import java.util.ArrayList;
import java.util.List;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import io.realm.Realm;


public class MainActivity extends AppCompatActivity {

    private Realm realm;

    private BottomMenu bottomMenu;
    private ViewPager viewPager;
    private ImageButton mainButton;

    private BottomMenuAdapter adapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!SessionManager.from(this).isLoggedIn()) {
            Intent intent = new Intent(MainActivity.this, SplashActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        bindViews();
        setupViews();
    }

    private void bindViews() {
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.main_view_pager);
        bottomMenu = findViewById(R.id.main_bottom_menu);
        mainButton = findViewById(R.id.main_main_button);
    }

    private void setupViews() {
        initViewPager();
        initBottomMenu();
        initMainButton();
    }

    private void initMainButton() {
        mainButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateExpenseActivity.class);
            startActivity(intent);
        });
    }

    private void initViewPager() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(DiscoverFragment.newInstance());
        fragments.add(CategoriesFragment.newInstance());
        fragments.add(StatsFragment.newInstance());
        fragments.add(ProfileFragment.newInstance());

        adapter = new BottomMenuAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);

        ZoomOutTransformation transformation = new ZoomOutTransformation();
        viewPager.setPageTransformer(true, transformation);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomMenu.setCurrentMenu(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initBottomMenu() {
        bottomMenu.setOnBottomMenuChangeListener(position -> viewPager.setCurrentItem(position));
    }

    private class BottomMenuAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> fragments;

        public BottomMenuAdapter(@NonNull FragmentManager fragmentManager, List<Fragment> fragments) {
            super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.fragments = fragments;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        realm = Realm.getDefaultInstance();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (realm != null) realm.close();
    }
}
