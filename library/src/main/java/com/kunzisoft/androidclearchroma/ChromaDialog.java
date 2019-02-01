package com.kunzisoft.androidclearchroma;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.kunzisoft.androidclearchroma.colormode.ColorMode;
import com.kunzisoft.androidclearchroma.fragment.ChromaColorFragment;
import com.kunzisoft.androidclearchroma.listener.OnColorSelectedListener;

import static com.kunzisoft.androidclearchroma.fragment.ChromaColorFragment.ARG_COLOR_MODE;
import static com.kunzisoft.androidclearchroma.fragment.ChromaColorFragment.ARG_INDICATOR_MODE;
import static com.kunzisoft.androidclearchroma.fragment.ChromaColorFragment.ARG_INITIAL_COLOR;

/**
 * Create Dialog who contains a ChromaColorFragment with buttons for confirmation. <br />
 * To link events to OnColorSelectedListener, it is enough to implement the interface in the parent fragment or parent activity of the dialog.
 * @author JJamet - Pavel Sikun
 */
public class ChromaDialog extends DialogFragment {

    private final static String TAG = "ChromaDialog";

    private final static String ARG_KEY = "ARG_KEY";

    private final static int DEFAULT_COLOR = Color.GRAY;
    private final static ColorMode DEFAULT_MODE = ColorMode.RGB;

    private final static String TAG_FRAGMENT_COLORS = "TAG_FRAGMENT_COLORS";

    private OnColorSelectedListener onColorSelectedListener;

    private ChromaColorFragment chromaColorFragment;

    private View rootView;

    /**
     * Build new instance of dialog
     * @param key Can be null, key link to preference, used when dialog is in a preference view
     * @param initialColor Initial color displayed in the dialog
     * @param colorMode Color mode for selection
     * @param indicatorMode Indicator mode
     * @return New instance of ChromaDialog
     */
    public static ChromaDialog newInstance(@Nullable String key, @ColorInt int initialColor, ColorMode colorMode, IndicatorMode indicatorMode) {
        ChromaDialog fragment = new ChromaDialog();
        Bundle args = makeArgs(initialColor, colorMode, indicatorMode);
        args.putString(ARG_KEY, key);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Build new instance of dialog
     * @param initialColor Initial color displayed in the dialog
     * @param colorMode Color mode for selection
     * @param indicatorMode Indicator mode
     * @return New instance of ChromaDialog
     */
    private static ChromaDialog newInstance(@ColorInt int initialColor, ColorMode colorMode, IndicatorMode indicatorMode) {
        ChromaDialog fragment = new ChromaDialog();
        fragment.setArguments(makeArgs(initialColor, colorMode, indicatorMode));
        return fragment;
    }

    private static Bundle makeArgs(@ColorInt int initialColor, ColorMode colorMode, IndicatorMode indicatorMode) {
        Bundle args = new Bundle();
        args.putInt(ARG_INITIAL_COLOR, initialColor);
        args.putInt(ARG_COLOR_MODE, colorMode.ordinal());
        args.putInt(ARG_INDICATOR_MODE, indicatorMode.ordinal());
        return args;
    }

    /**
     * Builder class for ChromaDialog objects. Provides a convenient way to set the various fields of a Chroma Dialog and generate fragment associated.
     */
    public static class Builder {
        private
        @ColorInt
        int initialColor = DEFAULT_COLOR;
        private ColorMode colorMode = DEFAULT_MODE;
        private IndicatorMode indicatorMode = IndicatorMode.DECIMAL;

        public Builder initialColor(@ColorInt int initialColor) {
            this.initialColor = initialColor;
            return this;
        }

        public Builder colorMode(ColorMode colorMode) {
            this.colorMode = colorMode;
            //TODO
            /*
            IndicatorMode indicatorMode = IndicatorMode.HEX;
            if(colorMode == ColorMode.HSV
                    || colorMode == ColorMode.CMYK
                    || colorMode == ColorMode.HSL) indicatorMode = IndicatorMode.DECIMAL; // cuz HEX is dumb for those
                    */
            return this;
        }

        public Builder indicatorMode(IndicatorMode indicatorMode) {
            this.indicatorMode = indicatorMode;
            return this;
        }

        public ChromaDialog create() {
            return newInstance(initialColor, colorMode, indicatorMode);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        assert getActivity() != null;

        AlertDialog.Builder alertDialogBuilder =  new  AlertDialog.Builder(getActivity());

        rootView = getActivity().getLayoutInflater().inflate(R.layout.color_dialog_fragment, null);

        FragmentManager fragmentManager = getChildFragmentManager();
        chromaColorFragment = (ChromaColorFragment) fragmentManager.findFragmentByTag(TAG_FRAGMENT_COLORS);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (chromaColorFragment == null) {
            chromaColorFragment = ChromaColorFragment.newInstance(getArguments());
            fragmentTransaction.add(R.id.color_dialog_container, chromaColorFragment, TAG_FRAGMENT_COLORS).commit();
        }

        alertDialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final Activity activity = getActivity();
                final Fragment fragment = getTargetFragment();
                if(onColorSelectedListener != null) {
                    onColorSelectedListener.onPositiveButtonClick(chromaColorFragment.getCurrentColor());
                } else if (activity instanceof OnColorSelectedListener) {
                    ((OnColorSelectedListener) activity).onPositiveButtonClick(chromaColorFragment.getCurrentColor());
                } else if (fragment instanceof OnColorSelectedListener) {
                    ((OnColorSelectedListener) fragment).onPositiveButtonClick(chromaColorFragment.getCurrentColor());
                }
                dismiss();
            }
        });

        alertDialogBuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final Activity activity = getActivity();
                final Fragment fragment = getTargetFragment();
                if(onColorSelectedListener != null) {
                    onColorSelectedListener.onNegativeButtonClick(chromaColorFragment.getCurrentColor());
                } else if (activity instanceof OnColorSelectedListener) {
                    ((OnColorSelectedListener) activity).onNegativeButtonClick(chromaColorFragment.getCurrentColor());
                } else if (fragment instanceof OnColorSelectedListener) {
                    ((OnColorSelectedListener) fragment).onNegativeButtonClick(chromaColorFragment.getCurrentColor());
                }
                dismiss();
            }
        });

        alertDialogBuilder.setView(rootView);

        Dialog dialog = alertDialogBuilder.create();
        // request a window without the title
        if (dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                measureLayout((Dialog) dialog);
            }
        });

        return dialog;
    }

    /**
     * Set new dimensions to dialog
     * @param ad dialog
     */
    private void measureLayout(Dialog ad) {
        TypedValue typedValue = new TypedValue();
        getResources().getValue(R.dimen.chroma_dialog_height_multiplier, typedValue, true);
        float heightMultiplier = typedValue.getFloat();
        int height = (int) ((ad.getContext().getResources().getDisplayMetrics().heightPixels) * heightMultiplier);

        getResources().getValue(R.dimen.chroma_dialog_width_multiplier, typedValue, true);
        float widthMultiplier = typedValue.getFloat();
        int width = (int) ((ad.getContext().getResources().getDisplayMetrics().widthPixels) * widthMultiplier);

        if (ad.getWindow() != null)
            ad.getWindow().setLayout(width, height);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    /**
     * Get key associated to preference, or null if key not present
     * @return a String value, or null
     */
    public String getKeyPreference() {
        return getArguments() != null ? getArguments().getString(ARG_KEY) : null;
    }

    /**
     * Get color listener if it was defined by setter else return null
     * @return Interface object who contains events
     */
    public OnColorSelectedListener getOnColorSelectedListener() {
        return onColorSelectedListener;
    }

    /**
     * Defined listener for click on positive and negative button
     * You can implement OnColorSelectedListener in activity or fragment without use this setter for a better usability
     * @param onColorSelectedListener Interface for events
     */
    public void setOnColorSelectedListener(OnColorSelectedListener onColorSelectedListener) {
        this.onColorSelectedListener = onColorSelectedListener;
    }
}
