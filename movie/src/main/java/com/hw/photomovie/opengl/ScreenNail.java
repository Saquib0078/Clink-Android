package com.hw.photomovie.opengl;

import android.graphics.RectF;

public interface ScreenNail {
    int getWidth();

    int getHeight();

    boolean isReady();

    void setLoadingTexture(StringTexture loadingTexture);

    void draw(GLESCanvas canvas, int x, int y, int width, int height);

    // We do not need to draw this ScreenNail in this frame.
    void noDraw();

    // This ScreenNail will not be used anymore. Release related resources.
    void recycle();

    // This is only used by TileImageView to back up the tiles not yet loaded.
    void draw(GLESCanvas canvas, RectF source, RectF dest);
}
