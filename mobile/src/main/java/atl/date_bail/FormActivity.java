package atl.date_bail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class FormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        //Todo: toolbar
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.formToolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //todo: save form data
        //TODO: on create, check is message was passed in
    }
}
