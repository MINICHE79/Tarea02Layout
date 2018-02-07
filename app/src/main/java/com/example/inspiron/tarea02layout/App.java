package com.example.inspiron.tarea02layout;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class App extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout swipe=null;
    private ArrayList<listItem> items = null;
    private ListView lista            = null;
    private adapterListItems adapter  = null;
    private int cuantas = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app);
        lista = (ListView)findViewById(R.id.List);
        items = new ArrayList<>();
        for (int i=0;i<cuantas;i++){
            items.add(new listItem("Titulo"+i,"Subtitulo"+i));
        }
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipe.setOnRefreshListener(this);
        swipe.setColorSchemeResources(
                android.R.color.holo_green_dark);
        adapter = new adapterListItems(this,items);
        lista.setAdapter(adapter);
        lista.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                int filasuperior = (lista == null || lista.getChildCount() == 0)? 0 :lista.getChildAt(0).getTop();
                swipe.setEnabled(filasuperior >=0);
            }
        });
    }

    @Override
    public void onRefresh() {
        swipe.setRefreshing(true);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(lista.getChildCount()>0)
                    items.remove(--cuantas);

                adapter.notifyDataSetChanged();
                swipe.setRefreshing(false);


            }
        },100);
    }

    class adapterListItems extends ArrayAdapter<listItem> {

        Context context;

        public adapterListItems(@NonNull Context context, ArrayList<listItem> users) {
            super(context, 0,users);
            this.context = context;
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //return super.getView(position, convertView, parent);
            View item = convertView;

            listItem user = getItem(position);

            if(item == null) {
                item = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
            }
            TextView tvName = (TextView)item.findViewById(R.id.textView);
            TextView tvHome = (TextView)item.findViewById(R.id.textView2);
            tvName.setText(user.getTitle());
            tvHome.setText(user.getSubtitle());
            return item;
        }
    }
}
