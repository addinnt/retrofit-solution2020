package id.putraprima.retrofit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.Toast;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.Envelope;
import id.putraprima.retrofit.api.models.LoginResponse;
import id.putraprima.retrofit.api.models.UserInfo;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    public Context context;

    TextView tvName,tvEmail, tvId;
    String sName,sEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //bind view
        tvId   = findViewById(R.id.idUser);
        tvName = findViewById(R.id.Name);
        tvEmail = findViewById(R.id.Email);

        context = getApplicationContext();
        getMe();

    }

    private void getMe() {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        Toast.makeText(context, preference.getString("token",null), Toast.LENGTH_SHORT).show();
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class, "Bearer "+preference.getString("token",null));
        Call<Envelope<UserInfo>> call = service.me();
        call.enqueue(new Callback<Envelope<UserInfo>>() {
            @Override
            public void onResponse(Call<Envelope<UserInfo>> call, Response<Envelope<UserInfo>> response) {
                //Toast.makeText(ProfileActivity.this, response.body().getData().getEmail(), Toast.LENGTH_SHORT).show();
                tvEmail.setText(response.body().getData().getEmail());
                tvName.setText(response.body().getData().getName());
            }

            @Override
            public void onFailure(Call<Envelope<UserInfo>> call, Throwable t) {
               Toast.makeText(ProfileActivity.this, "Error Request", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
