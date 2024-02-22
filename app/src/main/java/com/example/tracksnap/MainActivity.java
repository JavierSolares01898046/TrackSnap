package com.example.tracksnap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private MaterialToolbar toolbar;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        NavigationView navView = findViewById(R.id.nav_view);
        NavigationUI.setupWithNavController(navView, navController);

        //create an “AppBarConfiguration” object
        AppBarConfiguration.Builder builder = new AppBarConfiguration.Builder(navController.getGraph());

        builder.setOpenableLayout(drawerLayout); //before builder.build()
        AppBarConfiguration appBarConfiguration = builder.build();

        //link “AppBarConfigtureation” to the toolbar
        NavigationUI.setupWithNavController(findViewById(R.id.toolbar), navController, appBarConfiguration);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
//            int destId = destination.getId();
            if (destination.getId() == R.id.homeFragment) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
        });

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_drawer, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
    }
}