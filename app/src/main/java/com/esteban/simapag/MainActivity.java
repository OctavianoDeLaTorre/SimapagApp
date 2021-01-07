package com.esteban.simapag;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.esteban.simapag.ui.LoginActivity;
import com.esteban.simapag.ui.fragment.HorarioFragment;
import com.esteban.simapag.ui.fragment.PerfilFragment;
import com.esteban.simapag.ui.fragment.RegistroFragment;
import com.esteban.simapag.ui.fragment.TrabajadoresFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniciarVistas();
        iniciarMenuLateral();
    }

    private void iniciarMenuLateral() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    private void iniciarVistas() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        new MaterialAlertDialogBuilder(this)
                .setTitle("Salir")
                .setIcon(R.drawable.ic_salir)
                .setMessage("¿Cerrar sesion?")
                .setPositiveButton("CERRAR",  (dialog, which) ->{
                    startActivity(new Intent(this, LoginActivity.class));
                    dialog.dismiss();
                    finish();
                })
                .setNegativeButton("CANCELAR",  (dialog, which) ->{
                    dialog.dismiss();
                })
                .show();


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return navController.navigateUp()
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.nav_perfil:
                fragment = new PerfilFragment();
                break;

            case R.id.nav_registro:
                fragment = new RegistroFragment();
                break;

            case R.id.nav_horario:
                fragment = new HorarioFragment();
                break;

            case R.id.nav_trabajadores:
                fragment = new TrabajadoresFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment).addToBackStack(null).commit();
        item.setChecked(true);
        getSupportActionBar().setTitle(item.getTitle());
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}