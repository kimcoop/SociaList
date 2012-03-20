package edu.pitt.cs1635group3;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
/*import edu.pitt.cs1635.kac162.proj2.ColorPicker;
import edu.pitt.cs1635.kac162.proj2.DrawableActivity;
import edu.pitt.cs1635.kac162.proj2.R;*/


public class InsideList extends ListActivity {
	//private int selected_color;
	//private RadioGroup g;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listplaceholder);
        /*
        g = (RadioGroup) findViewById(R.id.radio_colors);  // Gets a reference to our radio group

        Button back = (Button) findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	
            	int selected = g.getCheckedRadioButtonId(); // Returns an integer which represents the selected radio button's ID
            	RadioButton b = (RadioButton) findViewById(selected);
            	selected_color = Color.parseColor(b.getText().toString().trim().toLowerCase()); // Parse the color based on color text
            	
                Intent intent = new Intent(ColorPicker.this, DrawableActivity.class);
                intent.putExtra("selected_color", selected_color);	// Pass the color information back to our DrawableActivity
                startActivity(intent);
                finish();
                
            }
        });*/
    }
    
    
}