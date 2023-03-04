package com.example.sumipubli_geolocalizacion;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sumipubli_geolocalizacion.databinding.ActivityHomeAdministradorBinding;

public class HomeAdministrador extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeAdministradorBinding binding;
    MenuItem UpdateDatosAdmin, UpdatePasswordAdmin, CerrarSesionAdmin;
    int idAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeAdministradorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarHomeAdministrador.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_negocio, R.id.nav_usuario, R.id.nav_listNegocio, R.id.nav_listUsuario)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_administrador);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_administrador, menu);
        UpdateDatosAdmin = menu.findItem(R.id.updateDatosAdmin);
        UpdateDatosAdmin.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent i = new Intent(getApplicationContext(), UpdateDatos.class);
                //i.putExtra("id", idUser);
                startActivity(i);
                return false;
            }
        });
        UpdatePasswordAdmin = menu.findItem(R.id.updatePasswordAdmin);
        UpdatePasswordAdmin.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent i = new Intent(getApplicationContext(), UpdateDatos.class);
                //i.putExtra("id", idUser);
                startActivity(i);
                return false;
            }
        });
        CerrarSesionAdmin = menu.findItem(R.id.cerrarSesionAdmin);
        CerrarSesionAdmin.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Preferences.guardarPreferenceBoolean(getApplicationContext(),false,Preferences.PREFERENCE_ESTADO_BUTTON_SESION);
                startActivity(new Intent(getApplicationContext(), Login.class));

                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_administrador);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}