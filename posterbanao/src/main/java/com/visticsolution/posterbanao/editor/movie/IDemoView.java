package com.visticsolution.posterbanao.editor.movie;

import android.content.Context;

import com.hw.photomovie.render.GLTextureView;

/**
 * Created by huangwei on 2018/9/9.
 */
public interface IDemoView {
    GLTextureView getGLView();
    Context getActivity();
}
