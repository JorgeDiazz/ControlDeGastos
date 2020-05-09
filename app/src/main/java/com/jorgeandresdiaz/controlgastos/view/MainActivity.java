package com.jorgeandresdiaz.controlgastos.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jorgeandresdiaz.controlgastos.R;
import com.jorgeandresdiaz.controlgastos.database.TinyDB;
import com.jorgeandresdiaz.controlgastos.model.Transaction;
import com.jorgeandresdiaz.controlgastos.view.fragments.HomeFragment;
import com.jorgeandresdiaz.controlgastos.view.fragments.TransactionFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements TransactionFragment.Callback {

    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView bottomNavigationView;

    private static final String TRANSACTIONS_KEY = "TRANSACTIONS_KEY";

    private TinyDB tinydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initDatabase();
        initBottomNavigationView();
        openHomeFragment();
    }

    private void initDatabase() {
        tinydb = new TinyDB(getApplicationContext());
    }

    private void initBottomNavigationView() {
        bottomNavigationView.setOnNavigationItemSelectedListener(selectedItem -> {
            switch (selectedItem.getItemId()) {
                case R.id.btn_home:
                    openHomeFragment();
                    return true;
                case R.id.btn_transaction:
                    openTransactionFragment();
                    return true;
            }
            return false;
        });
    }

    private void openHomeFragment() {
        List<Transaction> transactions = tinydb.getListObject(TRANSACTIONS_KEY, Transaction.class);
        openFragment(HomeFragment.newFragment(transactions));
    }

    private void openTransactionFragment() {
        openFragment(TransactionFragment.newFragment());
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commitNowAllowingStateLoss();
    }

    @Override
    public void saveTransaction(Transaction transaction) {
        List<Transaction> transactions = tinydb.getListObject(TRANSACTIONS_KEY, Transaction.class);

        if (transactions == null) {
            transactions = new ArrayList<>();
        }
        transactions.add(transaction);

        tinydb.putListObject(TRANSACTIONS_KEY, transactions);
    }
}
