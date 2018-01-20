package com.example.zaned.cryptoconverter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.view.KeyEvent;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifTextView;


public class Converter extends AppCompatActivity {

    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            GifTextView gif = (GifTextView) findViewById(R.id.gif_container);
            GifDrawable gifFromAssets = new GifDrawable( getAssets(), "images/coolest.gif" );
            gif.setBackground(gifFromAssets);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final EditText fromPrice = (EditText) findViewById((R.id.from_price));
        final EditText fromSymbol = (EditText) findViewById((R.id.from_symbol));
        final EditText toPrice = (EditText) findViewById((R.id.to_price));
        final EditText toSymbol = (EditText) findViewById((R.id.to_symbol));
        spinner = (ProgressBar) findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);


        fromPrice.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    // show spinner
                    spinner.setVisibility(View.VISIBLE);

                    // make request
                    fetchPrices(fromPrice, fromSymbol, toPrice, toSymbol);

                    // hide keyboard
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(fromPrice.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        toPrice.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    // show spinner
                    spinner.setVisibility(View.VISIBLE);

                    // make request
                    fetchPrices(toPrice, toSymbol, fromPrice, fromSymbol);

                    // hide keyboard
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(toPrice.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        fromSymbol.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    // show spinner
                    spinner.setVisibility(View.VISIBLE);

                    // make request
                    fetchPrices(fromPrice, fromSymbol, toPrice, toSymbol);

                    // hide keyboard
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(fromPrice.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        toSymbol.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    // show spinner
                    spinner.setVisibility(View.VISIBLE);

                    // make request
                    fetchPrices(fromPrice, fromSymbol, toPrice, toSymbol);

                    // hide keyboard
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(toSymbol.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
    }

    private void fetchPrices(final EditText fromPrice, EditText fromSymbol, final EditText toPrice, final EditText toSymbol) {
        String url = "https://min-api.cryptocompare.com/data/price?fsym=";
        url = url + fromSymbol.getText() + "&tsyms=" + toSymbol.getText();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        Double rate = 0.0;
                        try {
                            JSONObject json = new JSONObject(response);
                            String rateString = json.get(toSymbol.getText().toString()).toString();
                            System.out.println(rateString);
                            rate = Double.parseDouble(rateString);
                            System.out.println(rate);
                        } catch (JSONException e) {
                            System.err.print(e);
                        }
                        double _fromPrice = Double.parseDouble(fromPrice.getText().toString());
                        double _toPrice = rate * _fromPrice;
                        toPrice.setText(formatNumber(_toPrice, 12));
                        fromPrice.setText(formatNumber(_fromPrice, 12));
                        spinner.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("That didn't work!");
                spinner.setVisibility(View.GONE);
            }
        });
        queue.add(stringRequest);
    }

    private static String formatNumber(Double num, int length) {
        String s = String.valueOf(num);
        if (s.length() < length) {
            return s;
        }
        NumberFormat nFromatter = new DecimalFormat("0.0E0");
        nFromatter.setRoundingMode(RoundingMode.HALF_UP);
        nFromatter.setMinimumFractionDigits(3);
        return nFromatter.format(new BigDecimal(s));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_converter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
