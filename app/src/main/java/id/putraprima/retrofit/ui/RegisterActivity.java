package id.putraprima.retrofit.ui;

import androidx.appcompat.app.AppCompatActivity;
import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.ApiError;
import id.putraprima.retrofit.api.models.Envelope;
import id.putraprima.retrofit.api.models.ErrorUtils;
import id.putraprima.retrofit.api.models.RegisterRequest;
import id.putraprima.retrofit.api.models.RegisterResponse;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class RegisterActivity extends AppCompatActivity {
    EditText edtName, edtEmail, edtPassword, edtConfirmPassword;
    String nama, email, password, confirmPassword;
    RegisterRequest registerRequest;
    Intent intent;
    Boolean status = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //bind view
        edtName = findViewById(R.id.eName);
        edtEmail = findViewById(R.id.eEmail);
        edtPassword = findViewById(R.id.ePassword);
        edtConfirmPassword = findViewById(R.id.eConfirm);

        nama = edtName.getText().toString();
        email = edtEmail.getText().toString();
        password = edtPassword.getText().toString();
        confirmPassword = edtConfirmPassword.getText().toString();



    }

    private void snackbarmaker(String arg){
        Snackbar.make(getWindow().getDecorView().getRootView(), arg, Snackbar.LENGTH_SHORT).show();
    }

    private void registerProcess(){
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        registerRequest = new RegisterRequest(nama, email, password, confirmPassword);
        Call<Envelope<RegisterResponse>> call = apiInterface.doRegister(registerRequest);
        call.enqueue(new Callback<Envelope<RegisterResponse>>() {
            @Override
            public void onResponse(Call<Envelope<RegisterResponse>> call, Response<Envelope<RegisterResponse>> response) {
                if (response.isSuccessful()){
                    status = true;
                    snackbarmaker("Register Berhasil");
                    Toast.makeText(RegisterActivity.this, response.body().toString(), Toast.LENGTH_LONG);

                    intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }else {
                    status = false;
                    ApiError error = ErrorUtils.parseError(response);
                    if (edtName.getText().toString().isEmpty()){
                        snackbarmaker(error.getError().getName().get(0));
                    }else if (edtEmail.getText().toString().isEmpty()){
                        snackbarmaker(error.getError().getEmail().get(0));
                    }else if (edtPassword.getText().toString().length() <= 8){
                        snackbarmaker(error.getError().getPassword().get(0));
                    }else if(edtPassword.getText() != edtConfirmPassword.getText()){
                        snackbarmaker("Password tidak sama");
                    }
                }
            }

            @Override
            public void onFailure(Call<Envelope<RegisterResponse>> call, Throwable t) {
                status = false;
                snackbarmaker("Koneksi Gagal");
            }
        });
    }

    public void handleRegister(View view) {
        //set data
        nama = edtName.getText().toString();
        email = edtEmail.getText().toString();
        password = edtPassword.getText().toString();
        confirmPassword = edtConfirmPassword.getText().toString();

        registerProcess();

        if(status) {
            intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
        }

    }
}
