package com.ecell.end_eavour.fab;

import android.animation.Animator;
import android.view.View;

public class FABAnimation {

    public static void init(View v){
        v.setVisibility( View.GONE );
        v.setTranslationY(v.getHeight());
        v.setAlpha( 0f );       //completely invisi
    }
    public static boolean rotateFab(View view,boolean rotate){
        view.animate().setDuration( 200 ).setListener( new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) { }

                    @Override
                    public void onAnimationEnd(Animator animation) { }

                    @Override
                    public void onAnimationCancel(Animator animation) { }

                    @Override
                    public void onAnimationRepeat(Animator animation) { }
                } ).rotation( rotate?135f:0f );
        return rotate;
    }
    public static void fabopen(View view){
        view.setVisibility( View.VISIBLE );
        view.setAlpha( 0f );
        view.setTranslationY(view.getHeight());
        view.animate()
                .setDuration( 200 )
                .translationY( 0 )
                .alpha( 1f )
                .setListener( new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) { }

                    @Override
                    public void onAnimationEnd(Animator animation) { }

                    @Override
                    public void onAnimationCancel(Animator animation) { }

                    @Override
                    public void onAnimationRepeat(Animator animation) { }
                } ).start();
    }
    public static void fabclose(View view){
        view.setVisibility( View.INVISIBLE );
        view.setAlpha( 1f );
        view.setTranslationY(0);
        view.animate()
                .setDuration( 200 )
                .translationY( view.getHeight() )
                .alpha( 0f )
                .setListener( new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) { }

                    @Override
                    public void onAnimationEnd(Animator animation) { }

                    @Override
                    public void onAnimationCancel(Animator animation) { }

                    @Override
                    public void onAnimationRepeat(Animator animation) { }
                } ).start();
    }
}
