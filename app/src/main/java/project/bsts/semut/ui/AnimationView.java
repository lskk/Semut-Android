package project.bsts.semut.ui;


import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class AnimationView {

    public interface AnimationViewListener{
        public void onAnimationEnd(Animation anim);
    }

    private Context context;

    public AnimationView(Context context){
        this.context = context;
    }

    public Animation getAnimation(int id, final AnimationViewListener animationViewListener){
        Animation animation = AnimationUtils.loadAnimation(context, id);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(animationViewListener != null) animationViewListener.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return animation;
    }
}
