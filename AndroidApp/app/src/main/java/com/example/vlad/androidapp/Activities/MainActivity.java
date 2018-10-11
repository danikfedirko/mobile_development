package com.example.vlad.androidapp.Activities;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.vlad.androidapp.Adapters.ProductAdapter;
import com.example.vlad.androidapp.Entities.Product;
import com.example.vlad.androidapp.Entities.Products;
import com.example.vlad.androidapp.R;
import com.example.vlad.androidapp.ServerUtilities.LCBOClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.vlad.androidapp.ServerUtilities.LCBOUtility.generateRequest;


public class MainActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView)
    protected RecyclerView recyclerView;
    @BindView(R.id.textNoData)
    protected TextView textNoData;
    @BindView(R.id.pullToRefresh)
    protected SwipeRefreshLayout pullToRefresh;

    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        productAdapter = new ProductAdapter();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadData();

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    public void loadData() {
        LCBOClient client = generateRequest();
        Call<Products> call = client.allProducts();

        call.enqueue(new Callback<Products>() {
            @Override
            public void onResponse(Call<Products> call, Response<Products> response) {
                List<Product> products = response.body().getResult();

                textNoData.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                productAdapter.setProducts(products);

                recyclerView.setAdapter(productAdapter);

            }

            @Override
            public void onFailure(Call<Products> call, Throwable t) {
                recyclerView.setVisibility(View.GONE);
                textNoData.setVisibility(View.VISIBLE);
                t.getCause();
            }
        });

    }

}