/*
 * Created by ttdevs at 16-4-14 下午3:52.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.android;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ttdevs.android.utils.FileUtils;
import com.ttdevs.android.utils.Utils;
import com.ttdevs.markdown.MarkDownView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.mdv_content)
    MarkDownView mdvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        loadReadmeData();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            loadReadmeData(); // TODO
            Toast.makeText(this, getString(R.string.action_about), Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_conceal:
                Utils.comeOnBaby(this, ConcealActivity.class);
                break;
            case R.id.nav_endless:
                Utils.comeOnBaby(this, EndLessActivity.class);
                break;
            case R.id.nav_ucrop:
                Utils.comeOnBaby(this, UCropActivity.class);
                break;
            case R.id.nav_tablayout:
                Utils.comeOnBaby(this, TabLayoutActivity.class);
                break;
            case R.id.nav_markdown:
                Utils.comeOnBaby(this, MarkdownActivity.class);
                break;
            case R.id.nav_reactive:
                Utils.comeOnBaby(this, ReactiveActivity.class);
                break;
            case R.id.nav_flipper:
                Utils.comeOnBaby(this, ViewFlipperActivity.class);
                break;
            case R.id.nav_jni:
                Utils.comeOnBaby(this, JNIActivity.class);
                break;
            case R.id.nav_draw_circle:
                Utils.comeOnBaby(this, IndicatorViewActivity.class);
                break;

            default:
                break;
        }

        // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadReadmeData() {
        mdvContent.loadMarkdown(FileUtils.readAssets("README.md"));
    }
}
