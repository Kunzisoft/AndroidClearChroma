package com.kunzisoft.androidclearchroma.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.kunzisoft.androidclearchroma.IndicatorMode;
import com.kunzisoft.androidclearchroma.colormode.ColorMode;
import com.kunzisoft.androidclearchroma.listener.OnColorChangedListener;
import com.kunzisoft.androidclearchroma.view.ChromaColorView;

/**
 * Fragment used to show color views. The chromaColorFragment displays each color channel according to the chosen mode.
 * @author JJamet
 */
public class ChromaColorFragment extends Fragment {

    private static final String TAG = "ChromaColorFragment";

    public final static String ARG_INITIAL_COLOR = "arg_initial_color";
    public final static String ARG_COLOR_MODE = "arg_color_mode";
    public final static String ARG_INDICATOR_MODE = "arg_indicator_mode";

    private ChromaColorView chromaColorView;

    private @ColorInt int currentColor;
    private ColorMode colorMode;
    private IndicatorMode indicatorMode;

    /**
     * Must be used to create a new instance of the fragment.
     * @param initialColor Color originally viewed
     * @param colorMode Color mode use
     * @param indicatorMode Indicator used
     * @return New ChromaColorFragment
     */
    public static ChromaColorFragment newInstance(@ColorInt int initialColor, ColorMode colorMode, IndicatorMode indicatorMode) {
        ChromaColorFragment chromaColorFragment = new ChromaColorFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_INITIAL_COLOR, initialColor);
        args.putInt(ARG_COLOR_MODE, colorMode.ordinal());
        args.putInt(ARG_INDICATOR_MODE, indicatorMode.ordinal());

        chromaColorFragment.setArguments(args);

        return chromaColorFragment;
    }

    /**
     * Must be used to create a new instance of the fragment.
     * @param args Only arguments ARG_INITIAL_COLOR, ARG_COLOR_MODE and ARG_INDICATOR_MODE in bundle will be interpreted
     * @return New ChromaColorFragment
     */
    public static ChromaColorFragment newInstance(Bundle args) {
        ChromaColorFragment chromaColorFragment = new ChromaColorFragment();
        chromaColorFragment.setArguments(args);
        return chromaColorFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        chromaColorView = new ChromaColorView(getContext());
        chromaColorView.setCurrentColor(currentColor);
        chromaColorView.setColorMode(colorMode);
        chromaColorView.setIndicatorMode(indicatorMode);

        if (savedInstanceState != null) {
            // Restore last state for checked position.
            assignArguments(savedInstanceState);
        } else if (getArguments() != null) {
            assignArguments(getArguments());
        }

        chromaColorView.setClipToPadding(false);

        // Listener for color selected in real time
        final Activity activity = getActivity();
        final Fragment fragment = getTargetFragment();
        if (activity instanceof OnColorChangedListener) {
            chromaColorView.setOnColorChangedListener(((OnColorChangedListener) activity));
        } else if (fragment instanceof OnColorChangedListener) {
            chromaColorView.setOnColorChangedListener(((OnColorChangedListener) fragment));
        }

        chromaColorView.invalidate();

        return chromaColorView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_INITIAL_COLOR, chromaColorView.getCurrentColor());
        outState.putInt(ARG_COLOR_MODE, chromaColorView.getColorMode().ordinal());
        outState.putInt(ARG_INDICATOR_MODE, chromaColorView.getIndicatorMode().ordinal());
    }

    private void assignArguments(Bundle args) {
        if (args.containsKey(ARG_INITIAL_COLOR)) {
            currentColor = args.getInt(ARG_INITIAL_COLOR);
            if (chromaColorView != null)
                chromaColorView.setCurrentColor(currentColor);
        }
        if (args.containsKey(ARG_COLOR_MODE)) {
            colorMode = ColorMode.getColorModeFromId(args.getInt(ARG_COLOR_MODE));
            if (chromaColorView != null)
                chromaColorView.setColorMode(colorMode);
        }
        if (args.containsKey(ARG_INDICATOR_MODE)) {
            indicatorMode = IndicatorMode.getIndicatorModeFromId(args.getInt(ARG_INDICATOR_MODE));
            if (chromaColorView != null)
                chromaColorView.setIndicatorMode(indicatorMode);
        }
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);

        assignArguments(args);
    }

    /**
     * Get current color selected with channels
     * @return int: current color selected in fragment
     */
    public int getCurrentColor() {
        return chromaColorView.getCurrentColor();
    }

    /**
     * Get color mode used
     * @return ColorMode: color mode in fragment
     */
    public ColorMode getColorMode() {
        return chromaColorView.getColorMode();
    }

    /**
     * Get indicator mode used
     * @return IndicatorMode : indicator in fragment
     */
    public IndicatorMode getIndicatorMode() {
        return chromaColorView.getIndicatorMode();
    }
}
