package com.kunzisoft.androidclearchroma.sample;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.kunzisoft.androidclearchroma.ChromaUtil;
import com.kunzisoft.androidclearchroma.listener.OnColorChangedListener;
import com.kunzisoft.androidclearchroma.view.ChromaColorView;

/**
 * An activity that simply displays the color view.
 */
public class ViewColorActivity extends AppCompatActivity implements OnColorChangedListener {

    private static final String SAVED_COLOR = "SAVED_COLOR";
    private @ColorInt int initialColor = Color.GREEN;

    private ActionBar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_color_view);

        toolbar = getSupportActionBar();
        if (toolbar != null) {
            toolbar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState != null) {
            initialColor = savedInstanceState.getInt(SAVED_COLOR, initialColor);
        }

        ChromaColorView chromaColorView = findViewById(R.id.chroma_color_view);
        chromaColorView.setOnColorChangedListener(this);
        chromaColorView.setCurrentColor(initialColor);
        // Set in layout chromaColorView.setIndicatorMode(IndicatorMode.DECIMAL);
        // Set in layout chromaColorView.setColorMode(ColorMode.CMYK255);
        onColorChanged(initialColor);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_COLOR, initialColor);
    }

    @Override
    public void onColorChanged(@ColorInt int color) {
        initialColor = color;
        toolbar.setBackgroundDrawable(new ColorDrawable(color));
        Utility.updatetatusBar(this, color);
        toolbar.setTitle(ChromaUtil.getFormattedColorString(color, false));
    }
}
