/*
 * Created by ttdevs at 16-4-14 下午3:52.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.android;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ttdevs.android.calendar.CalendarActivity;
import com.ttdevs.android.utils.FileUtils;
import com.ttdevs.android.utils.Utils;
import com.ttdevs.markdown.MarkDownView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author ttdevs
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.mdv_content)
    MarkDownView mdvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
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
            Utils.comeOnBaby(this, AboutActivity.class);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_conceal:
                Utils.comeOnBaby(this, ConcealActivity.class);
                break;
            case R.id.nav_reactive:
                Utils.comeOnBaby(this, ReactiveActivity.class);
                break;
            case R.id.nav_jni:
                Utils.comeOnBaby(this, JNIActivity.class);
                break;
            case R.id.nav_web_socket:
                Utils.comeOnBaby(this, WebSocketActivity.class);
                break;
            case R.id.nav_web_socket_ok:
                Utils.comeOnBaby(this, WebSocketOKActivity.class);
                break;
            case R.id.nav_backup:
                Utils.comeOnBaby(this, BackupActivity.class);
                break;
            case R.id.nav_custom_view:
                Utils.comeOnBaby(this, CustomViewActivity.class);
                break;
            case R.id.nav_transformer:
                Utils.comeOnBaby(this, TransformerActivity.class);
                break;
            case R.id.nav_markdown:
                Utils.comeOnBaby(this, MarkdownActivity.class);
                break;
            case R.id.nav_ucrop:
                Utils.comeOnBaby(this, UCropActivity.class);
                break;
            case R.id.nav_flipper:
                Utils.comeOnBaby(this, ViewFlipperActivity.class);
                break;
            case R.id.nav_tablayout:
                Utils.comeOnBaby(this, TabLayoutActivity.class);
                break;
            case R.id.nav_include_text:
                Utils.comeOnBaby(this, TempActivity.class);
                break;
            case R.id.nav_argb_evaluator:
                Utils.comeOnBaby(this, ArgbEvaluatorActivity.class);
                break;
            case R.id.nav_spannable:
                Utils.comeOnBaby(this, SpannableActivity.class);
                break;
            case R.id.nav_video_view:
                Utils.comeOnBaby(this, com.ttdevs.android.PlayerActivity.class);
                break;
            case R.id.nav_socket:
                Utils.comeOnBaby(this, SocketActivity.class);
                break;
            case R.id.nav_calendar:
                Utils.comeOnBaby(this, CalendarActivity.class);
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
