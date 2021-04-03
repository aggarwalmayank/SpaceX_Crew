package com.appsaga.spacex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.appsaga.spacex.Adapter.MyListAdapter;
import com.appsaga.spacex.Database.CrewDBModel;
import com.appsaga.spacex.Database.CrewDao;
import com.appsaga.spacex.Database.DataBase;
import com.appsaga.spacex.WebModel.Crew;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private  ArrayList<Crew> allCrew=new ArrayList<Crew>();
    private RecyclerView recyclerView;
    private  WebService webService;
    private DataBase database;
    private String DB_NAME="CrewDetails";
    private CrewDao crewDao;
    private ConnectivityManager connectivityManager;
    private MyListAdapter adapter;
    private FloatingActionButton deleteDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        deleteDB = (FloatingActionButton)  findViewById(R.id.delete_btn);

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onStart();
                pullToRefresh.setRefreshing(false);
            }
        });
        database= Room.databaseBuilder(MainActivity.this,DataBase.class,DB_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        crewDao=database.crewDao();
//        Log.d("value ","start");

    }

    @Override
    protected void onStart() {
        super.onStart();
        deleteDB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                crewDao.deleteAll();
                adapter.notifyDataSetChanged();
            }
        });
        if (!allCrew.isEmpty()) {
            allCrew.clear();
            adapter.notifyDataSetChanged();

        }
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if ( connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected() )
        {
            crewDao.deleteAll();
            webService = WebService.retrofit.create(WebService.class);
            Call<List<Crew>> call = webService.getPosts();
            call.enqueue(new Callback<List<Crew>>() {
                @Override
                public void onResponse(Call<List<Crew>> call, Response<List<Crew>> response) {
                    if(response.isSuccessful())
                    {

                        for(Crew crew: response.body())
                        {
                            allCrew.add(crew);

                            crewDao.insertCrew(new CrewDBModel(crew.getName(),
                                    crew.getAgency(),crew.getImage(),crew.getWikipedia(),
                                    crew.getStatus()));
                        }
                        adapter=new MyListAdapter(allCrew,MainActivity.this);
                        if(!allCrew.isEmpty())
                        {
                            recyclerView.setVisibility(View.VISIBLE);
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);
                        }
                        else
                        {
                            adapter.notifyDataSetChanged();
                            recyclerView.setVisibility(View.GONE);
                        }
                    }

                }

                @Override
                public void onFailure(Call<List<Crew>> call, Throwable t) {


                }
            });
        }

        else
        {
            List<CrewDBModel> allFromDB= crewDao.getCrewDetails();
            allCrew.clear();
            for(CrewDBModel crewDBModel: allFromDB)
            {
                Crew c=new Crew();
                c.setAgency(crewDBModel.getAgency());
                c.setImage(crewDBModel.getImage());
                c.setName(crewDBModel.getName());
                c.setWikipedia(crewDBModel.getWikipedia());
                c.setStatus(crewDBModel.getStatus());
                allCrew.add(c);
            }
            adapter=new MyListAdapter(allCrew,MainActivity.this);
            if(!allCrew.isEmpty())
            {
                recyclerView.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            }
            else
            {
                adapter.notifyDataSetChanged();
                recyclerView.setVisibility(View.GONE);
            }
        }

    }


}
