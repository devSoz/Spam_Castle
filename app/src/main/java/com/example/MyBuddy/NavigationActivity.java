package com.example.MyBuddy;

        import android.os.Bundle;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.Menu;
        import com.google.android.material.snackbar.Snackbar;
        import com.google.android.material.navigation.NavigationView;

        import androidx.appcompat.app.ActionBarDrawerToggle;
        import androidx.appcompat.widget.Toolbar;
        import androidx.core.view.GravityCompat;
        import androidx.fragment.app.Fragment;
        import androidx.fragment.app.FragmentManager;
        import androidx.fragment.app.FragmentTransaction;
        import androidx.drawerlayout.widget.DrawerLayout;
        import androidx.appcompat.app.AppCompatActivity;



public class NavigationActivity extends AppCompatActivity {

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;

    private ActionBarDrawerToggle drawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);

        Class fragmentClass = MainActivity.class;
        Bundle bundle = new Bundle();
        bundle.putString("option", "a");
        Fragment fragment = null;
        //fragment = new MainActivity();
        fragment.setArguments(bundle);
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.flContent, fragment);
        tx.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        String selected = new String("");
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_female:
                selected = "f";
                fragmentClass = MainActivity.class;
                break;
            case R.id.nav_all:
                selected = "a";
                fragmentClass = MainActivity.class;
                break;
            case R.id.nav_male:
                selected = "m";
                fragmentClass = MainActivity.class;
                break;
            case R.id.nav_fav:
              //  fragmentClass = FavHeroDisplay.class;
                break;
            case R.id.nav_help:
                //fragmentClass = HelpActivity.class;
                break;
            default:
                fragmentClass = MainActivity.class;
        }

        try {
            Bundle bundle = new Bundle();
            bundle.putString("option", selected);
           // fragment = (Fragment) fragmentClass.newInstance();
            //fragment.setArguments(bundle);

        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }
}