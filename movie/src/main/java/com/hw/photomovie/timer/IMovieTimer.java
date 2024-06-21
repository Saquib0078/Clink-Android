package com.hw.photomovie.timer;

/**
 * Created by huangwei on 2015/5/25.
 */
public interface IMovieTimer {

    void start();

    void pause();

    void setMovieListener(MovieListener movieListener);

    int getCurrentPlayTime();

    void setLoop(boolean loop);

    interface MovieListener {
        void onMovieUpdate(int elapsedTime);

        void onMovieStarted();

        void onMoviedPaused();

        void onMovieResumed();

        void onMovieEnd();
    }
}
