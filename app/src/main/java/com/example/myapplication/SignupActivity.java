package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hbb20.CountryCodePicker;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    EditText etName, etPhone, etPassword;
    Button btnSignup;
    CountryCodePicker ccp;
    ImageView btnBack;

    String BASE_URL = "http://10.177.237.34/townride/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        btnSignup = findViewById(R.id.btnSignup);
        ccp = findViewById(R.id.ccp);
        btnBack = findViewById(R.id.btnBack);

        ccp.registerCarrierNumberEditText(etPhone);

        btnBack.setOnClickListener(v -> finish());

        // Simple Sign In click
        findViewById(R.id.tvSignIn).setOnClickListener(v ->
                startActivity(new Intent(SignupActivity.this, LoginActivity.class)));

        btnSignup.setOnClickListener(v -> signup());
    }

    private void signup() {

        String name = etName.getText().toString().trim();
        String phone = ccp.getFullNumberWithPlus();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Enter name");
            return;
        }

        if (TextUtils.isEmpty(phone) || phone.length() < 10) {
            etPhone.setError("Enter valid phone");
            return;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            etPassword.setError("Password must be 6+ characters");
            return;
        }

        btnSignup.setEnabled(false);

        StringRequest request = new StringRequest(Request.Method.POST,
                BASE_URL + "signup.php",
                response -> {

                    btnSignup.setEnabled(true);

                    try {
                        JSONObject obj = new JSONObject(response);

                        if (obj.getString("status").equals("success")) {

                            SharedPreferences prefs =
                                    getSharedPreferences("user", MODE_PRIVATE);

                            prefs.edit()
                                    .putBoolean("isLoggedIn", true)
                                    .putInt("user_id", obj.getInt("user_id"))
                                    .putString("name", name)
                                    .apply();

                            Toast.makeText(this,
                                    "Signup Successful",
                                    Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(this, MainActivity.class));
                            finish();

                        } else {
                            Toast.makeText(this,
                                    obj.getString("message"),
                                    Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Toast.makeText(this,
                                "Server error",
                                Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    btnSignup.setEnabled(true);
                    Toast.makeText(this,
                            "Network error",
                            Toast.LENGTH_SHORT).show();
                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("phone", phone);
                params.put("password", password);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                2,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(this).add(request);
    }
}